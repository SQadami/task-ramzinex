package ir.salman.ramzinex.crypto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.salman.ramzinex.data.CryptoPortfolio
import ir.salman.ramzinex.data.model.CryptoInvestment
import ir.salman.ramzinex.data.usecase.FetchInvestmentUseCase
import ir.salman.ramzinex.data.usecase.FetchPairIdsUseCase
import ir.salman.ramzinex.data.usecase.FetchPriceUseCase
import ir.salman.ramzinex.data.usecase.OptimizeInvestmentUseCase
import ir.salman.ramzinex.utils.ObservableLoadingCounter
import ir.salman.ramzinex.utils.livedata.Event
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val fetchInvestmentUseCase: FetchInvestmentUseCase,
    private val fetchPairIdsUseCase: FetchPairIdsUseCase,
    private val fetchPriceUseCase: FetchPriceUseCase,
    private val optimizeInvestmentUseCase: OptimizeInvestmentUseCase,
) : ViewModel() {

    private val _loadingState = ObservableLoadingCounter()
    private val _onError: (String?) -> Unit = {
        viewModelScope.launch {
            _pendingActions.emit(
                Event(ErrorMessageAction(it))
            )
        }
    }
    private val _pendingActions = MutableSharedFlow<Event<CryptoUiAction>>()
    private val _isLoading = MutableStateFlow(false)
    private val _portfolioValue = MutableSharedFlow<Double>()
    private val _currentInvestments = MutableSharedFlow<List<CryptoInvestment>>()
    private val _optimizedInvestments = MutableSharedFlow<List<CryptoInvestment>>()

    val isLoading = _isLoading.asStateFlow()
    val pendingActions = _pendingActions.asSharedFlow()
    val portfolioValue = _portfolioValue.asSharedFlow()
    val currentInvestments = _currentInvestments.asSharedFlow()
    val optimizedInvestments = _optimizedInvestments.asSharedFlow()

    private val portfolio = CryptoPortfolio()

    init {
        viewModelScope.launch {
            _loadingState.observable
                .distinctUntilChanged()
                .collect { _isLoading.emit(it) }
        }

        fetchInvestment()
    }

    fun prices() = fetchPriceUseCase.prices()

    private fun fetchInvestment() = viewModelScope.launch {
        fetchInvestmentUseCase.invoke()
            .onStart { _loadingState.addLoader() }
            .onCompletion { _loadingState.removeLoader() }
            .collect { investments ->
                investments.forEach { investment ->
                    portfolio.addInvestment(investment)
                }

                // fetch pairIds for next step
                fetchPairIds()
            }
    }

    private fun fetchPairIds() = viewModelScope.launch {
        fetchPairIdsUseCase.invoke(_onError)
            .onStart { _loadingState.addLoader() }
            .onCompletion { _loadingState.removeLoader() }
            .collect { collectInvestmentPairIds(it) }
    }

    private fun collectInvestmentPairIds(pairIds: Map<String, Int>) {
        // convert investments symbol to pair id to fetch price
        portfolio.listInvestments()
            .map {
                val mapValue = pairIds[it.symbol.lowercase(Locale.ROOT)] ?: Integer.MIN_VALUE
                it.symbol to mapValue
            }.filter { it.second > Integer.MIN_VALUE }
            .distinct()
            .let { fetchPrices(it) }
    }

    // the input is pair of symbol and pairId
    private fun fetchPrices(pairIds: List<Pair<String, Int>>) =
        viewModelScope.launch {
            fetchPriceUseCase.invoke(pairIds, _onError)
                .collect { fetchPriceDone ->
                    if (fetchPriceDone) {
                        calculateInvestment()
                    }
                }
        }

    private fun calculateInvestment() {
        val portfolioValue = portfolio.calculatePortfolioValue(fetchPriceUseCase.prices())
        // use items that we have current price for them
        val currentInvestments = portfolio.listInvestments()
            .filter { fetchPriceUseCase.prices().containsKey(it.symbol) }

        val optimizedInvestments = optimizeInvestmentUseCase.invoke(
            currentInvestments, fetchPriceUseCase.prices(), 20000.0
        )

        viewModelScope.launch {
            _portfolioValue.emit(portfolioValue)
            _currentInvestments.emit(currentInvestments)
            _optimizedInvestments.emit(optimizedInvestments)
        }
    }
}

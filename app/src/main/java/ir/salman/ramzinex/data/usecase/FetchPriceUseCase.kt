package ir.salman.ramzinex.data.usecase

import ir.salman.ramzinex.data.CryptoApiService
import ir.salman.ramzinex.utils.ObservableLoadingCounter
import ir.salman.ramzinex.utils.bodyOrThrow
import ir.salman.ramzinex.utils.withRetry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import retrofit2.awaitResponse
import javax.inject.Inject

class FetchPriceUseCase @Inject constructor(
    private val apiService: CryptoApiService,
    private val ioDispatcher: CoroutineDispatcher,
) {

    private val loadingState = ObservableLoadingCounter()
    private val prices = mutableMapOf<String, Double>()

    fun prices() = prices

    /**
     * fetch price by input list
     */
    suspend fun invoke(
        pairIds: List<Pair<String, Int>>,
        onError: (String?) -> Unit
    ) =
        flow {
            pairIds.forEach { pair ->
                fetchPriceList(pair.second.toString(), onError)
                    .onStart { loadingState.addLoader() }
                    .onCompletion { loadingState.removeLoader() }
                    .collect { price ->
                        prices[pair.first] = price
                    }
            }

            loadingState.observable
                .distinctUntilChanged()
                .collect {
                    emit(it.not())
                }
        }.flowOn(ioDispatcher)


    /**
     * fetch price by pairId
     */
    private suspend fun fetchPriceList(
        pairId: String,
        onError: (String?) -> Unit
    ) =
        flow {
            runCatching {
                withRetry {
                    apiService.currencyPrice(pairId)
                        .awaitResponse()
                        .bodyOrThrow()
                        .data
                        .price
                }
            }.onFailure {
                onError.invoke(it.message)
            }.onSuccess {
                emit(it)
            }
        }.flowOn(ioDispatcher)
}
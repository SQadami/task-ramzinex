package ir.salman.ramzinex.data.usecase

import ir.salman.ramzinex.data.CryptoApiService
import ir.salman.ramzinex.utils.bodyOrThrow
import ir.salman.ramzinex.utils.withRetry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.awaitResponse
import java.util.Locale
import javax.inject.Inject

class FetchPairIdsUseCase @Inject constructor(
    private val apiService: CryptoApiService,
    private val ioDispatcher: CoroutineDispatcher,
) {

    /**
     * fetch pairIds.
     */
    suspend fun invoke(
        onError: (String?) -> Unit
    ) = flow {
        runCatching {
            val data = withRetry {
                apiService.currencyPairs()
                    .awaitResponse()
                    .bodyOrThrow()
            }

            data.data.filter { it.quoteCurrencySymbol.en.lowercase(Locale.ROOT) == "usdt" }
                .associate {
                    it.baseCurrencySymbol.en.lowercase(Locale.ROOT) to it.pairId
                }

        }.onFailure {
            onError.invoke(it.message)
        }.onSuccess {
            emit(it)
        }
    }.flowOn(ioDispatcher)
}

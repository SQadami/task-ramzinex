package ir.salman.ramzinex.data.usecase

import ir.salman.ramzinex.data.model.CryptoInvestment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchInvestmentUseCase @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
) {

    private val investments = listOf(
        CryptoInvestment("BTC", 2, 45000.0, "2023-01-15"),
        CryptoInvestment("ETH", 5, 3000.0, "2023-02-20"),
        CryptoInvestment("LTC", 10, 150.0, "2023-03-10"),
        CryptoInvestment("XRP", 1000, 2.0, "2023-04-05"),
        CryptoInvestment("ADA", 2000, 1.0, "2023-04-15"),
        CryptoInvestment("DOT", 50, 30.0, "2023-05-10"),
        CryptoInvestment("BNB", 20, 400.0, "2023-05-15"),
        CryptoInvestment("XLM", 5000, 0.5, "2023-06-01"),
        CryptoInvestment("SOL", 10, 1500.0, "2023-06-15"),
        CryptoInvestment("DOGE", 50000, 0.01, "2023-07-10"),
        CryptoInvestment("LINK", 100, 20.0, "2023-08-05"),
        CryptoInvestment("MATIC", 2000, 1.5, "2023-08-15"),
        CryptoInvestment("UNI", 50, 40.0, "2023-09-01"),
        CryptoInvestment("XMR", 5, 300.0, "2023-09-15"),
        CryptoInvestment("AAVE", 10, 700.0, "2023-10-10"),
        CryptoInvestment("ATOM", 100, 10.0, "2023-11-05"),
        CryptoInvestment("AVAX", 100, 30.0, "2023-11-15"),
        CryptoInvestment("FIL", 1000, 5.0, "2023-12-01"),
    )

    /**
     * fetch investments.
     */
    suspend fun invoke() =
        flow {
            emit(investments)
        }.flowOn(ioDispatcher)
}
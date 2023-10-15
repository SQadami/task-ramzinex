package ir.salman.ramzinex.data.model

data class CryptoInvestment(
    val symbol: String,
    val quantity: Int,
    val purchasePrice: Double,
    val purchaseDate: String
)

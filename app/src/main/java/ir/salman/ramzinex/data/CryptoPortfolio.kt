package ir.salman.ramzinex.data

import ir.salman.ramzinex.data.model.CryptoInvestment

class CryptoPortfolio {

    private val investments = mutableListOf<CryptoInvestment>()

    fun addInvestment(investment: CryptoInvestment) {
        investments.add(investment)
    }

    fun listInvestments(): List<CryptoInvestment> {
        return investments
    }

    fun calculatePortfolioValue(currentPrices: Map<String, Double>): Double {
        var portfolioValue = 0.0
        for (investment in investments) {
            val symbol = investment.symbol
            val quantity = investment.quantity
            val currentPrice = currentPrices[symbol] ?: 0.0
            portfolioValue += quantity * currentPrice
        }
        return portfolioValue
    }
}
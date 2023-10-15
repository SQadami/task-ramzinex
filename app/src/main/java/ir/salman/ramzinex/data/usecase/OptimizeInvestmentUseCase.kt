package ir.salman.ramzinex.data.usecase

import android.util.Log
import ir.salman.ramzinex.data.model.CryptoInvestment

class OptimizeInvestmentUseCase {

    // Implement the optimization logic here
    // For example, you can optimize the investments based on maxBudget
    // and return the optimized list of investments.
    fun invoke(
        investments: List<CryptoInvestment>, currentPrices: Map<String, Double>, maxBudget: Double
    ): List<CryptoInvestment> {
        val visited = BooleanArray(investments.size)

        val maxProfit = knapSack(investments, currentPrices, maxBudget, investments.size, visited)
        Log.d("OptimizeInvestmentUseCase", "max profit: $maxProfit")

        val optimizedInvestment = mutableListOf<CryptoInvestment>()
        for (i in investments.indices) {
            if (visited[i]) {
                optimizedInvestment.add(investments[i])
            }
        }
        return optimizedInvestment
    }

    fun knapSack(
        investments: List<CryptoInvestment>,
        currentPrices: Map<String, Double>,
        budget: Double,
        n: Int,
        visited: BooleanArray
    ): Double {
        // Base Case
        if (n == 0) {
            return if (budget != 0.0) Integer.MIN_VALUE.toDouble() else 0.0
        }

        // If weight of the nth item is
        // more than Knapsack capacity W,
        // then this item cannot be included
        // in the optimal solution
        val currentItem = investments[n - 1]
        val totalInvestment = currentItem.purchasePrice * currentItem.quantity
        return if (totalInvestment > budget) {
            knapSack(investments, currentPrices, budget, n - 1, visited)
        } else {
            val v1 = BooleanArray(visited.size)
            arraycopy(visited, v1)
            val v2 = BooleanArray(visited.size)
            arraycopy(visited, v2)
            v1[n - 1] = true

            val ans1 = knapSack(investments, currentPrices, budget, n - 1, v2)
            val totalProfit =
                (currentPrices[currentItem.symbol]!! - currentItem.purchasePrice) * currentItem.quantity
            val ans2 = totalProfit + knapSack(
                investments, currentPrices, budget - totalInvestment, n - 1, v1
            )

            if (ans1 > ans2) {
                arraycopy(v2, visited)
                ans1
            } else {
                arraycopy(v1, visited)
                ans2
            }
        }
    }

    private fun arraycopy(
        src: BooleanArray,
        dest: BooleanArray,
    ) {
        System.arraycopy(src, 0, dest, 0, dest.size)
    }
}
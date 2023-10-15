package ir.salman.ramzinex.crypto

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.salman.ramzinex.R
import ir.salman.ramzinex.data.model.CryptoInvestment
import ir.salman.ramzinex.databinding.FragmentPlaygroundBinding
import ir.salman.ramzinex.utils.observeWithLifecycle
import ir.salman.ui.binding.BindingFragment

@AndroidEntryPoint
class CryptoFragment :
    BindingFragment<FragmentPlaygroundBinding>(R.layout.fragment_playground) {

    private val viewModel: CryptoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
    }

    private fun initData() =
        with(viewModel) {
            isLoading.observeWithLifecycle(this@CryptoFragment) { setLoading(it) }
            portfolioValue.observeWithLifecycle(this@CryptoFragment) { setPortfoliValue(it) }
            currentInvestments.observeWithLifecycle(this@CryptoFragment) { setCurrentInvestment(it) }
            optimizedInvestments.observeWithLifecycle(this@CryptoFragment) {
                setOptimizedInvestment(it)
            }
            pendingActions.observeWithLifecycle(this@CryptoFragment) {
                it.runOnContent {
                    when (this) {
                        is ErrorMessageAction -> {
                            Toast.makeText(requireContext(), message, LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    private fun setLoading(loading: Boolean) =
        with(binding) {
            indicator.isVisible = loading
            portfolio.isVisible = !loading
            ci.isVisible = !loading
            oi.isVisible = !loading
        }

    private fun setPortfoliValue(value: Double) {
        binding.portfolio.text = buildString { append("Portfolio Value: $value") }
    }

    private fun setCurrentInvestment(ci: List<CryptoInvestment>) {
        binding.ci.text = buildString {
            append("Current Investments:")
            ci.forEach {
                append("\n")
                append("${it.symbol}: Quantity=${it.quantity}, " + "Current Price=${viewModel.prices()[it.symbol]}")
            }
        }
    }

    private fun setOptimizedInvestment(oi: List<CryptoInvestment>) {
        binding.oi.text = buildString {
            append("Optimized Investments:")
            oi.forEach {
                append("\n")
                append("${it.symbol}: Quantity=${it.quantity}")
            }
        }
    }
}
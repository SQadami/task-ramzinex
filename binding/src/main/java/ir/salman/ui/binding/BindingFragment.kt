package ir.salman.ui.binding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

abstract class BindingFragment<B : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int
) : Fragment() {

    private var _binding: B? = null

    /**
     * This can be accessed by the child fragments
     * Only valid between onCreateView and onDestroyView
     */
    val binding: B get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        DataBindingUtil
            .inflate<B>(
                inflater,
                layoutRes,
                container,
                false
            ).apply {
                _binding = this
                initialize()
            }.root

    /**
     * Removing the binding reference when not needed is recommended as it avoids memory leak
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.let { findAndDetachRecyclerViewAdapter(it.root) }
        _binding = null
    }

    /**
     * use this method to prevent from memory leak
     */
    private fun findAndDetachRecyclerViewAdapter(view: View) {
        when (view) {
            is RecyclerView -> view.adapter = null
            is ViewGroup -> {
                for (c in 0 until view.childCount) {
                    val child = view.getChildAt(c)
                    findAndDetachRecyclerViewAdapter(child)
                }
            }
        }
    }

    open fun B.initialize() {}
}
package ir.salman.ui.binding

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding

abstract class BindingActivity<B : ViewDataBinding> : AppCompatActivity() {

    private var _binding: B? = null

    /**
     * This can be accessed by the child activities
     * Only valid between onCreate and onDestroy
     */
    val binding: B get() = _binding!!

    abstract val bindingInflater: (LayoutInflater) -> B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingInflater.invoke(layoutInflater).apply {
            _binding = this
            setContentView(root)
            initialize()
        }
    }

    /**
     * Removing the binding reference when not needed is recommended as it avoids memory leak
     */
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    open fun B.initialize() {}
}
package ir.salman.ramzinex.utils.livedata

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun getContent(): T = content

    fun <R> runOnContent(block: T.() -> R): R? =
        getContentIfNotHandled()?.run(block = block)
}
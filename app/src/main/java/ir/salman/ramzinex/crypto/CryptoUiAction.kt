package ir.salman.ramzinex.crypto

sealed class CryptoUiAction

data class ErrorMessageAction(val message: String?) : CryptoUiAction()
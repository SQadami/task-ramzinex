package ir.salman.ramzinex.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyPair(
    @Json(name = "pair_id") val pairId: Int,
    @Json(name = "base_currency_symbol") val baseCurrencySymbol: CurrencySymbol,
    @Json(name = "quote_currency_symbol") val quoteCurrencySymbol: CurrencySymbol,
)
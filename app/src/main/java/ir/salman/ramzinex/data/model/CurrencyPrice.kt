package ir.salman.ramzinex.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyPrice(
    @Json(name = "price") val price: Double,
)
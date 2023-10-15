package ir.salman.ramzinex.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PriceDto(
    @Json(name = "status") val status: Int,
    @Json(name = "data") val data: CurrencyPrice,
)
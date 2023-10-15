package ir.salman.ramzinex.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencySymbol(
    @Json(name = "en") val en: String,
    @Json(name = "fr") val fr: String? = null,
)
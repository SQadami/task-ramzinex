package ir.salman.ramzinex.data

import ir.salman.ramzinex.data.model.PairDto
import ir.salman.ramzinex.data.model.PriceDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CryptoApiService {

    @GET("/exchange/api/v1.0/exchange/pairs")
    fun currencyPairs(): Call<PairDto>

    @GET("/exchange/api/v1.0/exchange/orderbooks/{pair_id}/market_sell_price")
    fun currencyPrice(@Path("pair_id") pairId: String): Call<PriceDto>
}
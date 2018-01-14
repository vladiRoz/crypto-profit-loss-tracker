package crypro.profit.loss.tracker.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by vladi on 24/11/2017.
 */
class BittrexApi : CryptoExchangeApi {

    interface BittrexApi {
        @GET("getticker")
        fun getLastPrice(@Query("market") ticker : String) : Call<CoinDataResponse>

        // todo getmarkets
//        https://bittrex.com/api/v1.1/public/getmarkets
    }

    private val IVALID_MARKET = "INVALID MARKET"
    private val BASE_URL = "https://bittrex.com/api/v1.1/public/"
    private val api : BittrexApi

    init {

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        api = retrofit.create(BittrexApi::class.java)
    }

    override fun getLastPrice(ticker : String): Call<CoinDataResponse> {
        return api.getLastPrice(ticker)
    }

    override fun isValidResponse(message: String?) : Boolean{
        return !message.equals(IVALID_MARKET)
    }

    override fun getAvailableMarkets(): List<String> {
        return listOf("BTC", "USDT", "ETH")
    }


//    https://bittrex.com/api/v1.1//public/getticker?market=USDT-BTC
}


package crypro.profit.loss.tracker.api

import retrofit2.Call


/**
 * Created by vladi on 26/12/17.
 */
interface CryptoExchangeApi {

    companion object {
        enum class CONSTS(s: String) {USDT("USDT")}
    }

    fun getLastPrice(ticker : String) : Call<CoinDataResponse>
    fun isValidResponse(message: String?) : Boolean
    fun getAvailableMarkets() : List<String>
}
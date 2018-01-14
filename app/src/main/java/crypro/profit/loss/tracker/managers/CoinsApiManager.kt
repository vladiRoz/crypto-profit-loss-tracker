package crypro.profit.loss.tracker.managers

import crypro.profit.loss.tracker.api.CoinResponse
import crypro.profit.loss.tracker.api.DataCompletion

/**
 * Created by vladi on 27/11/2017.
 */
interface CoinsApiManager {
    fun getCoinStats(market : String, completion : DataCompletion<CoinResponse?>)
    fun init()
    fun destroy()
    fun checkValidResponse(message: String?) : Boolean

}
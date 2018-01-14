package crypro.profit.loss.tracker.api

import com.squareup.moshi.Json

/**
 * Created by vladi on 26/12/17.
 */
class CoinResponse(val ticker : String, val coinDataResponse : CoinDataResponse)

class CoinDataResponse (
        val success : Boolean,
        val message : String,
        val result : CoinResultData
)

class CoinResultData (
        @Json(name = "Bid") val bid : Double,
        @Json(name = "Ask") val ask : Double,
        @Json(name = "Last") val last : Double
)
package crypro.profit.loss.tracker.factories

import crypro.profit.loss.tracker.api.BittrexApi
import crypro.profit.loss.tracker.api.CryptoExchangeApi

/**
 * Created by vladi on 26/12/17.
 */
class ExchangeFactory {

    enum class SupportedExchanges {BITTREX}

    companion object {
        fun createExchangeApi(exchange : SupportedExchanges) : CryptoExchangeApi {
            var api : CryptoExchangeApi
            when (exchange){
                SupportedExchanges.BITTREX -> api = BittrexApi()
                else -> api = BittrexApi()
            }

            return api
        }
    }
}
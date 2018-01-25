package crypro.profit.loss.tracker

import android.app.Application
import android.content.Context
import android.util.Log
import crypro.profit.loss.tracker.api.CoinDataResponse
import crypro.profit.loss.tracker.api.CoinResponse
import crypro.profit.loss.tracker.api.CoinResultData
import crypro.profit.loss.tracker.api.DataCompletion
import crypro.profit.loss.tracker.managers.CoinsApiManager

/**
 * Created by vladi on 16/12/17.
 */
class CoinsStatusApplication : Application() {

    companion object {
        private var instance : CoinsStatusApplication? = null

        fun getApplicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    init {
        instance = this
    }


    class DefaultManager private constructor() : CoinsApiManager {

        init {
            println("This ($this) is a singleton")
        }

        private object Holder {
            val INSTANCE = DefaultManager()
        }

        companion object {
            val instance: DefaultManager by lazy { Holder.INSTANCE }
        }

        var b: String? = null

        private val list1 = listOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0)
        private val list2 = list1.reversed()

        private var index1 = 0
        private var index2 = list1.size - 1

        override fun init() {

        }

        override fun destroy() {

        }

        override fun getCoinStats(market: String, completion: DataCompletion<CoinResponse?>) {
            Log.i("DefaultManager", "index: " + index1 + " value: " + list1.get(index1))
            try {
                when (market.toLowerCase()) {
                    "btc-eth" -> completion.onResponse(CoinResponse("btc-eth", CoinDataResponse(true, "", CoinResultData(0.0, 0.0, 0.004))))
                    "btc-xrp" -> completion.onResponse(CoinResponse("btc-eth", CoinDataResponse(true, "", CoinResultData(0.0, 0.0, list1.get(index1++)))))
                    "btc-ltc" -> completion.onResponse(CoinResponse("btc-eth", CoinDataResponse(true, "", CoinResultData(0.0, 0.0, list2.get(index2--)))))
                }
            } catch (ex: IndexOutOfBoundsException) {
                Log.i("CoinJobServiceTest", "panic")
            }
        }

        override fun checkValidResponse(message: String?): Boolean {
            return true
        }

    }

}
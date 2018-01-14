package crypro.profit.loss.tracker.ui

import crypro.profit.loss.tracker.persistance.Coin


/**
 * Created by vladi on 08/12/2017.
 */
interface NewCoinListener {
    fun onCoinAdded(coin : Coin)
    fun onCoinUpdated(coin : Coin)
//    fun newCoinAdded(ticker1 : String, ticker2 : String, avgPosition : Double)
}
package crypro.profit.loss.tracker.controllers

import android.support.v4.app.FragmentManager
import android.widget.ImageButton
import crypro.profit.loss.tracker.persistance.Coin

/**
 * Created by vladi on 27/11/2017.
 */
interface CoinsListController : Delegation {

    enum class OptionButtonMode {ADD, DELETE}

    interface ReceivedCoinListener {
        fun onCoinReceived(coin : Coin)
        fun onCoinsDeleted(coins : List<Coin>)
        fun onRefresh()
    }

    fun showCoinsUI (fm : FragmentManager, container : Int)
    fun getCoinLastPrice(ticker : String)
    fun stopFetch()
    fun startFetch()
    fun refresh()
    fun onNewData(coin: Coin)
    fun onUpdatedData(coin: Coin)
    fun addReceivedCoinListener(listener : ReceivedCoinListener)
    fun onBackPressed() : Boolean
    fun delegateOptionButtonView(fab : ImageButton)
    fun setOptionButtonMode(optionButtonMode: OptionButtonMode)
    fun deleteCoin(coin : Coin)
    fun showEdit(coin: Coin)
}
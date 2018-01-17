package status.portfolio.crypto.vladi.cryptoportfoliostatus.controllers

import android.support.v4.app.FragmentManager
import crypro.profit.loss.tracker.api.Completion
import crypro.profit.loss.tracker.api.DataCompletion
import crypro.profit.loss.tracker.persistance.Coin
import crypro.profit.loss.tracker.persistance.CoinsPersistence
import crypro.profit.loss.tracker.ui.NewCoinListener

/**
 * Created by vladi on 04/12/2017.
 */
interface AddCoinsController{
    fun showAddCoinsUI (fm : FragmentManager, container : Int)
    fun showAddCoinsUI(fm: FragmentManager, container: Int, coin: Coin?)
    fun setPersistenceHandler(persistence : CoinsPersistence)
    fun insertCoin(coin : Coin, listener : Completion<Long>)
    fun updateCoin(coin : Coin)
    fun findCoin(ticker1 : String, ticker2 : String, listener : Completion<Coin?>)
    fun getAllCoins(listener : DataCompletion<List<Coin>>)
    fun setNewDataListener(listener : NewCoinListener)
    fun notifyNewCoinAdded(coin : Coin)
    fun notifyCoinUpdated(coin : Coin)
}
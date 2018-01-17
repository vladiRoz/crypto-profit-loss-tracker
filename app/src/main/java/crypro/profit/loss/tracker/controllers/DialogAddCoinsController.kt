package crypro.profit.loss.tracker.controllers

import android.support.v4.app.FragmentManager
import crypro.profit.loss.tracker.api.Completion
import crypro.profit.loss.tracker.api.DataCompletion
import crypro.profit.loss.tracker.persistance.Coin
import crypro.profit.loss.tracker.persistance.CoinsPersistence
import crypro.profit.loss.tracker.ui.AddNewCoinsDialog
import crypro.profit.loss.tracker.ui.NewCoinListener

/**
 * Created by vladi on 04/12/2017.
 */
class DialogAddCoinsController(persistence: CoinsPersistence?) : AddCoinsControllerImpl(persistence) {

    private var listener : NewCoinListener? = null
    private var newCoinsDialog : AddNewCoinsDialog? = null

    override fun showAddCoinsUI(fm: FragmentManager, container: Int) {
        showAddCoinsUI(fm, container, null)
    }

    override fun showAddCoinsUI(fm: FragmentManager, container: Int, coin: Coin?) {
        newCoinsDialog = AddNewCoinsDialog.newInstance(coin)
        newCoinsDialog?.setController(this)
        newCoinsDialog?.show(fm, newCoinsDialog?.tag)
    }

    override fun insertCoin(coin: Coin, listener: Completion<Long>) {
        persistence?.insertCoin(coin, listener)
    }

    override fun updateCoin(coin: Coin) {
        persistence?.updateCoin(coin)
    }

    override fun getAllCoins(listener : DataCompletion<List<Coin>>) {
        persistence?.getAllCoins(listener)
    }

    override fun setNewDataListener(listener: NewCoinListener) {
        this.listener = listener
    }

    override fun notifyNewCoinAdded(coin: Coin) {
        listener?.onCoinAdded(coin)
    }

    override fun notifyCoinUpdated(coin: Coin) {
        listener?.onCoinUpdated(coin)
    }

    override fun findCoin(ticker1: String, ticker2: String, listener: Completion<Coin?>) {
        persistence?.findCoin(ticker1, ticker2, listener)
    }


}
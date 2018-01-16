package crypro.profit.loss.tracker.ui

import crypro.profit.loss.tracker.controllers.CoinsListController
import crypro.profit.loss.tracker.persistance.Coin

/**
 * Created by vladi on 27/11/2017.
 */
interface CoinsUI {
    fun setController(contoller : CoinsListController)
    fun coinDataReceived(value: Coin?)
    fun coinsDataReceived(coins : ArrayList<Coin>)
    fun getCoinsToDelete() : ArrayList<Coin>
    fun onCoinDeleted(position : Int)
    fun onCoinsDeleted(deletedCoin: ArrayList<Coin>)
    fun onBackPressed() : Boolean
    fun showEmptyState(isShow : Boolean)
    fun setDeleteMode(isDeleteMode: Boolean)
}
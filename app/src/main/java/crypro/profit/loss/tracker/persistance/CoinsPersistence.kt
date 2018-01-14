package crypro.profit.loss.tracker.persistance

import crypro.profit.loss.tracker.api.Completion

/**
 * Created by vladi on 04/12/2017.
 */
interface CoinsPersistence {

    fun insertCoin(coin: Coin, listener: Completion<Long>)
    fun updateCoin(coin : Coin)
    fun delete(coin: List<Coin>)
    fun getAllCoins(listener : Completion<List<Coin>>)
    fun findCoin(ticker1 : String, ticker2 : String, listener : Completion<Coin?>)

}
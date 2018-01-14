package crypro.profit.loss.tracker.controllers

import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.res.ResourcesCompat
import android.widget.ImageButton
import com.annimon.stream.Stream
import crypro.profit.loss.tracker.CoinsStatusApplication
import crypro.profit.loss.tracker.R
import crypro.profit.loss.tracker.api.CoinResponse
import crypro.profit.loss.tracker.api.Completion
import crypro.profit.loss.tracker.api.DataCompletion
import crypro.profit.loss.tracker.managers.CoinRequestManager
import crypro.profit.loss.tracker.managers.CoinsApiManager
import crypro.profit.loss.tracker.persistance.Coin
import crypro.profit.loss.tracker.persistance.CoinsPersistence
import crypro.profit.loss.tracker.ui.CoinsUI
import crypro.profit.loss.tracker.ui.NewCoinListener
import crypro.profit.loss.tracker.utils.Utils

/**
 * Created by vladi on 27/11/2017.
 */
class CoinsListControllerImpl(val ui: CoinsUI, val apiManager: CoinsApiManager = CoinRequestManager(), val persistence: CoinsPersistence?) : CoinsListController {

    private var coinMap: HashMap<String, Coin>? = null
    private var coinsListeners: ArrayList<CoinsListController.ReceivedCoinListener>? = null
    private var fragmentManager: FragmentManager? = null
    private var container: Int? = null
    private var optionButton: ImageButton? = null
    private var deleteDrawable: Drawable? = null
    private var addDrawable: Drawable? = null
    private var optionButtonMode = CoinsListController.OptionButtonMode.ADD

    init {
        coinMap = HashMap()
        coinsListeners = ArrayList()

        val resources = CoinsStatusApplication.getApplicationContext().resources
        addDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_add_black_18dp, null)
        deleteDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_delete_black_24dp, null)
    }

    override fun getCoinLastPrice(ticker: String) {
        apiManager.getCoinStats(ticker, completion)
    }

    private var completion = object : DataCompletion<CoinResponse?> {

        override fun onError(message: String) {
            ui.coinDataReceived(null)
        }

        override fun onResponse(response: CoinResponse?) {

            val coin = coinMap?.get(response?.ticker)
            if (apiManager.checkValidResponse(response?.coinDataResponse?.message) &&
                    response?.coinDataResponse?.success!!) {

                coin?.lastPrice = response.coinDataResponse.result.last
                persistence?.updateCoin(coin!!)
                notifyReceivedCoinListeners(coin!!)
                ui.coinDataReceived(coin)

            } else {
                // TODO remove after getMarkets api call
                val list = listOf(coin) as List<Coin>
                persistence?.delete(list)
            }
        }
    }

    override fun showCoinsUI(fm: FragmentManager, container: Int) {
        fragmentManager = fm
        this.container = container
        ui.setController(this)
        fm.beginTransaction().replace(container, ui as Fragment).commit()
    }

    override fun stopFetch() {
        apiManager.destroy()
    }

    override fun startFetch() {
        apiManager.init()

        persistence?.getAllCoins(object : Completion<List<Coin>> {
            override fun onResponse(coins: List<Coin>) {
                ui.showEmptyState(coins.isEmpty())
                Stream.of(coins).forEach { coin -> notifyReceivedCoinListeners(coin) }
                ui.coinsDataReceived(coins as ArrayList<Coin>)
                Stream.of(coins).forEach { coin -> requestExchangePrice(coin) }
            }
        })
    }

    fun requestExchangePrice(coin: Coin) {
        val market = Utils.getMarketName(coin.ticker1, coin.ticker2)
        coinMap?.put(market, coin)
        apiManager.getCoinStats(market, completion)
    }

    override fun onNewData(coin: Coin) {
        ui.showEmptyState(false)
        requestExchangePrice(coin)
    }

    override fun onUpdatedData(coin: Coin) {
        notifyReceivedCoinListeners(coin)
        coinMap?.put(Utils.getMarketName(coin.ticker1, coin.ticker2), coin)
        ui.coinDataReceived(coin)
    }

    override fun addReceivedCoinListener(listener: CoinsListController.ReceivedCoinListener) {
        coinsListeners?.add(listener)
    }

    override fun onBackPressed(): Boolean {
        return ui.onBackPressed()
    }

    override fun <T> delegate(actionName: String, extraObject: T) {
        when (actionName) {
            CoinAction.OptionButtonPressed -> onOptionButtonPressed(extraObject)
        }
    }

    private fun <T> onOptionButtonPressed(extraObject: T) {
        when (optionButtonMode) {
            CoinsListController.OptionButtonMode.ADD -> onAddNewCoins(extraObject as NewCoinListener)
            CoinsListController.OptionButtonMode.DELETE -> onDeleteCoins()
        }
    }

    private fun onDeleteCoins() {
        val deleteList = ui.getCoinsToDelete()
        if (deleteList.size > 0) {
            persistence?.delete(deleteList)
            notifyDeletedCoins(deleteList)
            ui.onCoinsDeleted(deleteList)
            Stream.of(deleteList).forEach { coin -> coinMap?.remove(Utils.getMarketName(coin.ticker1, coin.ticker2)) }
        }
    }

    private fun onAddNewCoins(listener: NewCoinListener) {
        var addCoinsController = DialogAddCoinsController(persistence)
        addCoinsController.setNewDataListener(listener)
        if (fragmentManager != null && container != null) {
            addCoinsController.showAddCoinsUI(fragmentManager!!, container!!)
        }
    }

    private fun setOptionButtonImage(drawable: Drawable?) {
        optionButton?.setImageDrawable(drawable)
    }

    override fun delegateOptionButtonView(button: ImageButton) {
        this.optionButton = button
    }

    override fun setOptionButtonMode(mode: CoinsListController.OptionButtonMode) {
        optionButtonMode = mode
        when (mode) {
            CoinsListController.OptionButtonMode.ADD -> setOptionButtonImage(addDrawable)
            CoinsListController.OptionButtonMode.DELETE -> setOptionButtonImage(deleteDrawable)
        }
    }

    private fun notifyReceivedCoinListeners(coin: Coin) {
        Stream.of(coinsListeners).forEach { l -> l.onCoinReceived(coin) }
    }

    private fun notifyDeletedCoins(coins: List<Coin>) {
        Stream.of(coinsListeners).forEach { l -> l.onCoinsDeleted(coins) }
    }

    private fun notifyRefreshCoins() {
        Stream.of(coinsListeners).forEach { l -> l.onRefresh() }
    }

    override fun refresh() {
        notifyRefreshCoins()
        Stream.of(coinMap).forEach { (_, coin) ->
            val market = Utils.getMarketName(coin.ticker1, coin.ticker2)
            apiManager.getCoinStats(market, completion)
        }
    }

}
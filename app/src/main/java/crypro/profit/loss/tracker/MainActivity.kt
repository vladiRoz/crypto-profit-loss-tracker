package crypro.profit.loss.tracker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.annimon.stream.Stream
import com.crashlytics.android.Crashlytics
import crypro.profit.loss.tracker.api.CoinDataResponse
import crypro.profit.loss.tracker.api.CoinResponse
import crypro.profit.loss.tracker.api.CoinResultData
import crypro.profit.loss.tracker.api.DataCompletion
import crypro.profit.loss.tracker.controllers.CoinAction
import crypro.profit.loss.tracker.controllers.CoinsListController
import crypro.profit.loss.tracker.controllers.CoinsListControllerImpl
import crypro.profit.loss.tracker.factories.ExchangeFactory
import crypro.profit.loss.tracker.managers.CoinRequestManager
import crypro.profit.loss.tracker.managers.CoinsApiManager
import crypro.profit.loss.tracker.persistance.Coin
import crypro.profit.loss.tracker.persistance.RoomPersistence
import crypro.profit.loss.tracker.ui.CoinFragmentUICallback
import crypro.profit.loss.tracker.ui.CoinsListFragment
import crypro.profit.loss.tracker.ui.MoreInfoFragment
import crypro.profit.loss.tracker.ui.NewCoinListener
import crypro.profit.loss.tracker.utils.Utils
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.upper_header_layout.*


// todo
// dagger
// controller rx fix - add another component for request manager
// more info fragment
// verify market while user inputs coins / dialog fields, red if not valid + ticker 2 only btc/usdt
// alarms
// remove all coin? impossible

class MainActivity : AppCompatActivity(), View.OnClickListener, NewCoinListener, CoinsListController.ReceivedCoinListener, CoinFragmentUICallback {

    private var coinStorage: RoomPersistence? = null
    private var coinsListController: CoinsListController? = null
    private var totalPL = 0.0
    private var plViewCoins: HashMap<String, Coin>? = null

    init {
        coinStorage = RoomPersistence(this)
        plViewCoins = HashMap()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Fabric.with(this, Crashlytics())

        val exchangeApi = ExchangeFactory.createExchangeApi(ExchangeFactory.SupportedExchanges.BITTREX)

        coinsListController = CoinsListControllerImpl(CoinsListFragment.newInstance(), CoinRequestManager(exchangeApi), coinStorage)
        coinsListController?.addReceivedCoinListener(this)
        coinsListController?.delegateOptionButtonView(fab)

        fab.setOnClickListener(this)
        info_button.setOnClickListener(this)

        coinsListController?.showCoinsUI(supportFragmentManager, main_container.id)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            fab.id -> coinsListController?.delegate(CoinAction.OptionButtonPressed, this)
            info_button.id -> onClickMoreInfo()
        }
    }

    override fun onCoinAdded(coin: Coin) {
        coinsListController?.onNewData(coin)
    }

    override fun onCoinUpdated(coin: Coin) {
        coinsListController?.onUpdatedData(coin)
    }


    override fun onRefresh() {

    }

    override fun onCoinReceived(coin: Coin) {

        val key = Utils.getMarketName(coin)
        if (plViewCoins?.containsKey(key) == false) {
            totalPL += Utils.calcPL(coin.lastPrice, coin.avgPosition)
        } else {
            val prevCoin = plViewCoins?.get(key)
            val prevCoinPL = Utils.calcPL(prevCoin!!.lastPrice, prevCoin.avgPosition)
            totalPL += Utils.calcPL(coin!!.lastPrice, coin.avgPosition) - prevCoinPL
        }

        plViewCoins?.put(key, Coin(coin))
        setPLTextView()
    }

    override fun onCoinsDeleted(coins: List<Coin>) {
        Stream.of(coins).forEach { coin ->
            val key = Utils.getMarketName(coin)
            plViewCoins?.remove(key)
            totalPL -= Utils.calcPL(coin.lastPrice, coin.avgPosition)
            setPLTextView()
        }
    }

    private fun setPLTextView() {
        mainPLTextView.visibility = View.VISIBLE
        val total = Utils.getReadableFormat(totalPL, "######", "##")
        mainPLTextView.setText("$total%")
    }

    override fun onBackPressed() {
        if (coinsListController?.onBackPressed() == false) {
            super.onBackPressed()
        }
    }


    private fun onClickMoreInfo() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(MoreInfoFragment.toString())
        transaction.add(container.id, MoreInfoFragment.newInstance()).commit()
    }

    class DefaultManager : CoinsApiManager {

        override fun init() {

        }

        override fun destroy() {

        }

        override fun getCoinStats(market: String, completion: DataCompletion<CoinResponse?>) {
            when (market.toLowerCase()) {
                "btc-eth" -> completion.onResponse(CoinResponse("btc-eth", CoinDataResponse(true, "", CoinResultData(0.0, 0.0, 0.004))))
            }
        }

        override fun checkValidResponse(message: String?): Boolean {
            return true
        }

    }

    override fun toggleFabVisibility(isVisible: Boolean) {
        if (isVisible) fab.show() else fab.hide()
    }


}


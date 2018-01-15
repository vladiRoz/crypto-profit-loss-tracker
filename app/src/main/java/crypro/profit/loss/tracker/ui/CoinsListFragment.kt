package crypro.profit.loss.tracker.ui

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import crypro.profit.loss.tracker.R
import crypro.profit.loss.tracker.controllers.CoinsListController
import crypro.profit.loss.tracker.persistance.Coin
import crypro.profit.loss.tracker.utils.inflate
import kotlinx.android.synthetic.main.main_fragment_layout.*


/**
 * Created by vladi on 22/11/2017.
 */

class CoinsListFragment<T : CoinFragmentUICallback> : BaseFragment<T>(), CoinsUI, CoinAdapter.OnCoinClickListener, SwipeRefreshLayout.OnRefreshListener, BottomToolbar.ClickListener {

    private var controller: CoinsListController? = null
    private var coinsToDelete: ArrayList<Coin> = ArrayList<Coin>()
    private var emptyStateView: View? = null
    private var chosenCoin: Coin? = null

    companion object {
        fun newInstance(): CoinsListFragment<CoinFragmentUICallback> {
            return CoinsListFragment()
        }
    }

    private val recycler by lazy {
        coins_recycler.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        coins_recycler.setHasFixedSize(true)

        val dividerItemDecoration = DividerItemDecoration(context, LinearLayout.VERTICAL)
        coins_recycler.addItemDecoration(dividerItemDecoration)

        val adapter = CoinAdapter()
        adapter.setOnDeleteModeListener(this)
        coins_recycler.adapter = adapter
        coins_recycler
    }

    private val bottomBar by lazy {
        bottom_toolbar.setItemsClickListener(this)
        bottom_toolbar
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.main_fragment_layout)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshView.setOnRefreshListener(this)

        controller?.startFetch()
    }

    override fun coinDataReceived(coin: Coin?) {
        (recycler?.adapter as CoinAdapter).addCoin(coin)
    }

    override fun setController(controller: CoinsListController) {
        this.controller = controller
    }

    override fun onBackPressed(): Boolean {
        if (chosenCoin != null) {
            activityCallback?.toggleFabVisibility(true)
            bottomBar.visibility = View.GONE
            return true
        } else
            return (recycler?.adapter as CoinAdapter).onBackPressed()
    }

    override fun onDeleteMode(isDeleteMode: Boolean) {
        disableBottomBar()
        if (isDeleteMode) {
            controller?.setOptionButtonMode(CoinsListController.OptionButtonMode.DELETE)
        } else {
            controller?.setOptionButtonMode(CoinsListController.OptionButtonMode.ADD)
        }
    }

    override fun onItemClicked(coin: Coin) {
        disableDeleteMode()
        chosenCoin = coin
        bottomBar.visibility = View.VISIBLE
        bottomBar.init()

        activityCallback?.toggleFabVisibility(false)
    }

    override fun onCoinDeleteClicked(coin: Coin) {
        if (coinsToDelete?.remove(coin) == false) {
            coinsToDelete?.add(coin)
        }
    }

    override fun getCoinsToDelete(): ArrayList<Coin> {
        val newList = coinsToDelete?.toMutableList()
        coinsToDelete?.clear()
        return newList as ArrayList<Coin>
    }

    override fun onCoinDeleted(position: Int) {
        (recycler?.adapter as CoinAdapter).deleteCoin(position)
    }

    override fun onCoinsDeleted(deletedList: ArrayList<Coin>) {
        (recycler?.adapter as CoinAdapter).deleteCoins(deletedList)
    }

    override fun showEmptyState(isShow: Boolean) {
        if (isShow && emptyStateView == null) {
            emptyStateView = coins_list_empty_view.inflate()
        } else {
            emptyStateView?.visibility = if (isShow) View.VISIBLE else View.GONE
        }
    }

    override fun coinsDataReceived(coins: ArrayList<Coin>) {
        (recycler?.adapter as CoinAdapter).add(coins)
    }

    override fun onRefresh() {
        controller?.refresh()
        // Bittrex exchange allows only on market at a time
        // I don't want to show the refresh too much time
        Handler().postDelayed({ if (isAdded) refreshView.isRefreshing = false }, 1000)
    }

    private fun disableBottomBar() {
        chosenCoin = null
        bottomBar.visibility = View.GONE
        activityCallback?.toggleFabVisibility(true)
    }

    private fun disableDeleteMode() {
        coinsToDelete.clear()
        (recycler?.adapter as CoinAdapter).setDeleteMode(false)
        controller?.setOptionButtonMode(CoinsListController.OptionButtonMode.ADD)
    }

    override fun onItemClicked(item: BottomToolbar.Items) {
        when (item) {
            BottomToolbar.Items.delete -> onDeleteItem()
            BottomToolbar.Items.edit -> onEditItem()
            BottomToolbar.Items.chart -> onItemChart()
            BottomToolbar.Items.alarm -> onItemAlarm()
        }
    }

    private fun onItemAlarm() {
        Snackbar.make(bottomBar, "In Development", Snackbar.LENGTH_SHORT)
    }

    private fun onItemChart() {
        Snackbar.make(bottomBar, "In Development", Snackbar.LENGTH_SHORT)
    }

    private fun onEditItem() {
        Snackbar.make(bottomBar, "In Development", Snackbar.LENGTH_SHORT)
    }

    private fun onDeleteItem() {
        controller?.deleteCoin(chosenCoin!!)
        (recycler?.adapter as CoinAdapter).deleteCoin(chosenCoin!!)
        disableBottomBar()
    }


}


















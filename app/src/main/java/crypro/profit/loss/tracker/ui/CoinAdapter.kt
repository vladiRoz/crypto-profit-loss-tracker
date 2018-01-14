package crypro.profit.loss.tracker.ui

import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.coin_view_layout.view.*
import crypro.profit.loss.tracker.persistance.Coin
import crypro.profit.loss.tracker.utils.Utils
import crypro.profit.loss.tracker.utils.inflate
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import android.widget.TextView
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.res.ResourcesCompat
import android.widget.Button
import com.annimon.stream.Stream
import crypro.profit.loss.tracker.CoinsStatusApplication
import crypro.profit.loss.tracker.R
import crypro.profit.loss.tracker.api.CryptoExchangeApi


/**
 * Created by vladi on 23/11/2017.
 */
class CoinAdapter : RecyclerView.Adapter<CoinAdapter.ViewHolder>() {

    private var coins: ArrayList<Coin> = ArrayList<Coin>()
    private var recycler : RecyclerView? = null
    private var deleteMode = false
    private var coinClickListener: OnCoinClickListener? = null
    private var profitButtonColor: Int = 0
    private var lossButtonColor: Int = 0


    interface OnCoinClickListener {
        fun onItemClicked(coin : Coin)
        fun onDeleteMode(isDeleteMode: Boolean)
        fun onCoinDeleteClicked(coin: Coin)
    }

    companion object {
        var PRICE_DIGITS = "######"
        var PRICE_AFTER_DOTS = "########"

        var PL_DIGITS = "######"
        var PL_AFTER_DOTS = "##"
    }

    init {
        val resources = CoinsStatusApplication.getApplicationContext().resources
        profitButtonColor = ResourcesCompat.getColor(resources, R.color.pl_button_profit, null)
        lossButtonColor = ResourcesCompat.getColor(resources, R.color.pl_button_loss, null)
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(parent?.inflate(R.layout.coin_view_layout))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(position)
    }

    override fun getItemCount(): Int {
        return coins.size
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            val coinData = coins.get(position)
            initTickers(coinData, itemView.pairTextView)
            itemView.buyinTextView.text = Utils.getReadableFormat(coinData.avgPosition, PRICE_DIGITS, PRICE_AFTER_DOTS)
            initLastPrice(coinData, itemView.priceTextView)
            initPLButton(coinData, itemView.plButton)
            initDeleteFunctionality(coinData)
        }

        private fun initDeleteFunctionality(coinData: Coin) {
            if (deleteMode){
                itemView.deleteBox.visibility = View.VISIBLE
                itemView.deleteBox.isChecked = false
            } else {
                itemView.deleteBox.isChecked = true
                itemView.deleteBox.visibility = View.GONE
            }

            itemView.setOnLongClickListener {
                deleteMode = true
                coinClickListener?.onDeleteMode(true)
                notifyDataSetChanged()
                true
            }

            itemView.deleteBox.setOnClickListener {
                coinClickListener?.onCoinDeleteClicked(coinData)
            }

            itemView.setOnClickListener { coinClickListener?.onItemClicked(coinData) }
        }

        private fun initLastPrice(coinData: Coin, lastPrice: TextView) {
            if (coinData.lastPrice > -1.0) {
                var afterDotFormat = PRICE_AFTER_DOTS
                if (coinData.ticker2.equals(CryptoExchangeApi.Companion.CONSTS.USDT.name.toLowerCase())){
                    afterDotFormat = PL_AFTER_DOTS
                }

                lastPrice.text = Utils.getReadableFormat(coinData.lastPrice, PRICE_DIGITS, afterDotFormat)
            }
        }

        private fun initPLButton(coinData: Coin, plButton: Button) {
            if (coinData.lastPrice > -1.0) {
                val plNum = Utils.calcPL(coinData.lastPrice, coinData.avgPosition)
                plButton.text = Utils.getReadableFormat(plNum, PL_DIGITS, PL_AFTER_DOTS) + "%"
                val bgShape = plButton.background as GradientDrawable
                bgShape.setColor(if (plNum >= 0) profitButtonColor else lossButtonColor)
            }
        }

        private fun initTickers(coinData: Coin, pairTextView: TextView) {
            val ticker1 = "${coinData.ticker1} / ${coinData.ticker2}".toUpperCase()
            val ss = SpannableString(ticker1)
            val secondTickerStart = ticker1.indexOf("/")
            ss.setSpan(TextAppearanceSpan(itemView.context, R.style.pairStyle), secondTickerStart, ticker1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            pairTextView.setText(ss, TextView.BufferType.SPANNABLE)
        }
    }

    fun addCoin(coin: Coin?) {
        if (coin != null) {
            val coinIndex = coins.indexOf(coin)
            if (coinIndex == (-1)){
            // new coin added
                coins.add(coin)
                notifyItemInserted(coins.size - 1)
            } else {
            // update data received
                coins[coinIndex] = coin
                notifyItemChanged(coinIndex)
            }
        }
    }

    fun onBackPressed(): Boolean {
        if (deleteMode) {
            deleteMode = false
            coinClickListener?.onDeleteMode(false)
            notifyDataSetChanged()
            return true
        }
        return false
    }

    fun setDeleteMode(isShow : Boolean){
        deleteMode = isShow
        notifyDataSetChanged()
    }

    fun setOnDeleteModeListener(listener: OnCoinClickListener) {
        this.coinClickListener = listener
    }

    fun deleteCoin(position: Int) {
        findViewAtPosition(position).itemView.deleteBox.isChecked = false
        coins.removeAt(position)
        notifyItemRemoved(position)
    }

    fun deleteCoins(deletedList: ArrayList<Coin>) {
        Stream.of(deletedList).forEach { coin -> findViewAtPosition(findPosition(coin)).itemView.deleteBox.isChecked = false}
        coins.removeAll(deletedList)
        notifyDataSetChanged()
    }

    fun add(coins: ArrayList<Coin>) {
        this.coins.addAll(coins)
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        recycler = recyclerView
    }

    private fun findViewAtPosition(position : Int) : ViewHolder {
        return recycler?.findViewHolderForAdapterPosition(position) as ViewHolder
    }

    private fun findPosition(coin : Coin): Int {
        return coins.lastIndexOf(coin)
    }




}
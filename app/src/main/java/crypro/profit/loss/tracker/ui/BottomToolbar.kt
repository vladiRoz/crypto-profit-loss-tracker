package crypro.profit.loss.tracker.ui

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import crypro.profit.loss.tracker.R
import crypro.profit.loss.tracker.utils.inflate
import kotlinx.android.synthetic.main.bottom_toolbar_layout.view.*
import kotlinx.android.synthetic.main.circle_button_layout.view.*


/**
 * Created by vladi on 12/1/18.
 */
class BottomToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr) {

    enum class Items(var res : Int) { edit(R.string.edit), chart(R.string.chart), alarm(R.string.alarm), delete(R.string.delete);
        var desc : String = ""
    }

    private val recycler by lazy { bottom_recycler }
    private var clickListener : ClickListener? = null

    interface ClickListener {
        fun onItemClicked(item : Items)
    }

    fun initEnumItems(){
        val values = Items.values()
        val iterator = values.iterator()
        while (iterator.hasNext()){
            val next = iterator.next()
            next.desc = resources.getString(next.res)
        }
    }

    init {

        View.inflate(context, R.layout.bottom_toolbar_layout, this)

        setBackgroundColor(ResourcesCompat.getColor(resources, R.color.lower_toolbar, null))

        recycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
            setHasFixedSize(true)
            isLayoutFrozen = true
            adapter = BottomBarButtonAdapter()
            addItemDecoration(BottomToolbarRecyclerDecoration(resources.getDimensionPixelSize(R.dimen.bottom_toolbar_items_margin)))
            layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.bottom_toolbar_item_layout_animation)
        }

        initEnumItems()
    }

    private var drawablesData: IntArray = intArrayOf(R.drawable.ic_edit_black_18dp,
            R.drawable.ic_show_chart_black_18dp,
            R.drawable.ic_add_alarm_black_18dp,
            R.drawable.ic_delete_black_18dp)

    private var descsData = listOf(Items.edit, Items.chart, Items.alarm, Items.delete)

    inner class BottomBarButtonAdapter : RecyclerView.Adapter<BottonsViewHolder>() {

        private val ITEMS_COUNT = 4

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BottonsViewHolder {
            return BottonsViewHolder(parent?.inflate(R.layout.circle_button_layout))
        }

        override fun onBindViewHolder(holder: BottonsViewHolder?, position: Int) {
            holder?.bind(position)
        }

        override fun getItemCount(): Int {
            return ITEMS_COUNT
        }
    }

    inner class BottonsViewHolder(var view: View?) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            itemView.imageView.setImageResource(drawablesData.get(position))
            itemView.descTextView.text = descsData.get(position).desc

            itemView.setOnClickListener { clickListener?.onItemClicked(descsData.get(position))}
        }

    }

    fun setItemsClickListener(listener : ClickListener){
        clickListener = listener
    }

    fun init() {
        recycler.adapter.notifyDataSetChanged()
        recycler.scheduleLayoutAnimation()
    }

}

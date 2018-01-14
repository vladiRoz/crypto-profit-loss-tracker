package crypro.profit.loss.tracker.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import crypro.profit.loss.tracker.R

/**
 * Created by vladi on 8/1/18.
 */
class SimpleDividerItemDecoration(context: Context?) : RecyclerView.ItemDecoration() {
    private var mDividerLight: Drawable? = null
    private var mDividerDark: Drawable? = null

    init {
        if (context != null) {
            mDividerLight = context.resources.getDrawable(R.drawable.line_divider)
            mDividerDark = context.resources.getDrawable(R.drawable.line_divider_shadow)
        }
    }


    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount

        if (childCount <= 1) {
            return
        }

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + mDividerDark!!.intrinsicHeight
            val top_dark = bottom + 2
            val bottom_dark = top_dark + mDividerLight!!.intrinsicHeight

            mDividerDark!!.setBounds(left + 4, top_dark, right - 4, bottom_dark)
            mDividerDark!!.draw(c)

            mDividerLight!!.setBounds(left + 4, top, right - 4, bottom)
            mDividerLight!!.draw(c)
        }
    }
}
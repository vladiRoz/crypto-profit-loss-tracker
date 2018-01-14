package crypro.profit.loss.tracker.ui

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by vladi on 12/1/18.
 */
class BottomToolbarRecyclerDecoration(private var margin : Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = margin
        outRect.left = margin
    }

}
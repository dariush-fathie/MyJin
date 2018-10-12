package myjin.pro.ahoora.myjin.customClasses

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

import myjin.pro.ahoora.myjin.utils.Utils


class SliderDecoration
/**
 * @param gridSpacingDp
 */
(private val context: Context, gridSpacingDp: Int) : RecyclerView.ItemDecoration() {

    private val mSizeGridSpacingPx: Int = Utils.pxFromDp(context, gridSpacingDp.toFloat()).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        val itemCount = parent.adapter!!.itemCount

        outRect.top = mSizeGridSpacingPx
        outRect.bottom = mSizeGridSpacingPx

        outRect.left = mSizeGridSpacingPx / 2
        outRect.right = mSizeGridSpacingPx / 2

        if (itemPosition == 0) {
            outRect.left = mSizeGridSpacingPx
        }

        if (itemPosition == itemCount.minus(1)) {
            outRect.right = mSizeGridSpacingPx
        }

    }

}

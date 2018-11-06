package myjin.pro.ahoora.myjin.customClasses

import android.content.Context
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

import myjin.pro.ahoora.myjin.utils.Utils


class ThreeColGridDecorationSpec
/**
 * @param gridSpacingDp
 */
(private val context: Context, gridSpacingDp: Int) : RecyclerView.ItemDecoration() {

    private val mSizeGridSpacingPx: Int = Utils.pxFromDp(context, gridSpacingDp.toFloat()).toInt()
    private val useBottomOffset = true


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        val itemCount = parent.adapter!!.itemCount

        outRect.top = mSizeGridSpacingPx / 2

        /*if (itemPosition == 0 || itemPosition == 1) {
            outRect.top = (int) Utils.INSTANCE.pxFromDp(context, 56);
        }*/
        if (itemPosition == 0 || itemPosition == 1 || itemPosition == 2) {
            outRect.top = mSizeGridSpacingPx
        }

        if (itemPosition % 3 == 0) {
            outRect.left = mSizeGridSpacingPx/ 2
            outRect.right = mSizeGridSpacingPx
        } else if (itemPosition % 3 == 1) {
            outRect.right = mSizeGridSpacingPx / 2
            outRect.left = mSizeGridSpacingPx / 2
        } else if (itemPosition % 3 == 2) {
            outRect.right = mSizeGridSpacingPx/ 2
            outRect.left = mSizeGridSpacingPx
        }

        outRect.bottom = mSizeGridSpacingPx / 2

        if (itemCount % 3 == 0) {
            if (itemPosition == itemCount - 1 || itemPosition == itemCount - 2 || itemPosition == itemCount - 3) {
                outRect.bottom = Utils.pxFromDp(context, (20 + mSizeGridSpacingPx).toFloat()).toInt()
            }
        } else if (itemCount % 3 == 2) {
            if (itemPosition == itemCount - 1 || itemPosition == itemCount - 2) {
                outRect.bottom = Utils.pxFromDp(context, (20 + mSizeGridSpacingPx).toFloat()).toInt()
            }
        } else if (itemCount % 3 == 1) {
            if (itemPosition == itemCount - 1) {
                outRect.bottom = Utils.pxFromDp(context, (20 + mSizeGridSpacingPx).toFloat()).toInt()
            }
        }

    }

}


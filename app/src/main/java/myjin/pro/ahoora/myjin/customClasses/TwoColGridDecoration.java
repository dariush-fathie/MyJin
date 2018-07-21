package myjin.pro.ahoora.myjin.customClasses;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import myjin.pro.ahoora.myjin.utils.Utils;


public class TwoColGridDecoration extends RecyclerView.ItemDecoration {

    private int mSizeGridSpacingPx;
    private Context context;
    private boolean useBottomOffset = true;

    /**
     * @param gridSpacingDp
     */

    public TwoColGridDecoration(Context context, int gridSpacingDp) {
        mSizeGridSpacingPx = (int) Utils.INSTANCE.pxFromDp(context, gridSpacingDp);
        this.context = context;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        int itemCount = parent.getAdapter().getItemCount();

        outRect.top = mSizeGridSpacingPx / 2;

        /*if (itemPosition == 0 || itemPosition == 1) {
            outRect.top = (int) Utils.INSTANCE.pxFromDp(context, 56);
        }*/
        if (itemPosition == 0 || itemPosition == 1) {
            outRect.top = mSizeGridSpacingPx;
        }

        if (itemPosition % 2 == 0) {
            outRect.left = mSizeGridSpacingPx;
            outRect.right = mSizeGridSpacingPx / 2;
        } else if (itemPosition % 2 == 1) {
            outRect.right = mSizeGridSpacingPx;
            outRect.left = mSizeGridSpacingPx / 2;
        }

        outRect.bottom = mSizeGridSpacingPx / 2;

        if (itemCount % 2 == 0) {
            if (itemPosition == itemCount - 1 || itemPosition == itemCount - 2 ) {
                outRect.bottom = (int) Utils.INSTANCE.pxFromDp(context, 72 + mSizeGridSpacingPx);
            }
        } else if (itemCount % 2 == 1) {
            if (itemPosition == itemCount - 1) {
                outRect.bottom = (int) Utils.INSTANCE.pxFromDp(context, 72 + mSizeGridSpacingPx);
            }
        }

    }

}


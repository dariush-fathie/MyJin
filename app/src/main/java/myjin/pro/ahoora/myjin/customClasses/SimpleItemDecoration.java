package myjin.pro.ahoora.myjin.customClasses;


import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import myjin.pro.ahoora.myjin.utils.Utils;


public class SimpleItemDecoration extends RecyclerView.ItemDecoration {

    private int mSizeGridSpacingPx;
    private Context context;


    public SimpleItemDecoration(Context context, int gridSpacingDp) {
        mSizeGridSpacingPx = (int) Utils.INSTANCE.pxFromDp(context, gridSpacingDp);
        this.context = context;


    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        int itemCount = parent.getAdapter().getItemCount();

        outRect.top = mSizeGridSpacingPx / 2;

        if (itemPosition == 0) {
            outRect.top = mSizeGridSpacingPx;
        }
        outRect.left = mSizeGridSpacingPx;
        outRect.right = mSizeGridSpacingPx;
        outRect.bottom = mSizeGridSpacingPx / 2;

        if (itemPosition == itemCount - 1) {
            outRect.bottom = (int) Utils.INSTANCE.pxFromDp(context, 56);
        }
    }

}


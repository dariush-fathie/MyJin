package myjin.pro.ahoora.myjin.customClasses;

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CustomBottomSheetBehavior<V : View> : BottomSheetBehavior<V> {
    private var mAllowUserDragging = true

    /**
     * Default constructor for instantiating BottomSheetBehaviors.
     */
    constructor() : super() {}

    /**
     * Default constructor for inflating BottomSheetBehaviors from layout.
     *
     * @param context The [Context].
     * @param attrs   The [AttributeSet].
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    fun setAllowUserDragging(allowUserDragging: Boolean) {
        mAllowUserDragging = allowUserDragging
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout?, child: V, event: MotionEvent?): Boolean {
        return if (!mAllowUserDragging) {
            false
        } else super.onInterceptTouchEvent(parent, child, event)
    }
}
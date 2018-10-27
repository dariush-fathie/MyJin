package myjin.pro.ahoora.myjin.customClasses

import android.content.Context
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.utils.Colors

class CustomCardTab : TabLayout {

    private var flag = true

    private val mContext: Context

    private var pDark: Int = 0
    private var plight: Int = 0
    private var title : Int = 0
    private var transparent = 0
    private var semiWhite = 0


    private fun init(context: Context) {
        pDark = Colors(context).colorPrimaryDark
        plight = Colors(context).green
        title = Colors(context).title
        transparent = Colors(context).transparent
        semiWhite = Colors(context).transparent
    }

    constructor(context: Context) : super(context) {
        mContext = context
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mContext = context
        init(context)
    }


    override fun addTab(tab: Tab, position: Int, selected: Boolean) {
        val view = LayoutInflater.from(mContext).inflate(R.layout.custom_tablayout_item, this, false)
        val textView = view.findViewById<AppCompatTextView>(R.id.tv_customTabTitle)
        val cardView = view.findViewById<CardView>(R.id.cv_customTabContainer)
        textView.text = tab.text
        tab.customView = view

        if (selected) {
            flag = false
            textView.setTextColor(pDark)
            textView.textSize = 14f
            textView.setBackgroundResource(R.drawable.empty)
        } else {
            textView.textSize = 13f
            tab.customView?.scaleY = 0.85f
            tab.customView?.scaleX = 0.85f
            cardView.cardElevation = 0f
            cardView.setCardBackgroundColor(transparent)
            textView.setTextColor(title)
            textView.setBackgroundResource(R.drawable.dot_rect_back)
        }

        super.addTab(tab, position, selected)
    }


}
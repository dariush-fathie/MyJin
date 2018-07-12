package myjin.pro.ahoora.myjin.customClasses;

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import myjin.pro.ahoora.myjin.R

class CustomTabLayout : TabLayout {

    lateinit var textView: TextView
    val mContext: Context
    var selectedPosition = -1
    val TAG = "CustomTabLayout"

    constructor(context: Context) : super(context) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mContext = context
    }

    override fun addTab(tab: Tab, selected: Boolean) {
        val view = LayoutInflater.from(mContext).inflate(R.layout.custom_tab_item, null, false)
        textView = view.findViewById<AppCompatTextView>(R.id.text1)
        textView.text = tab.text
        tab.customView = view
        super.addTab(tab, selected)
    }



}
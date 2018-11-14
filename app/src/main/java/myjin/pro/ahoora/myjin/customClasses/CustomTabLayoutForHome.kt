package myjin.pro.ahoora.myjin.customClasses

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.tabs.TabLayout
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.utils.Colors

class CustomTabLayoutForHome : TabLayout {

    private var flag = true

    private val mContext: Context

    constructor(context: Context) : super(context) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mContext = context
    }


    override fun addTab(tab: Tab, position: Int, selected: Boolean) {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_custom_tab, this, false)
        val textView = view.findViewById<AppCompatTextView>(R.id.text1)
        val imageView = view.findViewById<AppCompatImageView>(R.id.icon)
        textView.text = tab.text
        imageView.setImageDrawable(tab.icon)
        tab.customView = view

       /* if (flag) {
            flag = false
            val accent = Colors(mContext).white
            textView.setTextColor(accent)
            imageView.setColorFilter(accent)
            textView.visibility = View.VISIBLE
        }*/

        super.addTab(tab, position, selected)
    }


}
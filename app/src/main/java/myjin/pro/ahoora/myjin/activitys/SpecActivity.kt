package myjin.pro.ahoora.myjin.activitys

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_spec.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.SpecAdapter
import myjin.pro.ahoora.myjin.customClasses.ThreeColGridDecorationSpec
import myjin.pro.ahoora.myjin.utils.StaticValues

class SpecActivity : AppCompatActivity() {
    private var provId = 19
    private var cityId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spec)

        if (intent != null) {
            provId = intent.getIntExtra(StaticValues.PROVID, 19)
            cityId = intent.getIntExtra(StaticValues.CITYID, 1)

        }

        init_()
    }

    private fun init_() {
        val adapter = SpecAdapter(this@SpecActivity,provId,cityId)
        rv_spec.layoutManager = RtlGridLayoutManager(this@SpecActivity, 3)
        rv_spec.adapter = adapter

        while (rv_spec.itemDecorationCount > 0) {
            rv_spec.removeItemDecorationAt(0)
        }

        val itemDecoration = ThreeColGridDecorationSpec(this@SpecActivity, 8)
        rv_spec.addItemDecoration(itemDecoration)
    }


    inner class RtlGridLayoutManager : GridLayoutManager {

        constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

        constructor(context: Context, spanCount: Int) : super(context, spanCount) {}

        constructor(context: Context, spanCount: Int, orientation: Int, reverseLayout: Boolean) : super(context, spanCount, orientation, reverseLayout) {}

        override fun isLayoutRTL(): Boolean {
            return true
        }
    }
}

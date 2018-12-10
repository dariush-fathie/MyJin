package myjin.pro.ahoora.myjin.activities.tools

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener
import kotlinx.android.synthetic.main.activity_bmichart.*
import myjin.pro.ahoora.myjin.R

class BMIChartActivity : AppCompatActivity(), View.OnTouchListener, View.OnClickListener {
    override fun onClick(p0: View?) {
        finish()
    }

    override fun onTouch(v: View?, p1: MotionEvent?): Boolean {
        v!!.parent.requestDisallowInterceptTouchEvent(true)

        return false
    }


    private var weight = 0
    private var height = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmichart)

        weight = ruler_picker_weight.currentValue
        height = ruler_picker_height.currentValue

        tv_height.text = "$height cm"
        tv_weight.text = "$weight kg"


        tv_description.setOnTouchListener(this)
        iv_goback.setOnClickListener(this)

        tv_description.movementMethod = ScrollingMovementMethod()


        ruler_picker_height.setValuePickerListener(object : RulerValuePickerListener {
            override fun onIntermediateValueChange(selectedValue: Int) {
                height = selectedValue
                tv_height.text = "$selectedValue cm"
            }

            override fun onValueChange(selectedValue: Int) {
                tv_height.text = "$selectedValue cm"
            }
        })

        ruler_picker_weight.setValuePickerListener(object : RulerValuePickerListener {
            override fun onIntermediateValueChange(selectedValue: Int) {
                tv_weight.text = "$selectedValue kg"
            }

            override fun onValueChange(selectedValue: Int) {
                weight = selectedValue
                tv_weight.text = "$selectedValue kg"
            }
        })


        btn_evaluate.setOnClickListener {
            var fHeight = height.toFloat() / 100f // convert height from cm to m
            fHeight *= fHeight

            val result = weight.toFloat() / fHeight
            Log.e("result", result.toString())

            startActivity(Intent(this, BmiResultActivity::class.java).apply {
                putExtra("result", result)
            })

        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
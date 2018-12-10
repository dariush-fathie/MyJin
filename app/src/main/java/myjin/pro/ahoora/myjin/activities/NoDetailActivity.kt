package myjin.pro.ahoora.myjin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_no_detail.*
import myjin.pro.ahoora.myjin.R

class NoDetailActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        finishActi()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_detail)

        iv_goback.setOnClickListener(this)

    }
    private fun finishActi() {
        finish()
    }
}

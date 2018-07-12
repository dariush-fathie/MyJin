package myjin.pro.ahoora.myjin.activitys

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.server_err_layout.*
import myjin.pro.ahoora.myjin.R

class ServerStatusActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        tryAgain()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_status)
        setClickListeners()
    }

    private fun tryAgain(){

       startActivity(Intent(this@ServerStatusActivity,StartActivity::class.java))
        finish()
    }


    private fun setClickListeners() {
        btn_tryAgain_se.setOnClickListener(this)

    }


}

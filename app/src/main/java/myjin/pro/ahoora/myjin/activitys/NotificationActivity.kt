package myjin.pro.ahoora.myjin.activitys

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_notification.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.NotificationModel


class NotificationActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)


        val bundle = intent.extras
        val myData = bundle.getSerializable("remoteNotification") as NotificationModel


        tv_title.text = myData.title
        tv_content.text = myData.message

        setListner()

    }

    private fun setListner() {
        iv_goback.setOnClickListener(this)
    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.iv_goback -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!MainActivity2.active) {
            startActivity(Intent(this@NotificationActivity, SplashScreen::class.java))
        }
        finish()
    }


}

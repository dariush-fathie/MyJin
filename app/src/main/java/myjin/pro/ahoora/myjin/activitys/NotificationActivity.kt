package myjin.pro.ahoora.myjin.activitys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import myjin.pro.ahoora.myjin.R
import co.ronash.pushe.Pushe



class NotificationActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)


    }

    private fun finishAct() {
        finish()
    }
}

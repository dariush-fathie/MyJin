package myjin.pro.ahoora.myjin.activitys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_notification.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.NotificationModel
import org.json.JSONObject

class NotificationActivity : AppCompatActivity() {


    var html = "<html>" +
            "<head>" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"> " +
            "   <meta http-equiv=\"Content-Language\" content=\"fa\"> " +
            "   <style type=\"text/css\"> " +
            "   h3{    color: #208b1c;        }" +
            "   " +
            " </style>" +
            " </head>" +
            " " +
            " <body dir='rtl'><h3 > <center> "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        wv_notificationData.settings.javaScriptEnabled = true
        wv_notificationData.settings.builtInZoomControls = true


        if (intent != null) {
            try {
                Log.e("data data", intent.getStringExtra("data"))

                val notificationModel = intent.getParcelableExtra<NotificationModel>("data1")
                Log.e("payload", notificationModel.custom)

                html += notificationModel.title + "</center></h3><h4>"
                html += notificationModel.alert + "</h4>"

                val custom = JSONObject(notificationModel.custom)
                val customPayload = custom.getJSONObject("a")

                html += "<h4>"

                for (i in 1..customPayload.length()) {
                    Log.e("key$i", customPayload.optString("key$i"))
                    html += customPayload.optString("key$i") + "</h4><h4>"
                }

                html += "</h4></body></html>"

                wv_notificationData.loadData(html, "text/html; charset=utf-8", "UTF-8")
            } catch (e: Exception) {
                html += "</body></html> "
                wv_notificationData.loadData(html, "text/html; charset=utf-8", "UTF-8")
            }

            Log.e("html",html)
        }
    }

    private fun finishAct() {
        finish()
    }
}

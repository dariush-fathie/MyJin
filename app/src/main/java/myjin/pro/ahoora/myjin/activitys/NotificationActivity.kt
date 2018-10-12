package myjin.pro.ahoora.myjin.activitys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_services.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.NotificationAdapter
import myjin.pro.ahoora.myjin.models.KotlinNotificationModel
import myjin.pro.ahoora.myjin.utils.DateConverter


class NotificationActivity : AppCompatActivity(), View.OnClickListener {
    private var converter: DateConverter? = null
    private var buffer = ArrayList<KotlinNotificationModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

        tv_ServicesTitle.text = getString(R.string.notification)
        converter = DateConverter(this@NotificationActivity)
        try {
            val realm = Realm.getDefaultInstance()
            /*  val bundle = intent.extras
              val myData = bundle.getSerializable("remoteNotification") as KotlinNotificationModel*/

            realm.executeTransaction { db ->

                var res = db.where(KotlinNotificationModel::class.java).findAll()!!
                res = res.sort("messageId", Sort.DESCENDING)


                rv_services.layoutManager = LinearLayoutManager(this@NotificationActivity, LinearLayoutManager.VERTICAL, false)


                rv_services.adapter = NotificationAdapter(this@NotificationActivity, res, realm)


            }
        } catch (e: Exception) {

        }
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
        /* if (!MainActivity2.active) {
             startActivity(Intent(this@NotificationActivity, SplashScreen::class.java))
         }*/
        finish()
    }


}

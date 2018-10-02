package myjin.pro.ahoora.myjin.activitys

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_notification.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.NotificationModel


class NotificationActivity : AppCompatActivity(), View.OnClickListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)


        val bundle = intent.extras
        val myData = bundle.getSerializable("remoteNotification") as NotificationModel

        Toast.makeText(this@NotificationActivity,myData.title+"  "+myData.message ,Toast.LENGTH_SHORT).show()

        setListner()

    }

    private fun setListner() {
        iv_goback.setOnClickListener(this)
    }



    override fun onClick(v: View?) {

        when(v?.id){
            R.id.iv_goback->{
                finish()
            }
        }
    }
}

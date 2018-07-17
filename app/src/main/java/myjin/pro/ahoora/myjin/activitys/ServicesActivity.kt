package myjin.pro.ahoora.myjin.activitys

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_services.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.ServicesAdapter

class ServicesActivity : AppCompatActivity(), View.OnClickListener {

    var groupId=1

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_goback->onBackPressed()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)
        if (intent != null) {
            tv_ServicesTitle.text=intent.getStringExtra("ServiceTitle")
            groupId=intent.getIntExtra("groupId",1)
        }
        onClick()
        getServicesTitle()
    }

    private fun onClick() {
        iv_goback.setOnClickListener(this)
    }

    private fun getServicesTitle(){
        rv_services.layoutManager = LinearLayoutManager(this@ServicesActivity, LinearLayoutManager.VERTICAL, false)
        rv_services.adapter =ServicesAdapter(this@ServicesActivity,groupId)
    }
}

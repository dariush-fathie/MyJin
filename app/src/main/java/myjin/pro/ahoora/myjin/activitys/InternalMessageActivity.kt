package myjin.pro.ahoora.myjin.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_services.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.InternalMessageAdapter

class InternalMessageActivity : AppCompatActivity(), View.OnClickListener {



    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_goback -> onBackPressed()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

        onClick()
        getList()

        tv_ServicesTitle.text=getString(R.string.phdzhm)
    }

    private fun onClick() {
        iv_goback.setOnClickListener(this)
    }

    private fun getList() {

        rv_services.layoutManager = LinearLayoutManager(this)
        rv_services.adapter = InternalMessageAdapter(this)
    }



}

package myjin.pro.ahoora.myjin.activitys

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_rules.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.KotlinAboutContactModel

class RulesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        val html: String
        val id = 1

        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val res = realm.where(KotlinAboutContactModel::class.java).equalTo("id", id).findFirst()!!
        html = res.tContact.toString()
        realm.commitTransaction()

        wv_rules_us.settings.javaScriptEnabled = true
        wv_rules_us.settings.builtInZoomControls = true
        wv_rules_us.loadData(html, "text/html; charset=utf-8", "UTF-8")

        iv_goback.setOnClickListener({
            finish()
        })
    }
}

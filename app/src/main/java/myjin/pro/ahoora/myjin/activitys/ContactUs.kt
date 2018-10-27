package myjin.pro.ahoora.myjin.activitys;

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_contact_us.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.KotlinAboutContactModel

class ContactUs : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        val html: String
        val id = 1

        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val res = realm.where(KotlinAboutContactModel::class.java).equalTo("id", id).findFirst()!!
        html = res.tContact.toString()
        realm.commitTransaction()

        wv_contactus.settings.javaScriptEnabled = true
        wv_contactus.settings.builtInZoomControls = true
        wv_contactus.loadData(html, "text/html; charset=utf-8", "UTF-8")

        iv_goback.setOnClickListener({
            finish()
        })
    }
}

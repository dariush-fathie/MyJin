package myjin.pro.ahoora.myjin.activitys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.CompoundButton
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_setting.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.KotlinItemModel
import myjin.pro.ahoora.myjin.models.KotlinMessagesModel
import myjin.pro.ahoora.myjin.models.events.DeleteFavEvent
import myjin.pro.ahoora.myjin.utils.SharedPer
import org.greenrobot.eventbus.EventBus

class SettingActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    override fun onClick(v: View?) {
        onBackPressed()
    }

    val realmDatabase = Realm.getDefaultInstance()
    val isSavedC = false
    val isSavedM = false

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.sc_intro -> {
                SharedPer(this).setIntro(getString(R.string.introductionFlag2), isChecked)
                SharedPer(this).setBoolean(getString(R.string.introductionFlag), isChecked)
            }
            R.id.rb_centers -> {
                SharedPer(this).setDefTab(getString(R.string.defTab), isChecked)
            }

            R.id.sc_centers -> {
                if (!isChecked) {
                    sc_centers.isEnabled = false
                    deleteSaved(true)
                }
            }
            R.id.sc_message -> {
                if (!isChecked) {
                    sc_message.isEnabled = false
                    deleteSaved(false)
                }
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        onClick_()
        init_()
    }

    private fun init_() {
        sc_intro.isChecked = SharedPer(this).getIntro(getString(R.string.introductionFlag2))

        if (SharedPer(this).getDefTab(getString(R.string.defTab)))
            rb_centers.isChecked = SharedPer(this).getDefTab(getString(R.string.defTab))
        else
            rb_messages.isChecked = !SharedPer(this).getDefTab(getString(R.string.defTab))

        getFromRealm()
    }

    private fun onClick_() {
        sc_centers.setOnCheckedChangeListener(this)
        sc_message.setOnCheckedChangeListener(this)
        sc_intro.setOnCheckedChangeListener(this)
        rb_centers.setOnCheckedChangeListener(this)
        iv_goback.setOnClickListener(this)
    }

    private fun getFromRealm() {
        realmDatabase.executeTransaction { db ->

            val res1 = db.where(KotlinItemModel::class.java).equalTo("saved", true).findFirst()
            val res2 = db.where(KotlinMessagesModel::class.java).equalTo("saved", true).findFirst()

            if (res1 != null) {
                sc_centers.isChecked = true
                sc_centers.isEnabled = true
            }
            if (res2 != null) {
                sc_message.isChecked = true
                sc_message.isEnabled = true
            }

        }
    }




    private fun deleteSaved(tf: Boolean) {

        realmDatabase.executeTransaction { db ->

            if (tf) {
                val item = db.where(KotlinItemModel::class.java)
                        .findAll()!!
                item.forEach { ii ->
                    ii.saved = false
                }

            } else {
                val item = db.where(KotlinMessagesModel::class.java)
                        .findAll()!!
                item.forEach { ii ->
                    ii.saved = false
                }
                EventBus.getDefault().post(DeleteFavEvent())
            }

        }
    }
}

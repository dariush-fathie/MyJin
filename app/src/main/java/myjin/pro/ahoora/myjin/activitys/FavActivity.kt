package myjin.pro.ahoora.myjin.activitys

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PorterDuff
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_fav.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.GroupItemSaveAdapter
import myjin.pro.ahoora.myjin.customClasses.SimpleItemDecoration
import myjin.pro.ahoora.myjin.models.KotlinGroupModel
import myjin.pro.ahoora.myjin.models.KotlinItemModel


class FavActivity : AppCompatActivity(), View.OnClickListener, TabLayout.OnTabSelectedListener, View.OnLongClickListener {

    val tabListId = ArrayList<Int>()
    var Receiver: BroadcastReceiver? = null
    var centersCleanFlag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)
        addTabs()
        startReceive()
    }

    private fun addTabs() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()

        val result = realm.where(KotlinItemModel::class.java)
                .equalTo("saved", true)
                .findAll()

        val groups = realm.where(KotlinItemModel::class.java)
                .equalTo("saved", true)
                .distinct("groupId")
                .findAll()


        realm.commitTransaction()

        tabListId.clear()
        if (result.size > 0) {
            val drawable = ContextCompat.getDrawable(this@FavActivity, R.drawable.ic_all)
            drawable?.setColorFilter(ContextCompat.getColor(this@FavActivity, R.color.green), PorterDuff.Mode.SRC_IN)
            ctb.addTab(ctb.newTab().setText("همه").setIcon(drawable))
            tabListId.add(0)
            groups.forEach { savedItem: KotlinItemModel? ->
                tabListId.add(savedItem?.groupId!!)

                ctb.addTab(ctb.newTab().setText(getTitleFromDb(savedItem.groupId)))


            }

        } else {
            tv_empty.visibility = View.VISIBLE
        }

        if (result.size > 0) {
            filter(0)
        }
        ctb.addOnTabSelectedListener(this)
        iv_goback.setOnClickListener(this)
        // iv_menu.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {

        when (p0?.getId()) {
            R.id.tv_login_outsign -> Toast.makeText(this@FavActivity, "این قسمت در نسخه جدید ارائه شده است", Toast.LENGTH_LONG).show()

            R.id.iv_menu -> openDrawerLayout()
            R.id.iv_goback -> onBackPressed()

            R.id.tv_healthCenters -> drawerClick(1)
            R.id.rl_drawer3 -> drawerClick(2)
            R.id.rl_drawer4 -> drawerClick(3)
            R.id.rl_takapoo_services -> goToServicesActivity(getString(R.string.mnvfs), 2)
            R.id.rl_setting -> startActivity(Intent(this, SettingActivity::class.java))
        }
    }

    private fun goToServicesActivity(title: String, i: Int) {
        val intentF = Intent(this@FavActivity, ServicesActivity::class.java)
        intentF.putExtra("ServiceTitle", title)
        intentF.putExtra("groupId", i)
        startActivity(intentF)

    }

    override fun onBackPressed() {
        if (centersCleanFlag) {
            val resIntent = Intent()

            resIntent.putExtra(getString(R.string.centersClean), centersCleanFlag)
            setResult(Activity.RESULT_OK, resIntent)
        }
        super.onBackPressed()
    }

    override fun onLongClick(v: View?): Boolean {
        when (v?.id) {
            R.id.iv_jinDrawer -> {
                startActivity(Intent(this@FavActivity, LoginActivity::class.java))

                finish()
                return true
            }
        }
        return false
    }

    private fun drawerClick(position: Int) {
        when (position) {

            1 -> closeDrawerLayout()
            2 -> {
                startActivity(Intent(this@FavActivity, AboutUs::class.java))

            }
            3 -> {
                startActivity(Intent(this@FavActivity, ContactUs::class.java))

            }
        }


    }


    private fun openDrawerLayout() {
        drawerLayout.openDrawer(GravityCompat.END)
    }

    private fun closeDrawerLayout() {
        drawerLayout.closeDrawers()
    }


    private fun getTitleFromDb(groupId: Int): String {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val name = realm.where(KotlinGroupModel::class.java)
                .equalTo("groupId", groupId)
                .findFirst()?.name
        realm.commitTransaction()
        return name!!
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        // tab?.icon?.setColorFilter(ContextCompat.getColor(this@FavActivity, R.color.title), PorterDuff.Mode.SRC_IN)
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        //  tab?.icon?.setColorFilter(ContextCompat.getColor(this@FavActivity, R.color.green), PorterDuff.Mode.SRC_IN)

        Log.e("tab", "${tab?.position} +  s")
        if (tab?.position == 0) {
            filter(0)
        } else {
            val pos = tab?.position
            filter(tabListId.get(pos!!))
        }
    }


    fun filter(id: Int) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val result: RealmResults<KotlinItemModel>
        if (id != 0) {

            result = realm.where(KotlinItemModel::class.java)
                    .equalTo("saved", true)
                    .equalTo("groupId", id).findAll()

        } else {

            result = realm.where(KotlinItemModel::class.java)
                    .equalTo("saved", true).findAll()

        }
        realm.commitTransaction()
        val list = ArrayList<Int>()
        result.forEach { item: KotlinItemModel ->
            list.add(item.centerId)
        }
        initList(list)
    }

    private fun initList(list: ArrayList<Int>) {


        Log.e("size", "${list.size}  l")
        rv_items_marked.layoutManager = LinearLayoutManager(this)
        rv_items_marked.adapter = GroupItemSaveAdapter(this, list)
        while (rv_items_marked.itemDecorationCount > 0) {
            rv_items_marked.removeItemDecorationAt(0)
        }
        rv_items_marked.addItemDecoration(SimpleItemDecoration(this, 8))
    }

    override fun onStart() {

        super.onStart()
        registerReceiver(Receiver, IntentFilter("myjin.pro.ahoora.myjin"))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(Receiver)
    }

    private fun startReceive() {
        if (Receiver == null) {

            Receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {

                    ctb?.removeAllTabs()
                    addTabs()


                }
            }
        }
    }
}

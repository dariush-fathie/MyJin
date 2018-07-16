package myjin.pro.ahoora.myjin.activitys

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_fav.*
import kotlinx.android.synthetic.main.drawer_layout.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.GroupItemSaveAdapter
import myjin.pro.ahoora.myjin.customClasses.SimpleItemDecoration
import myjin.pro.ahoora.myjin.models.KotlinGroupModel
import myjin.pro.ahoora.myjin.models.KotlinItemModel
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter


class FavActivity : AppCompatActivity(), View.OnClickListener, TabLayout.OnTabSelectedListener, View.OnLongClickListener {

    val tabListId = ArrayList<Int>()
    var Receiver: BroadcastReceiver? = null

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
        iv_menu.setOnClickListener(this)
        rl_myjin_services.setOnClickListener(this)
        rl_drawer2.setOnClickListener(this)
        rl_drawer3.setOnClickListener(this)
        rl_drawer4.setOnClickListener(this)
        rl_salamat.setOnClickListener(this)
        iv_jinDrawer.setOnLongClickListener(this)
        rl_myjin_services.setOnClickListener(this)
        rl_takapoo_services.setOnClickListener(this)
        rl_university_services.setOnClickListener(this)
        rl_tamin_services.setOnClickListener(this)
        rl_ict_services.setOnClickListener(this)
        rl_pishkhan_services .setOnClickListener(this)
        rl_post_services.setOnClickListener(this)
        rl_salamat .setOnClickListener(this)

    }
    override fun onClick(p0: View?) {

        when (p0?.getId()) {
            R.id.iv_menu -> openDrawerLayout()
            R.id.iv_goback -> finish()

            R.id.rl_myjin_services -> goToServicesActivity(tv_myjin_services_Title1.text.toString())
            R.id.rl_takapoo_services->goToServicesActivity(getString(R.string.takapoo))
            R.id.rl_university_services->goToServicesActivity(tv_university_services_Title1.text.toString())
            R.id.rl_tamin_services -> early_Mth()
            R.id.rl_ict_services->early_Mth()
            R.id.rl_pishkhan_services -> early_Mth()
            R.id.rl_post_services->early_Mth()
            R.id.rl_salamat -> startActivity(Intent(this@FavActivity,HeaIncServiceActivity::class.java))


            R.id.rl_drawer3 -> drawerClick(2)
            R.id.rl_drawer4 -> drawerClick(3)
        }
    }
    private fun goToServicesActivity(title:String){
        val intent=Intent(this@FavActivity, ServicesActivity::class.java)
        intent.putExtra("ServiceTitle",title)
        startActivity(intent)

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

            2 -> {
                startActivity(Intent(this@FavActivity, AboutUs::class.java))

            }
            3 -> {
                startActivity(Intent(this@FavActivity, ContactUs::class.java))

            }
        }


    }

    fun early_Mth() {
        Toast.makeText(this, "بزودی", Toast.LENGTH_LONG).show()
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

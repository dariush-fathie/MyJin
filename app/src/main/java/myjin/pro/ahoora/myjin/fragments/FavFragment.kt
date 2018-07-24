package myjin.pro.ahoora.myjin.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_fav.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.GroupItemSaveAdapter
import myjin.pro.ahoora.myjin.customClasses.SimpleItemDecoration
import myjin.pro.ahoora.myjin.models.KotlinGroupModel
import myjin.pro.ahoora.myjin.models.KotlinItemModel
import myjin.pro.ahoora.myjin.models.events.VisibilityEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class FavFragment : Fragment(), TabLayout.OnTabSelectedListener {


    val tabListId = ArrayList<Int>()
    var Receiver: BroadcastReceiver? = null

    private var firstLoad = false
    private var updated = false

    @Subscribe
    fun onBecomeVisible(e: VisibilityEvent) {
        if (e.position == 2) {
            Log.e(MessagesFragment::class.java.simpleName, "${e.position}")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_empty.visibility = View.GONE
        addTabs()
        startReceive()
    }

    override fun onResume() {
        super.onResume()
        tv_empty.visibility = View.GONE
        ctb?.removeAllTabs()
        addTabs()
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
            v1.visibility = View.VISIBLE
            ctb.visibility = View.VISIBLE
            val drawable = ContextCompat.getDrawable(activity as Context, R.drawable.ic_all)
            drawable?.setColorFilter(ContextCompat.getColor(activity as Context, R.color.green), PorterDuff.Mode.SRC_IN)
            ctb.addTab(ctb.newTab().setText("همه").setIcon(drawable))
            tabListId.add(0)
            groups.forEach { savedItem: KotlinItemModel? ->
                tabListId.add(savedItem?.groupId!!)
                ctb.addTab(ctb.newTab().setText(getTitleFromDb(savedItem.groupId)))
            }

        } else {
            v1.visibility = View.GONE
            ctb.visibility = View.GONE
            tv_empty.visibility = View.VISIBLE
        }

        if (result.size > 0) {
            filter(0)
        }
        ctb.addOnTabSelectedListener(this)
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
        rv_items_marked.layoutManager = LinearLayoutManager(activity as Context)
        rv_items_marked.adapter = GroupItemSaveAdapter(activity as Context, list)
        while (rv_items_marked.itemDecorationCount > 0) {
            rv_items_marked.removeItemDecorationAt(0)
        }
        rv_items_marked.addItemDecoration(SimpleItemDecoration(activity as Context, 8))
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        (activity as Context).registerReceiver(Receiver, IntentFilter("myjin.pro.ahoora.myjin"))
    }

    override fun onStop() {
        (activity as Context).unregisterReceiver(Receiver)
        EventBus.getDefault().unregister(this)
        super.onStop()
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
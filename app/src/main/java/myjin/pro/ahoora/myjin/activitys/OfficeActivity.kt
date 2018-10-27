package myjin.pro.ahoora.myjin.activitys

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.*
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_office.*
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.drawer_layout.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.GroupItemAdapter
import myjin.pro.ahoora.myjin.adapters.TAdapter
import myjin.pro.ahoora.myjin.customClasses.CustomBottomSheetBehavior
import myjin.pro.ahoora.myjin.customClasses.SimpleItemDecoration
import myjin.pro.ahoora.myjin.customClasses.TabLayoutInterface
import myjin.pro.ahoora.myjin.models.KotlinGroupModel
import myjin.pro.ahoora.myjin.models.KotlinItemModel
import myjin.pro.ahoora.myjin.models.events.NearestEvent
import myjin.pro.ahoora.myjin.models.events.NearestEvent2
import myjin.pro.ahoora.myjin.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OfficeActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {


    var groupId = 0
    var provId = 19
    var cityId = 1
    var g_url = ""
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback
    private lateinit var tabLayoutInterface: TabLayoutInterface
    private lateinit var adapter: GroupItemAdapter
    var idArray = ArrayList<Int>()
    var filterArray = ArrayList<Int>()
    private val requestLocationSetting = 1055

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_office)

        Handler().postDelayed({
            initListener()
            if (intent != null) {
                groupId = intent.getIntExtra(StaticValues.CATEGORY, 0)
                provId = intent.getIntExtra(StaticValues.PROVID, 19)
                cityId = intent.getIntExtra(StaticValues.CITYID, 1)
            }
            if (groupId != 1) {
                hideFilter()
            }
            tv_officeTitle.text = getTitleFromDb()
            initBottomSheet()
            tabLayoutInterface = TabLayoutInterface(this, supportFragmentManager, bottomSheetBehavior, ll_progress)
            ctbl.addOnTabSelectedListener(tabLayoutInterface)

            if (NetworkUtil().isNetworkAvailable(this)) {
                getItems()
            } else {
                showErrLayout()
            }
        }, 50)

    }

    private fun showSomeViews() {
        view_shadow.visibility = View.VISIBLE
        cl_bottomSheetLayout.visibility = View.VISIBLE
        ll_t.visibility = View.VISIBLE
    }

    private fun hideSomeViews() {
        view_shadow.visibility = View.GONE
        cl_bottomSheetLayout.visibility = View.GONE
        ll_t.visibility = View.GONE
    }

    private fun showErrLayout() {
        hideMainProgressLayout()
        hideSomeViews()
        btn_tryAgain.visibility = View.VISIBLE
        tv_netErrText.visibility = View.VISIBLE
    }

    private fun hideErrLayout() {
        btn_tryAgain.visibility = View.GONE
        tv_netErrText.visibility = View.GONE
    }

    private fun tryAgain() {
        hideErrLayout()
        if (NetworkUtil().isNetworkAvailable(this)) {
            getItems()
        } else {
            showErrLayout()
        }
    }

    private fun hideFilter() {
        view_divider.visibility = View.GONE
        rl_filter.visibility = View.GONE
    }

    //`center_id` IN(682,681,680,679,678,677,676,675,674)
    private fun getTitleFromDb(): String {
        Log.e("gid", "$groupId")
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val name = realm.where(KotlinGroupModel::class.java).equalTo("groupId", groupId).findFirst()?.name
        g_url = realm.where(KotlinGroupModel::class.java).equalTo("groupId", groupId).findFirst()?.g_url!!
        realm.commitTransaction()
        return name!!
    }

    private fun initListener() {
        btn_tryAgain.setOnClickListener(this)
        rl_filter.setOnClickListener(this)
        rl_sort.setOnClickListener(this)
        iv_goback.setOnClickListener(this)
        fab_goUp.setOnClickListener(this)
        iv_menu.setOnClickListener(this)
        rl_drawer3.setOnClickListener(this)
        rl_drawer4.setOnClickListener(this)
        rl_salamat.setOnClickListener(this)
        rl_myjin_services.setOnClickListener(this)
        iv_jinDrawer.setOnLongClickListener(this)
        rl_myjin_services.setOnClickListener(this)
        rl_takapoo_services.setOnClickListener(this)
        rl_university_services.setOnClickListener(this)
        rl_tamin_services.setOnClickListener(this)
        rl_ict_services.setOnClickListener(this)
        rl_pishkhan_services.setOnClickListener(this)
        rl_post_services.setOnClickListener(this)
        rl_salamat.setOnClickListener(this)
        tv_login_outsign.setOnClickListener(this)
        rl_setting.setOnClickListener(this)
        tv_healthCenters.setOnClickListener(this)
        tv_messages.setOnClickListener(this)
        rl_rules.setOnClickListener(this)
        rl_exit.setOnClickListener(this)
        rl_notifi.setOnClickListener(this)
    }

    private fun initList(list: ArrayList<Int>) {
        rv_items.layoutManager = LinearLayoutManager(this)
        while (rv_items.itemDecorationCount > 0) {
            rv_items.removeItemDecorationAt(0)
        }
        rv_items.clearOnScrollListeners()
        rv_items.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val p = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                flagScrollGoesDown = p > 8
                controlFabVisibility()
            }
        })
        val decor = SimpleItemDecoration(this, 6)
        rv_items.addItemDecoration(decor)
        adapter = GroupItemAdapter(this, list, g_url, getTitleFromDb())
        rv_items.adapter = adapter
        Log.e("itemSize", "${list.size}")
        hideMainProgressLayout()
    }

    private fun controlFabVisibility() {
        if (bottomSheetExpanded) {
            (fab_goUp as View).visibility = View.GONE
        } else {
            if (flagScrollGoesDown) {
                (fab_goUp as View).visibility = View.VISIBLE
            } else {
                (fab_goUp as View).visibility = View.GONE
            }
        }
    }

    fun hideMainProgressLayout() {
        ll_progressMain.visibility = View.GONE
    }

    fun showMainProgressLayout() {
        ll_progressMain.visibility = View.VISIBLE
    }

    private fun getItems() {
        if (Utils.isNetworkAvailable(this)) {
            hideErrLayout()
            showMainProgressLayout()
            val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
            apiInterface.getItems(groupId, provId, cityId).enqueue(object : Callback<List<KotlinItemModel>> {
                override fun onResponse(call: Call<List<KotlinItemModel>>?, response: Response<List<KotlinItemModel>>?) {
                    val list = response?.body()
                    Log.e("success", list.toString() + " res")

                    val realmDatabase = Realm.getDefaultInstance()
                    realmDatabase.executeTransactionAsync { realm: Realm? ->

                        val savedItem = realm?.where(KotlinItemModel::class.java)
                                ?.equalTo("saved", true)
                                ?.equalTo("groupId", groupId)
                                ?.findAll()
                        val savedItemIds = ArrayList<Int>()
                        savedItem?.forEach { model: KotlinItemModel? ->
                            savedItemIds.add(model?.centerId!!)
                        }

                        realm?.where(KotlinItemModel::class.java)
                                ?.equalTo("saved", false)
                                ?.equalTo("groupId", groupId)
                                ?.findAll()
                                ?.deleteAllFromRealm()

                        list?.forEach { kotlinItemModel: KotlinItemModel ->
                            if (savedItemIds.contains(kotlinItemModel.centerId)) {
                                kotlinItemModel.saved = true
                            }
                            realm?.copyToRealmOrUpdate(kotlinItemModel)
                        }

                        val query = realm?.where(KotlinItemModel::class.java)
                                ?.equalTo("groupId", groupId)

                        if (cityId != 0) {
                            query?.equalTo("addressList.cityId", cityId)
                        }
                        query?.equalTo("addressList.provId", provId)
                        query?.sort("firstName", Sort.ASCENDING)

                        val result1 = query?.findAll()

                        idArray.clear()
                        result1?.forEach { itemModel: KotlinItemModel? ->
                            idArray.add(itemModel?.centerId!!)
                        }
                        runOnUiThread {
                            initList(idArray)
                            hideErrLayout()
                            hideMainProgressLayout()
                            showSomeViews()
                        }
                    }
                }

                override fun onFailure(call: Call<List<KotlinItemModel>>?, t: Throwable?) {
                    Log.e("ERR", t?.message + "  ")
                    showErrLayout()
                    hideMainProgressLayout()
                }
            })
        } else {
            showErrLayout()
        }
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(cl_bottomSheetLayout)
        if (bottomSheetBehavior is CustomBottomSheetBehavior) {
            (bottomSheetBehavior as CustomBottomSheetBehavior).setAllowUserDragging(false)
        }
        bottomSheetBehavior.setBottomSheetCallback(getBottomSheetCallback())

    }

    var bottomSheetExpanded = false
    var flagScrollGoesDown = false

    private fun getBottomSheetCallback(): BottomSheetBehavior.BottomSheetCallback {
        val a = Utils.pxFromDp(this, 80f)
        bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.e("slide", "$slideOffset")
                ctbl.translationY = -slideOffset * a
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                bottomSheetExpanded = when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> true
                    BottomSheetBehavior.STATE_COLLAPSED -> false
                    else -> {
                        true
                    }
                }

                controlFabVisibility()
            }
        }
        return bottomSheetCallback
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun sort(sort: Sort) {
        // todo add filters
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        var result: RealmResults<KotlinItemModel>
        if (filterFlag) {
            result = realm.where(KotlinItemModel::class.java)
                    .equalTo("groupId", groupId)
                    .`in`("specialityList.specialtyId", filterArray.toTypedArray()).findAll()
            result = result.sort("firstName", sort)
        } else {
            result = realm.where(KotlinItemModel::class.java)
                    .equalTo("groupId", groupId).findAll()
            result = result.sort("firstName", sort)
        }

        realm.commitTransaction()

        idArray.clear()

        result.forEach { item: KotlinItemModel ->
            idArray.add(item.centerId)
        }

        initList(idArray)
    }

    private fun filter1(array: ArrayList<Int>) {
        filterFlag = true
        tv_filter.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val result = realm.where(KotlinItemModel::class.java)
                .equalTo("groupId", groupId)
                .`in`("specialityList.specialtyId", array.toTypedArray())
                .sort("firstName", Sort.ASCENDING)
                .findAll()
        realm.commitTransaction()
        idArray.clear()
        result.forEach { item: KotlinItemModel ->

            idArray.add(item.centerId)
        }

        initList(idArray)
    }

    private var filterFlag = false
    private var ascSort = true

    private fun clearFilter() {
        filterFlag = false
        filterArray.clear()
        tv_filter.setTextColor(ContextCompat.getColor(this, R.color.androidTextColor))
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val result: RealmResults<KotlinItemModel>
        result = realm.where(KotlinItemModel::class.java)
                .equalTo("groupId", groupId)
                .sort("firstName", Sort.ASCENDING)
                .findAll()
        realm.commitTransaction()
        idArray.clear()
        result.forEach { item: KotlinItemModel ->
            idArray.add(item.centerId)
        }

        initList(idArray)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_tryAgain -> tryAgain()
            R.id.tv_login_outsign -> {
                Toast.makeText(this@OfficeActivity,
                        getString(R.string.early), Toast.LENGTH_SHORT).show()
                //startActivity(Intent(this, Login2Activity::class.java))
            }
            R.id.rl_filter -> onFilterClick()
            R.id.rl_sort -> onSortClick()
            R.id.iv_goback -> onBackPressed()
            R.id.iv_menu -> openDrawerLayout()
            R.id.fab_goUp -> rv_items.smoothScrollToPosition(0)
            R.id.rl_exit -> showExitDialog()
            R.id.tv_healthCenters -> drawerClick(1)
            R.id.rl_drawer3 -> drawerClick(2)
            R.id.rl_drawer4 -> drawerClick(3)

            R.id.rl_myjin_services -> goToServicesActivity(getString(R.string.khj), 1)
            R.id.rl_takapoo_services -> goToServicesActivity(getString(R.string.mnvfs), 2)
            R.id.rl_university_services -> goToServicesActivity(tv_university_services_Title1.text.toString(), 3)
            R.id.rl_salamat -> goToServicesActivity(tv_drawerTitlesalamat.text.toString(), 4)
            R.id.rl_tamin_services -> goToServicesActivity(tv_tamin_services.text.toString(), 5)
            R.id.rl_ict_services -> goToServicesActivity(tv_ict_services.text.toString(), 6)
            R.id.rl_pishkhan_services -> goToServicesActivity(tv_pishkhan_services.text.toString(), 7)
            R.id.rl_post_services -> goToServicesActivity(tv_post_services.text.toString(), 8)
            R.id.rl_setting -> startActivityForResult(Intent(this, SettingActivity::class.java), settingRequest)
            R.id.tv_messages -> startActivity(Intent(this, FavMessageActivity::class.java))
            R.id.rl_notifi -> startActivity(Intent(this, NotificationActivity::class.java))
            R.id.rl_rules -> startActivity(Intent(this, RulesActivity::class.java))

        }
    }

    private fun goToServicesActivity(title: String, i: Int) {
        val intentO = Intent(this@OfficeActivity, ServicesActivity::class.java)
        intentO.putExtra("ServiceTitle", title)
        intentO.putExtra("groupId", i)
        startActivity(intentO)
    }

    override fun onLongClick(v: View?): Boolean {
        when (v?.id) {
            R.id.iv_jinDrawer -> {
                startActivity(Intent(this@OfficeActivity, LoginActivity::class.java))

                finish()
                return true
            }
        }
        return false
    }

    private fun drawerClick(position: Int) {

        when (position) {

            1 -> {
                startActivityForResult(Intent(this@OfficeActivity, FavActivity::class.java), settingRequest)

            }
            2 -> {
                startActivity(Intent(this@OfficeActivity, AboutUs::class.java))

            }
            3 -> {
                startActivity(Intent(this@OfficeActivity, ContactUs::class.java))

            }

        }


    }


    private fun openDrawerLayout() {
        drawerLayout.openDrawer(GravityCompat.END)
    }

    private fun closeDrawerLayout() {
        drawerLayout.closeDrawers()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createRevealAnimation() {
        val startRadius = 0
        val endRadius = 0
        Math.hypot(rv_items.width.toDouble(), rv_items.height.toDouble()).toInt()
        val anim = ViewAnimationUtils.createCircularReveal(rv_items, 0, 0, 0f, endRadius.toFloat())
        anim.duration = 500
        anim.interpolator = AccelerateInterpolator()
        anim.start()
    }

    inner class RtlGridLayoutManager : GridLayoutManager {

        constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

        constructor(context: Context, spanCount: Int) : super(context, spanCount) {}

        constructor(context: Context, spanCount: Int, orientation: Int, reverseLayout: Boolean) : super(context, spanCount, orientation, reverseLayout) {}

        override fun isLayoutRTL(): Boolean {
            return true
        }
    }

    private fun onFilterClick() {
        val builder = AlertDialog.Builder(this@OfficeActivity)
        val dialog: AlertDialog
        val view = View.inflate(this@OfficeActivity, R.layout.filter, null)

        val tvAction: AppCompatTextView = view.findViewById(R.id.tv_action)
        val tvClose: AppCompatTextView = view.findViewById(R.id.tv_close)
        val tvClearSlecteds: AppCompatTextView = view.findViewById(R.id.tv_clearSlecteds)
        val tList: RecyclerView = view.findViewById(R.id.rv_tList)

        val adapter = TAdapter(this@OfficeActivity, filterArray)
        tList.layoutManager = RtlGridLayoutManager(this@OfficeActivity, 3)
        tList.adapter = adapter


        builder.setView(view)
        dialog = builder.create()
        dialog.show()

        val listener = View.OnClickListener { v ->
            when (v.id) {
                R.id.tv_action -> {
                    dialog.dismiss()
                    if (adapter.idsArray.size > 0) {
                        filterArray = adapter.idsArray
                        Log.e("size", "${filterArray.size}")
                        filter1(filterArray)
                    } else {
                        clearFilter()
                    }
                }
                R.id.tv_close -> {
                    dialog.dismiss()
                }
                R.id.tv_clearSlecteds -> {
                    clearFilter()
                    adapter.clearSelections()
                }
            }
        }
        tvAction.setOnClickListener(listener)
        tvClose.setOnClickListener(listener)
        tvClearSlecteds.setOnClickListener(listener)
    }

    private fun onSortClick() {
        val builder = AlertDialog.Builder(this@OfficeActivity)
        val dialog: AlertDialog
        val view = View.inflate(this@OfficeActivity, R.layout.sort, null)

        val tvAction: AppCompatTextView = view.findViewById(R.id.tv_action)
        val tvClose: AppCompatTextView = view.findViewById(R.id.tv_close)
        val rb1: AppCompatRadioButton = view.findViewById(R.id.rb1)
        val rb2: AppCompatRadioButton = view.findViewById(R.id.rb2)

        rb1.isClickable = false
        rb2.isClickable = false

        if (ascSort) {
            rb1.isChecked = true
        } else {
            rb2.isChecked = true
        }


        builder.setView(view)
        dialog = builder.create()
        dialog.show()

        val listener = View.OnClickListener { v ->
            when (v.id) {
                R.id.tv_action -> {
                    dialog.dismiss()
                    if (rb1.isChecked) {
                        sort(Sort.ASCENDING)
                        ascSort = true
                    } else {
                        sort(Sort.DESCENDING)
                        ascSort = false
                    }
                }
                R.id.tv_close -> {
                    dialog.dismiss()
                }
                R.id.rb1 -> {

                }
                R.id.rb2 -> {

                }
            }
        }
        tvAction.setOnClickListener(listener)
        tvClose.setOnClickListener(listener)
        rb1.setOnClickListener(listener)
        rb2.setOnClickListener(listener)
    }




    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(e: String) {
        tabLayoutInterface.hideProgress()
        Log.e("dfs ", e)
    }

    override fun onBackPressed() {

        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("Office", "onResult")
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == StaticValues.requestCodeOfficeDetail) {
                val id = data?.getIntExtra("centerId", -1)
                val savedOrDelete = data?.getBooleanExtra("save", false)
                adapter.onItemMarkedEvent(id!!, savedOrDelete!!)
            }

            if (requestCode == settingRequest) {
                if (data?.getBooleanExtra(getString(R.string.centersClean), false)!!) {
                    notifyAdapter()
                }
            }
            if (requestCode == requestLocationSetting) {
                Log.e("resultCode", "$resultCode")
                EventBus.getDefault().post(NearestEvent(0))
            }

        }

    }

    private val requestPermission = 1054
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == requestPermission) {
            if (permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("granted", "success")
                    EventBus.getDefault().post(NearestEvent2(0))
                } else {

                }
            }
        }
    }

    private fun notifyAdapter() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val query = realm.where(KotlinItemModel::class.java)
                .equalTo("groupId", groupId)
        if (cityId != 0) {
            query?.equalTo("addressList.cityId", cityId)
        }
        query?.equalTo("addressList.provId", provId)
        if (filterFlag) {
            query.`in`("specialityList.specialtyId", filterArray.toTypedArray())
        }
        if (ascSort) {
            query.sort("firstName", Sort.ASCENDING)
        } else {
            query.sort("firstName", Sort.DESCENDING)
        }
        val result = query.findAll()
        realm.commitTransaction()
        idArray.clear()
        result.forEach { item: KotlinItemModel ->
            idArray.add(item.centerId)
        }
        initList(idArray)
    }


    companion object {
        const val settingRequest = 1047
    }

    private fun showExitDialog() {

        val builder = AlertDialog.Builder(this@OfficeActivity)
        val dialog: AlertDialog
        val view = View.inflate(this@OfficeActivity, R.layout.exit_layout, null)
        val btnOk: AppCompatButton = view.findViewById(R.id.btn_ok)
        val btnNo: AppCompatButton = view.findViewById(R.id.btn_no)
        builder.setView(view)
        dialog = builder.create()
        dialog.show()
        val listener = View.OnClickListener { v ->
            when (v.id) {
                R.id.btn_ok -> {
                    dialog.dismiss()
                    val intent = Intent(applicationContext, MainActivity2::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.putExtra("EXIT", true)
                    startActivity(intent)
                    finish()
                }
                R.id.btn_no -> {
                    dialog.dismiss()
                }
            }
        }
        btnOk.setOnClickListener(listener)
        btnNo.setOnClickListener(listener)
    }

}

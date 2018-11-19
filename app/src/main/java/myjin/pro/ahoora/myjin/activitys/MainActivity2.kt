package myjin.pro.ahoora.myjin.activitys

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.drawer_layout.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.PagerAdapter
import myjin.pro.ahoora.myjin.adapters.SliderAdapter
import myjin.pro.ahoora.myjin.customClasses.SliderDecoration
import myjin.pro.ahoora.myjin.models.*
import myjin.pro.ahoora.myjin.models.events.*
import myjin.pro.ahoora.myjin.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity2 : AppCompatActivity(),
        ViewPager.OnPageChangeListener,
        AppBarLayout.OnOffsetChangedListener,
        View.OnClickListener, View.OnLongClickListener {

    private val bankPosition = 5
    private var fabH = 0f
    var isSearchVisible = true
    private var appBarOffset = 0
    private var currentPage = 5

    lateinit var tvLocation: AppCompatTextView

    val realm = Realm.getDefaultInstance()
    private var signIn = false
    private var number = ""

    companion object {
        const val settingRequest = 1564
        var active = false
    }

    override fun onLongClick(v: View?): Boolean {
        when (v?.id) {

            R.id.iv_jinDrawer -> {
                startActivity(Intent(this, LoginActivity::class.java))
                return true
            }

        }
        return false
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_search -> search()
            R.id.iv_menu -> openDrawerLayout()
            R.id.tv_login_outsign -> {

                if (signIn) {
                    startActivity(Intent(this, ProfileActivity::class.java))
                } else {
                    startActivity(Intent(this, Login2Activity::class.java))
                }
            }
            R.id.rl_exit -> showExitDialog()
            R.id.rl_myjin_services -> goToServicesActivity(getString(R.string.khj), 1)
            R.id.rl_takapoo_services -> goToServicesActivity(getString(R.string.mnvfs), 2)
            R.id.rl_university_services -> goToServicesActivity(tv_university_services_Title1.text.toString(), 3)
            R.id.rl_tamin_services -> goToServicesActivity(tv_tamin_services.text.toString(), 5)
            R.id.rl_ict_services -> goToServicesActivity(tv_ict_services.text.toString(), 6)
            R.id.rl_pishkhan_services -> goToServicesActivity(tv_pishkhan_services.text.toString(), 7)
            R.id.rl_post_services -> goToServicesActivity(tv_post_services.text.toString(), 8)
            R.id.rl_salamat -> goToServicesActivity(tv_drawerTitlesalamat.text.toString(), 4)
            R.id.tv_healthCenters -> startActivity(Intent(this, FavActivity::class.java))
            R.id.tv_messages -> startActivityForResult(Intent(this, FavMessageActivity::class.java), settingRequest)
            R.id.rl_drawer3 -> startActivity(Intent(this, AboutUs::class.java))
            R.id.rl_drawer4 -> startActivity(Intent(this, ContactUs::class.java))
            R.id.rl_setting -> startActivityForResult(Intent(this, SettingActivity::class.java), settingRequest)
            R.id.rl_rules -> startActivity(Intent(this, RulesActivity::class.java))
            R.id.rl_notifi -> startActivity(Intent(this, NotificationActivity::class.java))
            R.id.rl_onlineContact -> startActivity(Intent(this, ScripeChat::class.java))

        }
    }


    private fun closeDrawerLayout() {
        drawerLayout.closeDrawers()
    }

    private fun goToServicesActivity(title: String, i: Int) {
        val intentM = Intent(this, ServicesActivity::class.java)
        intentM.putExtra("ServiceTitle", title)
        intentM.putExtra("groupId", i)
        startActivity(intentM)
    }

    private fun getUserInfos() {
        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
        apiInterface.getUserInfo(number).enqueue(object : Callback<List<KotlinSignInModel>> {

            override fun onResponse(call: Call<List<KotlinSignInModel>>?, response: Response<List<KotlinSignInModel>>?) {
                val list = response?.body()
                val u = list?.get(0)
                if (u?.allow == "1") {
                    showViews()
                } else {
                    hideViews()
                }
                realm.executeTransactionAsync { realm: Realm? ->

                    realm?.where(KotlinSignInModel::class.java)?.findAll()?.deleteAllFromRealm()
                    realm?.copyToRealm(list!!)
                }
            }

            override fun onFailure(call: Call<List<KotlinSignInModel>>?, t: Throwable?) {
                Log.e("ERR", t?.message + "  ")
            }
        })

    }

    private fun isLogin() {
        realm.beginTransaction()
        val u = realm.where(KotlinSignInModel::class.java).findFirst()

        if (u != null) {
            try {
                signIn = true
                tv_login_outsign.text = "خوش آمدید " + u.firstName + " عزیز "
                number = u.phoneNumber!!
                SharedPer(this).setBoolean("signIn", signIn)
                if (u.allow == "1") {
                    showViews()
                } else {
                    hideViews()
                }

                if (number != "") {
                    getUserInfos()
                }
            } catch (e: Exception) {

            }
        } else {
            signIn = false
            tv_login_outsign.text = getString(R.string.vrodvozviat)
            SharedPer(this).setBoolean("signIn", signIn)

            hideViews()
        }
        realm.commitTransaction()
    }

    private fun showViews() {
        ll_services.visibility = View.VISIBLE
    }

    private fun hideViews() {
        ll_services.visibility = View.GONE
    }

    private fun setListener() {
        rl_notifi.setOnClickListener(this)
        iv_menu.setOnClickListener(this)
        iv_jinDrawer.setOnLongClickListener(this)
        // fab_search.setOnClickListener(this)
        iv_search.setOnClickListener(this)
        tv_location.setOnClickListener(this)
        rl_myjin_services.setOnClickListener(this)
        tv_healthCenters.setOnClickListener(this)
        tv_messages.setOnClickListener(this)
        rl_drawer3.setOnClickListener(this)
        rl_drawer4.setOnClickListener(this)
        rl_salamat.setOnClickListener(this)
        rl_myjin_services.setOnClickListener(this)
        rl_takapoo_services.setOnClickListener(this)
        rl_university_services.setOnClickListener(this)
        rl_tamin_services.setOnClickListener(this)
        rl_ict_services.setOnClickListener(this)
        rl_pishkhan_services.setOnClickListener(this)
        rl_post_services.setOnClickListener(this)
        rl_salamat.setOnClickListener(this)
        rl_setting.setOnClickListener(this)
        tv_login_outsign.setOnClickListener(this)
        rl_exit.setOnClickListener(this)
        rl_rules.setOnClickListener(this)
        rl_notifi.setOnClickListener(this)
        rl_onlineContact.setOnClickListener(this)
        tbl_main.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.customView?.findViewById<AppCompatImageView>(R.id.icon)?.setColorFilter(ContextCompat.getColor(this@MainActivity2, R.color.green), PorterDuff.Mode.SRC_IN)
                tab.customView?.findViewById<AppCompatTextView>(R.id.text1)?.setTextColor(ContextCompat.getColor(this@MainActivity2, R.color.green))
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.customView?.findViewById<AppCompatImageView>(R.id.icon)?.setColorFilter(ContextCompat.getColor(this@MainActivity2, R.color.tabTextDef), PorterDuff.Mode.SRC_IN)
                tab.customView?.findViewById<AppCompatTextView>(R.id.text1)?.setTextColor(ContextCompat.getColor(this@MainActivity2, R.color.tabTextDef))
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
    }


    private fun openDrawerLayout() {
        drawerLayout.openDrawer(GravityCompat.END)
    }


    override fun onResume() {
        super.onResume()
        isLogin()
        EventBus.getDefault().post(VisibilityEvent(currentPage))
    }

    override fun onStart() {
        super.onStart()
        active = true
        EventBus.getDefault().register(this)
        abp_main.addOnOffsetChangedListener(this)

    }

    override fun onStop() {
        abp_main.removeOnOffsetChangedListener(this)
        EventBus.getDefault().unregister(this)
        super.onStop()

    }

    override fun onDestroy() {
        super.onDestroy()

        active = false
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        setVisibleShadow(appBarLayout, verticalOffset)
        appBarOffset = verticalOffset
        if (appBarLayout?.totalScrollRange == Math.abs(verticalOffset)) {
            when (currentPage) {
                5 -> {
                    tv_mainTitle.text = getString(R.string.healthCenters)

                }
                4 -> {
                    tv_mainTitle.text = getString(R.string.etlaeieh)
                }
                3 -> {
                    tv_mainTitle.text = getString(R.string.filmoanimation)
                }
                2 -> {
                    tv_mainTitle.text = getString(R.string.pishnahadvizheh)
                }
                1 -> {
                    tv_mainTitle.text = getString(R.string.abzarmofid)
                }

                0 -> {
                    tv_mainTitle.text = getString(R.string.moshaverhoporsesh)
                }


            }
            tv_test.visibility = View.GONE
        } else {
            tv_mainTitle.text = getString(R.string.myJin)
            tv_test.visibility = View.VISIBLE
        }
    }

    private fun setVisibleShadow(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (currentPage == tbl_main.tabCount - 1) {
            if (appBarLayout?.totalScrollRange == Math.abs(verticalOffset)) {
                view_gradient.visibility = View.VISIBLE
            } else {
                view_gradient.visibility = View.GONE
            }
        }
    }

    @Suppress("UNUSED_EXPRESSION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getBooleanExtra("EXIT", false)) {
            finish()
        }
        NetworkUtil().updateNetFlag(this)
        netAvailability = NetworkUtil().isNetworkAvailable(this)
        setContentView(R.layout.activity_main2)

        tvLocation = tv_location
        fabH = Converter.pxFromDp(this, 16f + 50f + 20)



        vp_mainContainer.adapter = PagerAdapter(supportFragmentManager, this)
        vp_mainContainer.addOnPageChangeListener(this)
        tbl_main.setupWithViewPager(vp_mainContainer)
        setIcon()



        et_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
                Utils.closeKeyBoard(et_search.windowToken, this@MainActivity2)
            }
            false
        }


        et_search!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString() == "") {
                    search()
                }
            }
        })

        Handler().postDelayed({
            1
            // ipi_main.attachToViewPager(vp_mainContainer)
            if (SharedPer(this@MainActivity2).getDefTab(getString(R.string.defTab))) {
                vp_mainContainer.currentItem = tbl_main.tabCount - 1

            } else {
                vp_mainContainer.currentItem = tbl_main.tabCount - 2
                //  onPageSelected(tbl_main.tabCount-2)
            }
        }, 50)

        setListener()

        isLogin()
        checkNetState()

        val tf = SharedPer(this@MainActivity2).getIntro(getString(R.string.introductionFlag2))
        SharedPer(this).setBoolean(getString(R.string.introductionFlag), tf)
    }


    private var netAvailability = false

    @Subscribe
    fun netEvent(e: NetChangeEvent) {
        netAvailability = e.isCon
    }

    private fun checkNetState() {
        if (netAvailability) {

            getSlides()
            getSpList()
        } else {
            showNetErrSnack()
        }
    }

    @Subscribe
    fun tryAgainEvent(e: TryAgainEvent) {

        if (!sliderLoadFlag) {
            getSlides()
        }
        if (!spLoadFlag) {
            getSpList()
        }

    }

    fun showNetErrSnack() {
        hideSliderCPV()
        Snackbar.make(cl_homeContainer, R.string.khrda, Snackbar.LENGTH_INDEFINITE)
                .setAction("تلاش دوباره") {
                    Handler().postDelayed({
                        EventBus.getDefault().post(TryAgainEvent())
                    }, 1000)
                }.show()
    }

    private fun setIcon() {
        val drawable = ArrayList<Drawable>()
        drawable.add(
                ContextCompat.getDrawable(
                        this@MainActivity2,
                        R.drawable.ic_advice
                )!!
        )

        drawable.add(
                ContextCompat.getDrawable(
                        this@MainActivity2,
                        R.drawable.ic_usefull_tool
                )!!
        )

        drawable.add(
                ContextCompat.getDrawable(
                        this@MainActivity2,
                        R.drawable.ic_spe_offer
                )!!
        )

        drawable.add(
                ContextCompat.getDrawable(
                        this@MainActivity2,
                        R.drawable.ic_film_animation
                )!!
        )

        drawable.add(
                ContextCompat.getDrawable(
                        this@MainActivity2,
                        R.drawable.ic_messages
                )!!
        )

        drawable.add(
                ContextCompat.getDrawable(
                        this@MainActivity2,
                        R.drawable.ic_centers
                )!!
        )

        for (i in 0 until drawable.size) {
            val tab = tbl_main.getTabAt(i)?.customView
            val icon = tab?.findViewById<AppCompatImageView>(R.id.icon)
            icon?.setImageDrawable(drawable[i])
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    private var mOffset = 0f

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        /*if (positionOffset > mOffset) {
            Log.e("direction", "to right")
        } else if (positionOffset < mOffset && positionOffset != 0f) {
            Log.e("direction", "to left")
        } else {
            Log.e("direction", "no move")
        }
        mOffset = positionOffset
        val i = tbl_main.selectedTabPosition
        Log.e("offset", "$position - $positionOffset - $positionOffsetPixels")
        tbl_main.getTabAt(i)?.customView?.alpha = 1 - positionOffset*/
    }

    override fun onPageSelected(position: Int) {

        currentPage = position
        if (position != bankPosition) {
            view_gradient.visibility = View.GONE
            hideLocation()
            //  hideSearchFab()
        } else {
            setVisibleShadow(abp_main, appBarOffset)
            showLocation()
            //  showSearchFab()
        }

        EventBus.getDefault().post(VisibilityEvent(position))
    }


    private fun showLocation() {
        iv_locationArrrow.visibility = View.VISIBLE
        tv_location.visibility = View.VISIBLE
    }

    private fun hideLocation() {
        iv_locationArrrow.visibility = View.GONE
        tv_location.visibility = View.GONE
    }


    private fun search() {

        val value = et_search.text.toString()

        try {
            Utils.closeKeyBoard(et_search.windowToken, this@MainActivity2)
        } catch (e: IllegalStateException) {

        }

        if (currentPage == tbl_main.tabCount - 1) {

            if (value != "")
                et_search.setText("")

            if (value != "") {
                val intentS = Intent(this, SearchActivity::class.java)

                val mBundle = Bundle()
                mBundle.putString("sVal", value)
                intentS.putExtras(mBundle)
                startActivity(intentS)
            }
        } else if (currentPage == tbl_main.tabCount - 2) {
            EventBus.getDefault().post(SearchMEvent(value))
        }

    }

    private var sliderLoadFlag = false
    private var spLoadFlag = false
    private var itemLoadFlag = false

    private fun initSlider(list: ArrayList<String>) {

        Handler().postDelayed({
            rv_mainSlider.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rv_mainSlider.addItemDecoration(SliderDecoration(this, 8))
            rv_mainSlider.adapter = SliderAdapter(this, list)
            hideSliderCPV()
        }, 500)

        sliderLoadFlag = true
    }

    private fun getSlides() {

        showSliderCPV()
        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
        apiInterface.sliderMain(1).enqueue(object : Callback<List<KotlinSlideMainModel>> {

            override fun onResponse(call: Call<List<KotlinSlideMainModel>>?, response: Response<List<KotlinSlideMainModel>>?) {
                response?.body() ?: onFailure(call, Throwable("null body"))
                response?.body() ?: return

                val list = response.body()
                val urls = ArrayList<String>()
                list?.get(0)!!.slideList?.forEach { i ->
                    urls.add(i.fileUrl!!)
                }
                sliderLoadFlag = true
                initSlider(urls)
            }

            override fun onFailure(call: Call<List<KotlinSlideMainModel>>?, t: Throwable?) {
                Log.e("ERR", t?.message + "  ")
                sliderLoadFlag = false
                showNetErrSnack()
            }
        })
    }





    private fun getSpList() {
        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
        val response = apiInterface.spList
        response.enqueue(object : Callback<List<KotlinSpecialityModel2>> {
            override fun onResponse(call: Call<List<KotlinSpecialityModel2>>?, response: Response<List<KotlinSpecialityModel2>>?) {
                val list: List<KotlinSpecialityModel2>? = response?.body()
                list?.forEach { sp: KotlinSpecialityModel2 ->
                    sp.saved = true
                }

                realm.executeTransactionAsync { realm: Realm? ->
                    realm?.copyToRealmOrUpdate(list!!)

                }
                spLoadFlag = true
            }

            override fun onFailure(call: Call<List<KotlinSpecialityModel2>>?, t: Throwable?) {
                spLoadFlag = false
            }
        })
    }


    private fun showSliderCPV() {
        cpv_slideLoad.visibility = View.VISIBLE
    }

    private fun hideSliderCPV() {
        cpv_slideLoad.visibility = View.GONE
    }

    private fun showExitDialog() {

        val builder = AlertDialog.Builder(this@MainActivity2)
        val dialog: AlertDialog
        val view = View.inflate(this@MainActivity2, R.layout.exit_layout, null)
        val btnOk: AppCompatButton = view.findViewById(R.id.btn_ok)
        val btnNo: AppCompatButton = view.findViewById(R.id.btn_no)
        builder.setView(view)
        dialog = builder.create()
        dialog.show()
        val listener = View.OnClickListener { v ->
            when (v.id) {
                R.id.btn_ok -> {
                    dialog.dismiss()
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

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawers()
        } else {
            showExitDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == settingRequest) {

                if (data?.getBooleanExtra(getString(R.string.messagesClean), false)!!) {
                    Handler().postDelayed({
                        EventBus.getDefault().post(TestEvent())
                    }, 100)
                }
            }
        }
    }

}
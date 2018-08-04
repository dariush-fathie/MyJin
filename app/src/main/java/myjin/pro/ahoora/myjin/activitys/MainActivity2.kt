package myjin.pro.ahoora.myjin.activitys

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import io.realm.Realm
import ir.paad.audiobook.utils.Converter
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.drawer_layout.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.PagerAdapter
import myjin.pro.ahoora.myjin.adapters.SliderAdapter
import myjin.pro.ahoora.myjin.customClasses.SliderDecoration
import myjin.pro.ahoora.myjin.models.KotlinSlideMainModel
import myjin.pro.ahoora.myjin.models.KotlinSpecialityModel
import myjin.pro.ahoora.myjin.models.events.NetChangeEvent
import myjin.pro.ahoora.myjin.models.events.TestEvent
import myjin.pro.ahoora.myjin.models.events.TryAgainEvent
import myjin.pro.ahoora.myjin.models.events.VisibilityEvent
import myjin.pro.ahoora.myjin.utils.ApiInterface
import myjin.pro.ahoora.myjin.utils.KotlinApiClient
import myjin.pro.ahoora.myjin.utils.NetworkUtil
import myjin.pro.ahoora.myjin.utils.SharedPer
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity2 : AppCompatActivity(),
        TabLayout.OnTabSelectedListener,
        ViewPager.OnPageChangeListener,
        AppBarLayout.OnOffsetChangedListener,
        View.OnClickListener, View.OnLongClickListener {

    companion object {
        const val settingRequest = 1564
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
            R.id.fab_search -> search()
            R.id.iv_menu -> openDrawerLayout()
            R.id.tv_login_outsign -> Toast.makeText(this, "این قسمت در نسخه جدید ارائه شده است", Toast.LENGTH_LONG).show()
            R.id.btn_exit -> showExitDialog()
            R.id.rl_myjin_services -> goToServicesActivity(tv_myjin_services_Title1.text.toString(), 1)
            R.id.rl_takapoo_services -> goToServicesActivity(getString(R.string.takapoo), 2)
            R.id.rl_university_services -> goToServicesActivity(tv_university_services_Title1.text.toString(), 3)
            R.id.rl_tamin_services -> goToServicesActivity(tv_tamin_services.text.toString(), 5)
            R.id.rl_ict_services -> goToServicesActivity(tv_ict_services.text.toString(), 6)
            R.id.rl_pishkhan_services -> goToServicesActivity(tv_pishkhan_services.text.toString(), 7)
            R.id.rl_post_services -> goToServicesActivity(tv_post_services.text.toString(), 8)
            R.id.rl_salamat -> goToServicesActivity(tv_drawerTitlesalamat.text.toString(), 4)
            R.id.tv_healthCenters -> startActivity(Intent(this, FavActivity::class.java))
            R.id.tv_messages -> startActivity(Intent(this, FavMessageActivity::class.java))
            R.id.rl_drawer3 -> startActivity(Intent(this, AboutUs::class.java))
            R.id.rl_drawer4 -> startActivity(Intent(this, ContactUs::class.java))
            R.id.rl_setting -> startActivityForResult(Intent(this, SettingActivity::class.java), settingRequest)
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

    private fun setListener() {
        iv_menu.setOnClickListener(this)
        iv_jinDrawer.setOnLongClickListener(this)
        fab_search.setOnClickListener(this)
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
        btn_exit.setOnClickListener(this)
    }


    private fun openDrawerLayout() {
        drawerLayout.openDrawer(GravityCompat.END)
    }

    private val bankPosition = 1
    private var fabH = 0f
    var isSearchVisible = true
    private var appBarOffset = 0
    private var currentPage = 0

    lateinit var tvLocation: AppCompatTextView

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().post(VisibilityEvent(currentPage))
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        abp_main.addOnOffsetChangedListener(this)
        Log.e("sdklfjslf", "ffsdklfjsdlf")
    }

    override fun onStop() {
        abp_main.removeOnOffsetChangedListener(this)
        EventBus.getDefault().unregister(this)
        super.onStop()

    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        setVisibleShadow(appBarLayout, verticalOffset)
        appBarOffset = verticalOffset
        if (appBarLayout?.totalScrollRange == Math.abs(verticalOffset)) {
            when (currentPage) {
                0 -> {
                    tv_mainTitle.text = "پیام و اطلاعیه"
                }
                1 -> {
                    tv_mainTitle.text = "بانک سلامت"
                }
                /* 2 -> {
                     tv_mainTitle.text = "نشان شده ها"
                 }*/
            }
        } else {
            tv_mainTitle.text = getString(R.string.myJin)
        }
    }

    private fun setVisibleShadow(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (currentPage == 1) {
            if (appBarLayout?.totalScrollRange == Math.abs(verticalOffset)) {
                view_gradient.visibility = View.VISIBLE
            } else {
                view_gradient.visibility = View.GONE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkUtil().updateNetFlag(this)
        netAvailability = NetworkUtil().isNetworkAvailable(this)
        setContentView(R.layout.activity_main2)

        tvLocation = tv_location
        fabH = Converter.pxFromDp(this, 16f + 50f + 20)

/*        tbl_main?.addTab(tbl_main.newTab()
                .setText("بانک سلامت"), false)
        tbl_main?.addTab(tbl_main.newTab()
                .setText("پیغام ها"), false)
        tbl_main?.addTab(tbl_main.newTab()
                .setText("نشان شده ها"), false)*/

        tbl_main.addOnTabSelectedListener(this)

        vp_mainContainer.adapter = PagerAdapter(supportFragmentManager)
        vp_mainContainer.addOnPageChangeListener(this)
        vp_mainContainer.offscreenPageLimit = 2
        tbl_main.setupWithViewPager(vp_mainContainer)


        Handler().postDelayed({
            ipi_main.attachToViewPager(vp_mainContainer)
            if (SharedPer(this@MainActivity2).getDefTab(getString(R.string.defTab))) {
                vp_mainContainer.currentItem = 1
                Log.e("XXX", "ZZZ")
            } else {
                onPageSelected(0)
            }
        }, 50)

        setListener()
        checkNetState()

        val tf = SharedPer(this@MainActivity2).getBoolean(getString(R.string.introductionFlag2))
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
        Snackbar.make(cl_homeContainer, "خطایی رخ داد دوباره امتحان کنید", Snackbar.LENGTH_INDEFINITE)
                .setAction("تلاش دوباره") {
                    Handler().postDelayed({
                        EventBus.getDefault().post(TryAgainEvent())
                    }, 1000)
                }.show()
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
        Log.e("pageSelected", position.toString())
        currentPage = position
        if (position != bankPosition) {
            view_gradient.visibility = View.GONE
            hideLocation()
            hideSearchFab()
        } else {
            setVisibleShadow(abp_main, appBarOffset)
            showLocation()
            showSearchFab()
        }

        EventBus.getDefault().post(VisibilityEvent(position))
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        Log.e("tabSelected", "${tab?.position}")
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        //addOrRemoveColorFilter(tab!!, false)

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        //addOrRemoveColorFilter(tab!!, true)
        //newGenerSelected(tab.position)

    }

    /*private fun addOrRemoveColorFilter(tab: TabLayout.Tab, addFilter: Boolean) {
        val view = tab.customView
        val text = view?.findViewById<AppCompatTextView>(R.id.tv_customTabTitle)
        val cardView = view?.findViewById<CardView>(R.id.cv_customTabContainer)
        val colors = Colors(this)

        if (addFilter) {
            view?.scaleY = 1f
            view?.scaleX = 1f
            text?.setTextColor(colors.colorPrimaryDark)
            text?.setBackgroundResource(R.drawable.empty)
            cardView?.cardElevation = 3f
            cardView?.setCardBackgroundColor(colors.white)
        } else {
            view?.scaleY = 0.85f
            view?.scaleX = 0.85f
            text?.setTextColor(colors.title)
            text?.setBackgroundResource(R.drawable.dot_rect_back)
            cardView?.cardElevation = 0f
            cardView?.setCardBackgroundColor(colors.transparent)
        }
    }*/

    private fun showLocation() {
        iv_locationArrrow.visibility = View.VISIBLE
        tv_location.visibility = View.VISIBLE
    }

    private fun hideLocation() {
        iv_locationArrrow.visibility = View.GONE
        tv_location.visibility = View.GONE
    }


    fun showSearchFab() {
        if (currentPage == bankPosition) {
            if (fab_search.translationY != 0f) {
                isSearchVisible = true
                val animSet = AnimatorSet()
                val alphaAnimator = ObjectAnimator.ofFloat(fab_search, "alpha", 0f, 1f)
                val transitionAnimator = ObjectAnimator.ofFloat(fab_search, "translationY", fabH, 0f)
                animSet.playTogether(alphaAnimator, transitionAnimator)
                animSet.start()
            }
        }
    }

    fun hideSearchFab() {
        if (fab_search.translationY != fabH) {
            isSearchVisible = false
            val animSet = AnimatorSet()
            val alphaAnimator = ObjectAnimator.ofFloat(fab_search, "alpha", 1f, 0f)
            val transitionAnimator = ObjectAnimator.ofFloat(fab_search, "translationY", 0f, fabH)
            animSet.playTogether(alphaAnimator, transitionAnimator)
            animSet.start()
        }
    }

    private fun search() {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    private var sliderLoadFlag = false
    private var spLoadFlag = false

    private fun initSlider(list: ArrayList<String>) {
        Log.e("sdfdsfds", "sdfsfsfsf")
        Handler().postDelayed({
            rv_mainSlider.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rv_mainSlider.addItemDecoration(SliderDecoration(this, 8))
            rv_mainSlider.adapter = SliderAdapter(this, list)
            hideSliderCPV()
        }, 500)

        sliderLoadFlag = true
    }

    private fun getSlides() {
        Log.e("sdfdsfds", "sdfsfsfsf")
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
        response.enqueue(object : Callback<List<KotlinSpecialityModel>> {
            override fun onResponse(call: Call<List<KotlinSpecialityModel>>?, response: Response<List<KotlinSpecialityModel>>?) {
                val list: List<KotlinSpecialityModel>? = response?.body()
                list?.forEach { sp: KotlinSpecialityModel ->
                    sp.saved = true
                }
                val realmDatabase = Realm.getDefaultInstance()
                realmDatabase.executeTransactionAsync { realm: Realm? ->
                    realm?.copyToRealmOrUpdate(list!!)
                    /*val savedSps = realm?.where(KotlinSpecialityModel::class.java)?.findAll()
                    realm?.where(KotlinSpecialityModel::class.java)?.findAll()?.deleteAllFromRealm()
                    list?.forEach { spl: KotlinSpecialityModel ->
                        spl.saved = true
                        realm?.copyToRealm(spl)
                    }*/
                    /*val r = realm?.where(KotlinSpecialityModel::class.java)?.findAll()
                    r!!.forEach { model: KotlinSpecialityModel? ->
                        Log.e("SP", "${model?.name}:${model?.specialtyId}:${model?.saved}")
                    }
                    */
                }
                spLoadFlag = true
            }

            override fun onFailure(call: Call<List<KotlinSpecialityModel>>?, t: Throwable?) {
                Log.e("ERR", t?.message + "  ")
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
        val btn_ok: AppCompatButton = view.findViewById(R.id.btn_ok)
        val btn_no: AppCompatButton = view.findViewById(R.id.btn_no)
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
        btn_ok.setOnClickListener(listener)
        btn_no.setOnClickListener(listener)
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
        Log.e("mainActivity2", "onresult")
        if (resultCode == Activity.RESULT_OK) {
            Log.e("mainActivity2", "onresult")
            if (requestCode == settingRequest) {
                Log.e("mainActivity2", "onresult")
                if (data?.getBooleanExtra(getString(R.string.messagesClean) , false)!!){
                    Handler().postDelayed({
                        EventBus.getDefault().post(TestEvent())
                    },100)
                }
            }
        }
    }

}
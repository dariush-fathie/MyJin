package myjin.pro.ahoora.myjin.activitys

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.DialogInterface
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
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.CardView
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
import myjin.pro.ahoora.myjin.models.KotlinAboutContactModel
import myjin.pro.ahoora.myjin.models.KotlinProvCityModel
import myjin.pro.ahoora.myjin.models.KotlinServicesModel
import myjin.pro.ahoora.myjin.models.KotlinSlideMainModel
import myjin.pro.ahoora.myjin.models.events.NetChangeEvent
import myjin.pro.ahoora.myjin.models.events.TryAgainEvent
import myjin.pro.ahoora.myjin.models.events.VisibilityEvent
import myjin.pro.ahoora.myjin.utils.ApiInterface
import myjin.pro.ahoora.myjin.utils.Colors
import myjin.pro.ahoora.myjin.utils.KotlinApiClient
import myjin.pro.ahoora.myjin.utils.Utils
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


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_login_outsign -> Toast.makeText(this@MainActivity2, "این قسمت در نسخه جدید ارائه شده است", Toast.LENGTH_LONG).show()
            R.id.rl_exit -> showExitDialog()
            R.id.iv_menu -> openDrawerLayout()
            R.id.fab_search -> search()
            R.id.rl_myjin_services -> goToServicesActivity(tv_myjin_services_Title1.text.toString(), 1)
            R.id.rl_takapoo_services -> goToServicesActivity(getString(R.string.takapoo), 2)
            R.id.rl_university_services -> goToServicesActivity(tv_university_services_Title1.text.toString(), 3)
            R.id.rl_tamin_services -> cummingSoon()
            R.id.rl_ict_services -> cummingSoon()
            R.id.rl_pishkhan_services -> cummingSoon()
            R.id.rl_post_services -> cummingSoon()
            R.id.rl_salamat -> startActivity(Intent(this@MainActivity2, HeaIncServiceActivity::class.java))
            R.id.rl_drawer2 -> drawerClick(1)
            R.id.rl_drawer3 -> drawerClick(2)
            R.id.rl_drawer4 -> drawerClick(3)
        }
    }

    private fun openDrawerLayout() {
        drawerLayout.openDrawer(GravityCompat.END)
    }

    private fun closeDrawerLayout() {
        drawerLayout.closeDrawers()
    }
    private fun cummingSoon() {
        Toast.makeText(this, "بزودی", Toast.LENGTH_LONG).show()
    }
    private fun goToServicesActivity(title: String, i: Int) {
        val intentM = Intent(this@MainActivity2, ServicesActivity::class.java)
        intentM.putExtra("ServiceTitle", title)
        intentM.putExtra("groupId", i)
        startActivity(intentM)
    }

    private fun drawerClick(position: Int) {
        when (position) {
            1 -> {

                startActivity(Intent(this@MainActivity2, FavActivity::class.java))
            }
            2 -> {
                startActivity(Intent(this@MainActivity2, AboutUs::class.java))
            }
            3 -> {
                startActivity(Intent(this@MainActivity2, ContactUs::class.java))
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        when (v?.id) {
            R.id.iv_jinDrawer -> {
                startActivity(Intent(this@MainActivity2, LoginActivity::class.java))
                return true
            }
        }
        return false
    }

    private val bankPosition = 0
    private var fabH = 0f
    private var isSearchVisible = true
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
                    tv_mainTitle.text = "بانک سلامت"
                }
                1 -> {
                    tv_mainTitle.text = "پیام ها"
                }
                2 -> {
                    tv_mainTitle.text = "نشان شده ها"
                }
            }
        } else {
            tv_mainTitle.text = getString(R.string.myJin)
        }
    }

    private fun setVisibleShadow(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (currentPage == 0) {
            if (appBarLayout?.totalScrollRange == Math.abs(verticalOffset)) {
                view_gradient.visibility = View.VISIBLE
            } else {
                view_gradient.visibility = View.GONE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        tvLocation = tv_location

        fabH = Converter.pxFromDp(this, 16f + 50f + 20)

        tbl_main?.addTab(tbl_main.newTab()
                .setText("بانک سلامت"), false)
        tbl_main?.addTab(tbl_main.newTab()
                .setText("پیغام ها"), false)
        tbl_main?.addTab(tbl_main.newTab()
                .setText("نشان شده ها"), false)

        tbl_main.addOnTabSelectedListener(this)

        vp_mainContainer.adapter = PagerAdapter(supportFragmentManager)
        vp_mainContainer.addOnPageChangeListener(this)
        vp_mainContainer.offscreenPageLimit = 4
        tbl_main.setupWithViewPager(vp_mainContainer)
        Handler().postDelayed({
            tbl_main?.getTabAt(0)?.select()
            ipi_main.attachToViewPager(vp_mainContainer)
        }, 50)
        setListener()

        checkNetState()
    }

    private fun getAC_() {
        if (Utils.isNetworkAvailable(this@MainActivity2)) {

            val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
            val response = apiInterface.ac
            response.enqueue(object : Callback<KotlinAboutContactModel> {
                override fun onResponse(call: Call<KotlinAboutContactModel>?, response: Response<KotlinAboutContactModel>?) {

                    val list: KotlinAboutContactModel? = response?.body()
                    val realm = Realm.getDefaultInstance()
                    realm.executeTransactionAsync { db: Realm? ->
                        db?.where(KotlinAboutContactModel::class.java)?.findAll()?.deleteAllFromRealm()
                        db?.copyToRealm(list!!)
                    }

                }

                override fun onFailure(call: Call<KotlinAboutContactModel>?, t: Throwable?) {

                }
            })
        }
    }

    @Subscribe
    fun netEvent(e: NetChangeEvent) {

    }

    private fun checkNetState() {
        if (true) {
            getSlides()
            getServicesList()
            getProvinceAndCitiesList()
            getAC_()
            //todo : get services and others ...
        } else {
            showNetErrSnack()
        }
    }

    @Subscribe
    fun tryAgainEvent(e: TryAgainEvent) {
        if (!sliderLoadFlag) {
            getSlides()
        }
        if (!servicesLoadFlag) {
            getServicesList()
        }
        if (!provsLoadFlag) {
            getProvinceAndCitiesList()
        }
        // todo : get services
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

    private fun setListener() {
        rl_exit.setOnClickListener(this)
        iv_menu.setOnClickListener(this)
        rl_myjin_services.setOnClickListener(this)
        rl_drawer2.setOnClickListener(this)
        rl_drawer3.setOnClickListener(this)
        fab_search.setOnClickListener(this)
        rl_drawer4.setOnClickListener(this)
        rl_salamat.setOnClickListener(this)
        fab_search.setOnClickListener(this)
        tv_location.setOnClickListener(this)
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
            if (isSearchVisible) {
                hideSearchFab()
            }
        } else {
            setVisibleShadow(abp_main, appBarOffset)
            showLocation()
            if (!isSearchVisible) {
                showSearchFab()
            }
        }

        EventBus.getDefault().post(VisibilityEvent(position))

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        addOrRemoveColorFilter(tab!!, false)
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        addOrRemoveColorFilter(tab!!, true)
        //newGenerSelected(tab.position)
    }

    private fun addOrRemoveColorFilter(tab: TabLayout.Tab, addFilter: Boolean) {
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
    }

    private fun showLocation() {
        iv_locationArrrow.visibility = View.VISIBLE
        tv_location.visibility = View.VISIBLE
    }

    private fun hideLocation() {
        iv_locationArrrow.visibility = View.GONE
        tv_location.visibility = View.GONE
    }

    fun visibleSearchFab() {
        fab_search.visibility = View.VISIBLE
        showSearchFab()
    }

    private fun showSearchFab() {
        val animSet = AnimatorSet()
        val alphaAnimator = ObjectAnimator.ofFloat(fab_search, "alpha", 0f, 1f)
        val transitionAnimator = ObjectAnimator.ofFloat(fab_search, "translationY", fabH, 0f)
        animSet.playTogether(alphaAnimator, transitionAnimator)
        animSet.start()
        isSearchVisible = true
    }

    fun hideSearchFab() {
        val animSet = AnimatorSet()
        val alphaAnimator = ObjectAnimator.ofFloat(fab_search, "alpha", 1f, 0f)
        val transitionAnimator = ObjectAnimator.ofFloat(fab_search, "translationY", 0f, fabH)
        animSet.playTogether(alphaAnimator, transitionAnimator)
        animSet.start()
        isSearchVisible = false
    }

    private fun search() {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    private var sliderLoadFlag = false
    private var servicesLoadFlag = false
    private var provsLoadFlag = false

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

                initSlider(urls)
            }

            override fun onFailure(call: Call<List<KotlinSlideMainModel>>?, t: Throwable?) {
                Log.e("ERR", t?.message + "  ")
                sliderLoadFlag = false
                showNetErrSnack()
            }
        })
    }

    private fun getServicesList() {
        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
        val response = apiInterface.servicesList
        response.enqueue(object : Callback<List<KotlinServicesModel>> {
            override fun onResponse(call: Call<List<KotlinServicesModel>>?, response: Response<List<KotlinServicesModel>>?) {
                val list: List<KotlinServicesModel>? = response?.body()
                val realm = Realm.getDefaultInstance()
                realm.executeTransactionAsync { db: Realm? ->
                    db?.where(KotlinServicesModel::class.java)?.findAll()?.deleteAllFromRealm()
                    db?.copyToRealm(list!!)
                }
                servicesLoadFlag = true
            }

            override fun onFailure(call: Call<List<KotlinServicesModel>>?, t: Throwable?) {
                Toast.makeText(this@MainActivity2, "خطا در اتصال به سرور", Toast.LENGTH_SHORT).show()
                servicesLoadFlag = false
                showNetErrSnack()
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
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage("خارج می شوید ؟")
                .setNegativeButton("نه", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })
                .setPositiveButton("بله", DialogInterface.OnClickListener { _, _ ->
                    finish()
                })
        alertDialog.show()
    }

    // todo put this in splash
    private fun getProvinceAndCitiesList() {
        tv_location.visibility = View.GONE

        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val cityCount = realm.where(KotlinProvCityModel::class.java).findAll().size
        realm.commitTransaction()

        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
        val response = apiInterface.getProvinceAndCitiesList(cityCount)
        response.enqueue(object : Callback<List<KotlinProvCityModel>> {
            override fun onResponse(call: Call<List<KotlinProvCityModel>>?, response: Response<List<KotlinProvCityModel>>?) {
                response?.body() ?: onFailure(call, Throwable("null body"))
                response?.body() ?: return

                val list: List<KotlinProvCityModel>? = response.body()
                if (list != null) {
                    if (list.isNotEmpty()) {
                        val realm1 = Realm.getDefaultInstance()
                        realm1.executeTransactionAsync { db: Realm? ->
                            db?.where(KotlinProvCityModel::class.java)?.findAll()?.deleteAllFromRealm()
                            db?.copyToRealm(list)
                        }
                    }
                } else {
                    onFailure(call, Throwable("null city list"))
                    return
                }

                provsLoadFlag = true
                tv_location.visibility = View.VISIBLE
            }

            override fun onFailure(call: Call<List<KotlinProvCityModel>>?, t: Throwable?) {
                Toast.makeText(this@MainActivity2, "خطا در اتصال به سرور", Toast.LENGTH_SHORT).show()
                provsLoadFlag = false
                showNetErrSnack()
            }
        })
    }

}
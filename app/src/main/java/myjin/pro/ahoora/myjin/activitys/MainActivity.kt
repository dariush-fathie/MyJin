package myjin.pro.ahoora.myjin.activitys

import android.animation.ObjectAnimator
import android.content.*
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.net_err_layout.*
import kotlinx.android.synthetic.main.progress_layout.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.SliderAdapter
import myjin.pro.ahoora.myjin.customClasses.TwoColGridDecoration
import myjin.pro.ahoora.myjin.interfaces.ServerStatusResponse
import myjin.pro.ahoora.myjin.models.KotlinGroupModel
import myjin.pro.ahoora.myjin.models.KotlinServicesModel
import myjin.pro.ahoora.myjin.models.KotlinSlideMainModel
import myjin.pro.ahoora.myjin.models.KotlinSpecialityModel
import myjin.pro.ahoora.myjin.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener, ServerStatusResponse {

    internal var Receiver: BroadcastReceiver? = null
    var serverStatus: ServerStatus? = null
    lateinit var adapter: CategoryAdapter
    private var scrollToBottom = true

    private var n = 0
    private var forward = true
    private var provId = 19
    private var cityId = 1
    private var t = Timer()
    private var play = true
    /*copied*/
    override fun onClick(v: View?) {
        when (v?.id) {
            //R.id.btn_fav -> openDrawerLayout()
            //R.id.btn_tryAgain -> tryAgain()
            R.id.tv_login_outsign -> Toast.makeText(this@MainActivity, "این قسمت در نسخه جدید ارائه شده است", Toast.LENGTH_LONG).show()
            R.id.rl_exit -> showExitDialog()
            //R.id.iv_menu -> openDrawerLayout()
            //R.id.fab_search -> search()
            R.id.rl_myjin_services -> goToServicesActivity(tv_myjin_services_Title1.text.toString(), 1)
            R.id.rl_takapoo_services -> goToServicesActivity(getString(R.string.takapoo), 2)
            R.id.rl_university_services -> goToServicesActivity(tv_university_services_Title1.text.toString(), 3)
            R.id.rl_tamin_services -> cummingSoon()
            R.id.rl_ict_services -> cummingSoon()
            R.id.rl_pishkhan_services -> cummingSoon()
            R.id.rl_post_services -> cummingSoon()
            R.id.rl_salamat -> startActivity(Intent(this@MainActivity, HeaIncServiceActivity::class.java))
            R.id.rl_drawer2 -> drawerClick(1)
            R.id.rl_drawer3 -> drawerClick(2)
            R.id.rl_drawer4 -> drawerClick(3)
         /*   R.id.rl_city -> openCityDialog()
            R.id.rl_prov -> openProvDialog()*/
        }
    }
    /*copied*/
    private fun setClickListeners() {
        //btn_tryAgain.setOnClickListener(this)
        //rl_prov.setOnClickListener(this)
        //rl_city.setOnClickListener(this)
        //rl_exit.setOnClickListener(this)
        iv_menu.setOnClickListener(this)
        rl_myjin_services.setOnClickListener(this)
        rl_drawer2.setOnClickListener(this)
        rl_drawer3.setOnClickListener(this)
        btn_fav.setOnClickListener(this)
        fab_search.setOnClickListener(this)
        rl_drawer4.setOnClickListener(this)
        rl_salamat.setOnClickListener(this)

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

    /*copied*/
    private fun goToServicesActivity(title: String, i: Int) {
        val intentM = Intent(this@MainActivity, ServicesActivity::class.java)
        intentM.putExtra("ServiceTitle", title)
        intentM.putExtra("groupId", i)
        startActivity(intentM)
    }

    /*copied*/
    private fun drawerClick(position: Int) {
        when (position) {
            1 -> {
                startActivity(Intent(this@MainActivity, FavActivity::class.java))
            }
            2 -> {
                startActivity(Intent(this@MainActivity, AboutUs::class.java))
            }
            3 -> {
                startActivity(Intent(this@MainActivity, ContactUs::class.java))
            }
        }
    }

    /*copied*/
    private fun showExitDialog() {
        closeDrawerLayout()
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage("خروج از برنامه ؟")
                .setPositiveButton("نه", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })
                .setNegativeButton("بله", DialogInterface.OnClickListener { _, _ ->
                    finish()
                })
        alertDialog.show()
    }

    /*copied*/
    override fun onLongClick(v: View?): Boolean {
        when (v?.id) {
            R.id.iv_jinDrawer -> {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                return true
            }
        }
        return false
    }

    /*copied*/
    private fun openCityDialog() {
        /*val cityArray = ArrayList<String>()
        cityArray.add("همه")
        cityArray.add("سنندج")
        cityArray.add("سقز")
        cityArray.add("قروه")
        cityArray.add("مریوان")
        cityArray.add("بانه")
        cityArray.add("کامیاران")
        cityArray.add("بیجار")
        cityArray.add("دیواندره")
        cityArray.add("دهگلان")
        cityArray.add("سروآباد")
        val dialog = SpinnerDialog(this@MainActivity, cityArray, "شهر خود را انتخاب کنید :", "نمیخوام")
        dialog.bindOnSpinerListener(OnSpinerItemClick { name, index ->
            cityId = index
            initList()
            tv_city.text = name
        })
        dialog.showSpinerDialog()*/
    }

    /*copied*/
    private fun openProvDialog() {
        val x = resources.getStringArray(R.array.provArray)
        val provArray = ArrayList<String>()

        x.forEach { s ->
            provArray.add(s)
        }

        /*val dialog = SpinnerDialog(this@MainActivity, provArray, "استان خود را انتخاب کنید :", "نمیخوام")

        dialog.bindOnSpinerListener { _, index ->
            //initList()
            if (index != 19) {
                Toast.makeText(this, "این برنامه در حال حاضر تنها استان کردستان را پوشش می دهد ..", Toast.LENGTH_LONG).show()
            }
            tv_prov.text = provArray.get(19)
        }
        dialog.showSpinerDialog()*/
    }
    /*copied*/
    private fun cummingSoon() {
        Toast.makeText(this, "بزودی", Toast.LENGTH_LONG).show()
    }
    /*copied*/
    private fun tryAgain() {
        initList()
    }
    /*copied*/
    private fun showNetErrLayout() {
        ll_netErr.visibility = View.VISIBLE
    }
    /*copied*/
    private fun hideNetErrLayout() {
        ll_netErr.visibility = View.GONE
    }
    /*copied*/
    private fun openDrawerLayout() {
        drawerLayout.openDrawer(GravityCompat.END)
    }
    /*copied*/
    private fun closeDrawerLayout() {
        drawerLayout.closeDrawers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (VarableValues.first) {
            startActivity(Intent(this, SplashScreen::class.java))
        } else {
            startActivity(Intent(this, AppIntro::class.java))
        }

        setContentView(R.layout.activity_main)

        serverStatus = ServerStatus(this, this@MainActivity)

        setClickListeners()
        initList()

        rv_category.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollToBottom = dy > 0
            }
        })

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rv_main_slider)

        startReceive()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(Receiver, IntentFilter(getString(R.string.reciver)))
    }

    private fun playOrStop() {
        if (play) {
            play = false
            t.cancel()
        } else {
            play = true
            autoScrollSlide()
        }
    }

    private fun startReceive() {
        if (Receiver == null) {
            Receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.getAction().equals(getString(R.string.reciver))) {
                        playOrStop()
                    }
                }
            }
        }
    }

    /*copied*/
    private fun search() {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    private fun initList() {
        if (Utils.isNetworkAvailable(this@MainActivity)) {
            t.cancel()
            //serverStatus?.IsOkServer()
        } else {
            showNetErrLayout()
            Toast.makeText(this, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show()
        }
    }

    override fun isOk() {
        getGroupCount()
        getSpList()// from server
        sliderUrls()
        getServicesList()
    }

    /*copied*/
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
            }

            override fun onFailure(call: Call<List<KotlinServicesModel>>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "خطا در اتصال به سرور", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun notOk() {
        if (VarableValues.NetworkState) {
            startActivity(Intent(this, ServerStatusActivity::class.java))
            finish()
        } else {
            showNetErrLayout()
            Toast.makeText(this, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show()
        }
    }

    private fun autoScrollSlide() {
        val handler = Handler()
        t = Timer()
        t.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    if (forward) {
                        rv_main_slider.smoothScrollToPosition(n++)
                    } else {
                        rv_main_slider.smoothScrollToPosition(n--)
                    }

                    if (n == rv_main_slider.adapter.itemCount - 1) {
                        forward = false
                        n = rv_main_slider.adapter.itemCount - 1

                    }
                    if (n == 0) {
                        forward = true
                    }
                }
            }
        }, 1, 4000)
    }

    /*copied*/
    private fun slider(list: ArrayList<String>) {
        rv_main_slider.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        rv_main_slider.adapter = SliderAdapter(this@MainActivity, list)
        list_indicator_m.attachToRecyclerView(rv_main_slider)
        autoScrollSlide()
    }

    /*copied*/
    private fun sliderUrls() {
        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
        apiInterface.sliderMain(1).enqueue(object : Callback<List<KotlinSlideMainModel>> {

            override fun onResponse(call: Call<List<KotlinSlideMainModel>>?, response: Response<List<KotlinSlideMainModel>>?) {
                val list = response?.body()
                val urls = ArrayList<String>()
                list?.get(0)!!.slideList?.forEach { i ->
                    urls.add(i.fileUrl!!)
                }
                slider(urls)
            }

            override fun onFailure(call: Call<List<KotlinSlideMainModel>>?, t: Throwable?) {
                Log.e("ERR", t?.message + "  ")
            }


        })
    }


    private fun showProgressLayout() {
        ll_progressLayout.visibility = View.VISIBLE
        fab_search.visibility = View.VISIBLE
    }

    private fun hideProgressLayout() {
        ll_progressLayout.visibility = View.GONE
        fab_search.visibility = View.VISIBLE
    }

    /*copied*/
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
            }

            override fun onFailure(call: Call<List<KotlinSpecialityModel>>?, t: Throwable?) {
                Log.e("ERR", t?.message + "  ")
            }
        })
    }

    /*copied*/
    private fun getGroupCount() {
        showProgressLayout()
        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
        val response = apiInterface.getGroupCount(provId, cityId)
        response.enqueue(object : Callback<List<KotlinGroupModel>> {
            override fun onResponse(call: Call<List<KotlinGroupModel>>?, response: Response<List<KotlinGroupModel>>?) {
                val list: List<KotlinGroupModel>? = response?.body()
                val realm = Realm.getDefaultInstance()
                realm.executeTransactionAsync { db: Realm? ->
                    db?.where(KotlinGroupModel::class.java)?.findAll()?.deleteAllFromRealm()
                    db?.copyToRealm(list!!)
                    /*val r = db?.where(KotlinGroupModel::class.java)?.findAll()
                    r?.forEach { model: KotlinGroupModel? ->
                        Log.e("GM", "${model?.name}:${model?.groupId}")
                    }*/
                }
                val c = list!!.size
                AllCentars.text = "$c مرکز "
                loadAdapter(list)
                hideProgressLayout()
                hideNetErrLayout()
            }

            override fun onFailure(call: Call<List<KotlinGroupModel>>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "خطا در اتصال به سرور", Toast.LENGTH_SHORT).show()
                hideProgressLayout()
                showNetErrLayout()
            }
        })
    }

    /*copied*/
    private fun loadAdapter(list: List<KotlinGroupModel>) {
        adapter = CategoryAdapter(list)
        rv_category.layoutManager = GridLayoutManager(this, 3)

        while (rv_category.itemDecorationCount > 0) {
            rv_category.removeItemDecorationAt(0)
        }

        val itemDecoration = TwoColGridDecoration(this, 6)
        rv_category.addItemDecoration(itemDecoration)
        rv_category.adapter = adapter
    }

    private fun deleteAdapter() {
        while (rv_category.itemDecorationCount > 0) {
            rv_category.removeItemDecorationAt(0)
        }
        rv_category.layoutManager = null
        rv_category.adapter = null
    }

    /*copied*/
    inner class CategoryAdapter(gList: List<KotlinGroupModel>) : RecyclerView.Adapter<CategoryAdapter.ItemHolder>() {

        var groupsList: List<KotlinGroupModel> = gList

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val view: View = LayoutInflater.from(this@MainActivity).inflate(R.layout.main_category_item, parent, false)
            return ItemHolder(view)
        }

        override fun getItemCount(): Int {
            rv_category.overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS
            return groupsList.size
        }


        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            if (groupsList.size > position) {
                holder.subTitle.text = "${groupsList.get(position).counter}  مرکز"
                holder.titleTv.text = groupsList.get(position).name
            } else {
                holder.subTitle.text = "0  مرکز"
            }
            setAnimation(holder.container)

            Glide.with(this@MainActivity).load(groupsList.get(position).g_url).into(holder.imageView)
        }


        private fun setAnimation(viewToAnimate: View) {
            var a = ObjectAnimator.ofFloat(viewToAnimate, "translationY", 200f, 0f)
            if (!scrollToBottom) {
                a = ObjectAnimator.ofFloat(viewToAnimate, "translationY", -200f, 0f)
            }
            val r = Random()
            val i1 = r.nextInt(200) + 50
            a.duration = i1.toLong()
            a.start()

        }


        inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, ServerStatusResponse {
            override fun isOk() {
                if (groupsList[adapterPosition].counter > 0) {
                    val i = Intent(this@MainActivity, OfficeActivity::class.java)
                    i.putExtra(StaticValues.CATEGORY, groupsList.get(adapterPosition).groupId)
                    i.putExtra(StaticValues.PROVID, provId)
                    i.putExtra(StaticValues.CITYID, cityId)
                    startActivity(i)
                } else {
                    Toast.makeText(this@MainActivity, "پایگاه داده ژین در حال تکمیل شدن اطلاعات است ...", Toast.LENGTH_SHORT).show()
                }
            }

            override fun notOk() {
                if (VarableValues.NetworkState) {
                    startActivity(Intent(this@MainActivity, ServerStatusActivity::class.java))
                    finish()
                } else {
                    showNetErrLayout()
                    Toast.makeText(this@MainActivity, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onClick(v: View?) {
                if (Utils.isNetworkAvailable(this@MainActivity)) {
                    //ss.IsOkServer()
                } else {
                    showNetErrLayout()
                    Toast.makeText(this@MainActivity, "به اینترنت متصل نیستید", Toast.LENGTH_SHORT).show()
                }
            }

            val imageView: AppCompatImageView = itemView.findViewById(R.id.iv_mainCategoryImage)
            val titleTv: AppCompatTextView = itemView.findViewById(R.id.tv_mainCategoryTitle)
            val subTitle: AppCompatTextView = itemView.findViewById(R.id.tv_mainCategorySubTitle)
            val container: CardView = itemView.findViewById(R.id.cv_mainCategoryContainer)
            val ss: ServerStatus

            init {
                container.setOnClickListener(this)
                ss = ServerStatus(this, this@MainActivity)
            }
        }

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            closeDrawerLayout()
        } else {
            showExitDialog()
            unregisterReceiver(Receiver)
        }
    }

}



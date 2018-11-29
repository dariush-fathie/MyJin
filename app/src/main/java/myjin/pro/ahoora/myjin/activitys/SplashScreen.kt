package myjin.pro.ahoora.myjin.activitys

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_splash.*
import myjin.pro.ahoora.myjin.BuildConfig
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.*
import myjin.pro.ahoora.myjin.utils.ApiInterface
import myjin.pro.ahoora.myjin.utils.KotlinApiClient
import myjin.pro.ahoora.myjin.utils.NetworkUtil
import myjin.pro.ahoora.myjin.utils.SharedPer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashScreen : AppCompatActivity(), View.OnClickListener {

    var versionCode = BuildConfig.VERSION_CODE
    val realm = Realm.getDefaultInstance()
    private var netAvailability: Boolean = false



    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_splashTryAgain -> tryAgain()
            R.id.btn_splashFav -> goToFav1()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        btn_splashTryAgain.setOnClickListener(this)
        btn_splashFav.setOnClickListener(this)
        netAvailability = NetworkUtil().isNetworkAvailable(this)
        clearGlideCache()

        if (netAvailability) {
            getQuesAnsewer()
            getConfig()
        } else {
            if (isThereAnyFav() && isThereAnyCity()) {
                showNetErrLayout(1)
            } else {
                showNetErrLayout(0)
            }
        }
        clearServices()
    }



    private fun showNetErrLayout(i: Int) {
        hideCpv()
        iv_splashImage.setImageResource(R.drawable.ic_wifi_not_connected)
        cl_netErrLayout.visibility = View.VISIBLE
        when (i) {
            0 -> {
                btn_splashFav.visibility = View.GONE
                btn_splashTryAgain.visibility = View.VISIBLE
                tv_splashContent.text = getString(R.string.checkConnectionAndTryAgain)
            }
            1 -> {
                btn_splashFav.visibility = View.VISIBLE
                btn_splashTryAgain.visibility = View.VISIBLE
                tv_splashContent.text = getString(R.string.doYouConnected)
            }
            2 -> {
                iv_splashImage.setImageResource(R.drawable.ic_repair)
                if (isThereAnyCity() && isThereAnyFav()) {
                    tv_splashContent.text = getString(R.string.gmdepakmbzemrbkhkmnshrb)
                } else {
                    // todo : change back to repair
                    tv_splashContent.text = getString(R.string.gmdepakmbzemrbkhk)
                }
            }
        }
    }


    private fun isThereAnyFav(): Boolean {
        realm.beginTransaction()
        val i = realm.where(KotlinItemModel::class.java).equalTo("saved", true).findAll().size
        realm.commitTransaction()
        return i != 0
    }

    private fun isThereAnyCity(): Boolean {
        realm.beginTransaction()
        val cityCount = realm.where(KotlinProvCityModel::class.java).findAll().size
        realm.commitTransaction()
        return cityCount != 0
    }

    private fun getCityCount(): Int {
        realm.beginTransaction()
        val cityCount = realm.where(KotlinProvCityModel::class.java).findAll().size
        realm.commitTransaction()
        return cityCount
    }

    var statusItem: KotlinAboutContactModel? = null

    private var configLock = false

    private fun getConfig() {
        if (!configLock) {
            configLock = true

            hideNetErrLayout()
            showCpv()

            val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
            val response = apiInterface.ac
            response.enqueue(object : Callback<KotlinAboutContactModel> {
                override fun onResponse(call: Call<KotlinAboutContactModel>?, response: Response<KotlinAboutContactModel>?) {
                    val result = response?.body()

                    result ?: onFailure(call, Throwable("null body"))
                    result ?: return

                    statusItem = response.body()

                    realm.executeTransactionAsync { db: Realm? ->
                        db?.where(KotlinAboutContactModel::class.java)?.findAll()?.deleteAllFromRealm()
                        db?.copyToRealm(result)
                    }

                    if (result.serverStatus == "ok") {
                        getProvinceAndCitiesList()
                    } else {
                        showNetErrLayout(2)
                    }
                    configLock = false

                }

                override fun onFailure(call: Call<KotlinAboutContactModel>?, t: Throwable?) {
                    hideCpv()
                    configLock = false

                    if (!isThereAnyCity()) {
                        showNetErrLayout(0)
                        // todo try without fav
                    } else {
                        showNetErrLayout(1)
                    }

                }
            })
        }
    }


    private fun getQuesAnsewer(){
        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
       val response = apiInterface.quesAndAnswer
        response.enqueue(object : Callback<List<KotlinAdviceModel>> {
            override fun onResponse(call: Call<List<KotlinAdviceModel>>?, response: Response<List<KotlinAdviceModel>>?) {
                val list: List<KotlinAdviceModel>? = response?.body()

                realm.executeTransactionAsync { db: Realm? ->
                    db?.where(KotlinAdviceModel::class.java)?.findAll()?.deleteAllFromRealm()
                    db?.copyToRealm(list!!)


                }
            }

            override fun onFailure(call: Call<List<KotlinAdviceModel>>?, t: Throwable?) {
                Toast.makeText(this@SplashScreen, "خطا در اتصال به سرور", Toast.LENGTH_SHORT).show()

            }
        })
    }


    private var lock = false
    private fun getProvinceAndCitiesList() {
        if (!lock) {
            lock = true

            hideNetErrLayout()
            showCpv()

            val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
            val response = apiInterface.getProvinceAndCitiesList(getCityCount())
            response.enqueue(object : Callback<List<KotlinProvCityModel>> {
                override fun onResponse(call: Call<List<KotlinProvCityModel>>?, response: Response<List<KotlinProvCityModel>>?) {
                    response?.body() ?: onFailure(call, Throwable("null body"))
                    response?.body() ?: return

                    val list: List<KotlinProvCityModel>? = response.body()
                    if (list != null) {
                        if (list.isNotEmpty()) {
                            realm.executeTransactionAsync { db: Realm? ->
                                db?.where(KotlinProvCityModel::class.java)?.findAll()?.deleteAllFromRealm()
                                db?.copyToRealm(list)
                            }
                        }
                    } else {
                        onFailure(call, Throwable("null city list"))
                        return
                    }

                    hideNetErrLayout()
                    lock = false

                    if (statusItem?.version!! > versionCode) {
                        newVersion(statusItem?.version!!.toString(), statusItem?.force!!)
                    } else {
                        if (!SharedPer(this@SplashScreen).getBoolean(getString(R.string.introductionFlag))) {
                            hideCpv()
                            gotoIntro()

                        } else {

                            Handler().postDelayed({
                                hideCpv()
                                gotoHome1()
                            }, 1500)
                        }
                    }
                }

                override fun onFailure(call: Call<List<KotlinProvCityModel>>?, t: Throwable?) {
                    hideCpv()
                    showNetErrLayout(0)
                    lock = false
                }
            })

        }
    }

    private fun tryAgain() {
        netAvailability = NetworkUtil().isNetworkAvailable(this)
        if (netAvailability) {
            hideNetErrLayout()
            showCpv()
            getConfig()
            getQuesAnsewer()
        } else {
            hideCpv()
            if (isThereAnyFav() && isThereAnyCity()) {
                showNetErrLayout(1)
            } else {
                showNetErrLayout(0)
            }
        }
    }


    private fun hideNetErrLayout() {
        cl_netErrLayout.visibility = View.GONE
    }

    private fun showCpv() {
        rl_splash.visibility = View.VISIBLE
    }

    private fun hideCpv() {
        rl_splash.visibility = View.GONE
    }

    private fun clearGlideCache() {
        Handler().postDelayed(Runnable { Glide.get(this@SplashScreen).clearMemory() }, 0)
        AsyncTask.execute(Runnable { Glide.get(this@SplashScreen).clearDiskCache() })
    }

    private fun clearServices() {
        realm.executeTransaction { db ->
            db.where(KotlinServicesModel::class.java).findAll().deleteAllFromRealm()
        }
    }

    private fun newVersion(v: String, force: Int) {
        val tvCustomTitle: TextView
        val view2 = LayoutInflater.from(this).inflate(R.layout.hea_inc_services_title, null, false)
        tvCustomTitle = view2.findViewById(R.id.tv_customTitle)
        tvCustomTitle.text = "نسخه جدید ژین من آماده است (نسخه $v)"
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setCustomTitle(view2)
        builder.setMessage(R.string.hbnjbrvsh)

        builder.setPositiveButton(R.string.dnj) { _, _ -> update() }
        builder.setNeutralButton("خروج از برنامه") { _, _ ->
            finish()
        }
        if (force != 1) {
            builder.setNegativeButton(R.string.continue_) { _, _ ->
                if (!SharedPer(this@SplashScreen).getBoolean(getString(R.string.introductionFlag))) {
                    hideCpv()
                    gotoIntro()
                } else {
                    Handler().postDelayed({
                        hideCpv()
                        gotoHome1()
                    }, 1500)
                }
            }
            builder.setMessage(R.string.addarabdk)
        }
        builder.show()
    }

    fun update() {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("bazaar://details?id=" + BuildConfig.APPLICATION_ID)
            intent.setPackage("com.farsitel.bazaar")
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "خطا در اتصال به بازار", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun gotoHome1() {

        startActivity(Intent(this, MainActivity2::class.java))
        finish()
    }

    private fun goToFav1() {
        startActivity(Intent(this, FavActivity::class.java))
        finish()
    }

    private fun gotoIntro() {
        startActivity(Intent(this, AppIntro::class.java))
        finish()


    }
}

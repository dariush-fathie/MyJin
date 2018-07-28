package myjin.pro.ahoora.myjin.activitys

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.net_err_layout_start.*
import myjin.pro.ahoora.myjin.BuildConfig
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.interfaces.ServerStatusResponse
import myjin.pro.ahoora.myjin.models.KotlinAboutContactModel
import myjin.pro.ahoora.myjin.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StartActivity : AppCompatActivity(), View.OnClickListener, ServerStatusResponse {

    private lateinit var serverStatus: ServerStatus
    private var isFirstStart: Boolean = false
    var versionCode = BuildConfig.VERSION_CODE

    override fun isOk() {
        init_()
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

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_fav -> {
                startActivity(Intent(this@StartActivity, FavActivity::class.java))
            }
            R.id.btn_tryAgain -> serverStatus.checkServer()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        serverStatus = ServerStatus(this, this)
        serverStatus.checkServer()

        btn_tryAgain.setOnClickListener(this)
        btn_fav.setOnClickListener(this)

    }

    private fun newVersion(v: String, force: Int) {
        val tvCustomTitle: TextView
        val view2 = LayoutInflater.from(this@StartActivity).inflate(R.layout.hea_inc_services_title, null, false)
        tvCustomTitle = view2.findViewById(R.id.tv_customTitle)
        tvCustomTitle.text = "نسخه جدید ژین من آماده است (نسخه $v)"
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@StartActivity)
        builder.setCancelable(false)
        builder.setCustomTitle(view2)
        builder.setMessage(R.string.hbnjbrvsh)

        builder.setPositiveButton(R.string.dnj) { _, _ -> update() }
        if (force != 1) {
            builder.setNegativeButton(R.string.continue_) { _, _ -> notNewVersion() }
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
            Toast.makeText(this@StartActivity, "خطا در اتصال به بازار", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun notNewVersion() {
        val getSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(baseContext)

        isFirstStart = getSharedPreferences.getBoolean("firstStart", true)
        VarableValues.first = !isFirstStart

        val i = Intent(this@StartActivity, MainActivity::class.java)
        startActivity(i)
        overridePendingTransition(R.anim.in_, R.anim.out)
        finish()

    }

    private fun init_() {
        if (Utils.isNetworkAvailable(this@StartActivity)) {
            hideNetErrLayout()
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

                    if (list?.version!! > versionCode) {
                        newVersion(list.version!!.toString(), list.force!!)
                    } else {
                        notNewVersion()
                    }
                }

                override fun onFailure(call: Call<KotlinAboutContactModel>?, t: Throwable?) {
                    showNetErrLayout()
                }
            })
        } else {
            showNetErrLayout()
        }
    }

    private fun showNetErrLayout() {
        ll_netErr_start.visibility = View.VISIBLE
    }

    private fun hideNetErrLayout() {
        ll_netErr_start.visibility = View.GONE
    }

}

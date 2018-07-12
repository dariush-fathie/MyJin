package myjin.pro.ahoora.myjin.activitys

import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import myjin.pro.ahoora.myjin.BuildConfig
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.models.KotlinAboutContactModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.DialogInterface
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.net_err_layout_start.*
import myjin.pro.ahoora.myjin.interfaces.TempListener
import myjin.pro.ahoora.myjin.models.KotlinSlideMainModel
import myjin.pro.ahoora.myjin.utils.*


class StartActivity : AppCompatActivity(), View.OnClickListener, TempListener {
    var serverStatus: ServerStatus? = null
    private var isFirstStart: Boolean = false
    internal var versionCode = BuildConfig.VERSION_CODE

    override fun IsOk() {
        init_()
    }

    override fun IsNotOk() {
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
            R.id.btn_tryAgain -> serverStatus?.IsOkServer()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        serverStatus = ServerStatus(this, this@StartActivity)
        serverStatus?.IsOkServer()

        btn_tryAgain.setOnClickListener(this)
        btn_fav.setOnClickListener(this)

    }

    private fun newVersion(v: String, force: Int) {

        var tv_costom_title: TextView
        val view2 = LayoutInflater.from(this@StartActivity).inflate(R.layout.hea_inc_services_title, null, false)
        tv_costom_title = view2.findViewById(R.id.tv_customTitle)
        tv_costom_title.setText("نسخه $v موجود است : ")
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this@StartActivity)
        builder.setCancelable(false)
        builder.setCustomTitle(view2)
        builder.setMessage(R.string.hbnjbrvsh)

        builder.setPositiveButton(R.string.dnj, DialogInterface.OnClickListener { dialogInterface, i -> update() })
        if (force != 1) {
            builder.setNegativeButton(R.string.continue_, DialogInterface.OnClickListener { dialogInterface, i -> notNewVersion() })
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

    private fun showNetErrLayout() {
        ll_netErr_start.visibility = View.VISIBLE
    }

    private fun hideNetErrLayout() {
        ll_netErr_start.visibility = View.GONE
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
                        newVersion(list?.version!!.toString(), list?.force!!)
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
}

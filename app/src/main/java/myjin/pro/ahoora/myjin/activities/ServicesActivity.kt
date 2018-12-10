package myjin.pro.ahoora.myjin.activities

import android.content.Context
import android.os.Bundle
import android.os.Handler
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_services.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.ServicesAdapter
import myjin.pro.ahoora.myjin.interfaces.GetServicesList
import myjin.pro.ahoora.myjin.models.KotlinServicesModel
import myjin.pro.ahoora.myjin.utils.ApiInterface
import myjin.pro.ahoora.myjin.utils.KotlinApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServicesActivity : AppCompatActivity(), View.OnClickListener, GetServicesList {

    var groupId = 1
    var serviceList: ServiceList? = null

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_goback -> onBackPressed()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)
        if (intent != null) {
            tv_ServicesTitle.text = intent.getStringExtra("ServiceTitle")
            groupId = intent.getIntExtra("groupId", 1)
        }
        onClick()

        serviceList = ServiceList(this@ServicesActivity, this)
    }

    private fun onClick() {
        iv_goback.setOnClickListener(this)
    }

    private fun getServicesTitle() {
        rv_services.layoutManager = LinearLayoutManager(this@ServicesActivity)
        rv_services.adapter = ServicesAdapter(this@ServicesActivity, groupId)
    }

    override fun getSL() {
        getServicesTitle()
    }

    override fun error() {
        Snackbar.make(rv_services, "خطایی رخ داد دوباره امتحان کنید", Snackbar.LENGTH_INDEFINITE)
                .setAction("تلاش دوباره") {
                    Handler().postDelayed({
                        serviceList = ServiceList(this@ServicesActivity, this)
                    }, 1000)
                }.show()
    }


    private fun cancelResponse() {
        if (this::response.isInitialized) {
            if (!response.isCanceled) {
                response.cancel()
            }
        }
    }


    override fun onStop() {
        cancelResponse()
        super.onStop()
    }

    lateinit var response: Call<List<KotlinServicesModel>>

    inner class ServiceList(internal var context: Context, internal var gsl: GetServicesList) {
        val realm = Realm.getDefaultInstance()

        init {
            realm.beginTransaction()
            val res = realm.where(KotlinServicesModel::class.java).findAll().size
            realm.commitTransaction()

            if (res == 0) {
                val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
                response = apiInterface.servicesList
                response.enqueue(object : Callback<List<KotlinServicesModel>> {
                    override fun onResponse(call: Call<List<KotlinServicesModel>>?, response: Response<List<KotlinServicesModel>>?) {
                        val list: List<KotlinServicesModel>? = response?.body()

                        realm.executeTransactionAsync { db: Realm? ->
                            db?.where(KotlinServicesModel::class.java)?.findAll()?.deleteAllFromRealm()
                            db?.copyToRealm(list!!)
                            runOnUiThread {
                                gsl.getSL()
                            }

                        }
                    }

                    override fun onFailure(call: Call<List<KotlinServicesModel>>?, t: Throwable?) {
                        Toast.makeText(context, "خطا در اتصال به سرور", Toast.LENGTH_SHORT).show()
                        gsl.error()
                    }
                })
            } else {
                gsl.getSL()
            }
        }
    }

}

package myjin.pro.ahoora.myjin.activitys

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.TextViewCompat
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.content_scrolling_layout.*

import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.customClasses.CustomToast
import myjin.pro.ahoora.myjin.models.KotlinSignInModel
import myjin.pro.ahoora.myjin.models.TempModel
import myjin.pro.ahoora.myjin.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity(), View.OnClickListener {


    private var number = ""
    val realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        try {

            number = intent.getStringExtra("number")
            rl_netErrText.visibility = View.VISIBLE
            tryAgain()

        } catch (e: IllegalStateException) {
            realm.executeTransactionAsync { db ->
                val u = db.where(KotlinSignInModel::class.java).findFirst()
                setValues(u)
                (fab_camera as View).visibility = View.VISIBLE
            }
        }

        initListener()
    }

    private fun initListener() {
        btn_tryAgain.setOnClickListener(this)
        iv_menu.setOnClickListener(this)
        iv_goback.setOnClickListener(this)
    }

    fun hideMainProgressLayout() {
        rl_progressMain.visibility = View.GONE
        (fab_camera as View).visibility = View.VISIBLE
    }

    fun showMainProgressLayout() {
        rl_progressMain.visibility = View.VISIBLE
        (fab_camera as View).visibility = View.GONE
    }

    private fun showErrLayout() {
        rl_netErrText.visibility = View.VISIBLE
        (fab_camera as View).visibility = View.GONE
    }

    private fun hideErrLayout() {
        rl_netErrText.visibility = View.GONE
        (fab_camera as View).visibility = View.VISIBLE
    }

    private fun tryAgain() {
        hideErrLayout()
        if (NetworkUtil().isNetworkAvailable(this)) {
            getUserInfos()
        } else {
            showErrLayout()
        }
    }

    private fun getUserInfos() {

        hideErrLayout()
        showMainProgressLayout()

        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
        apiInterface.getUserInfo(number).enqueue(object : Callback<List<KotlinSignInModel>> {

            override fun onResponse(call: Call<List<KotlinSignInModel>>?, response: Response<List<KotlinSignInModel>>?) {
                val list = response?.body()

                hideMainProgressLayout()

                val u = list?.get(0)

                setValues(u)


                realm.executeTransactionAsync { realm: Realm? ->

                    realm?.where(KotlinSignInModel::class.java)?.findAll()?.deleteAllFromRealm()
                    realm?.copyToRealm(list!!)


                    runOnUiThread {

                        hideMainProgressLayout()

                    }
                }
            }

            override fun onFailure(call: Call<List<KotlinSignInModel>>?, t: Throwable?) {
                Log.e("ERR", t?.message + "  ")
                showErrLayout()
                hideMainProgressLayout()
            }
        })

    }


    @SuppressLint("SetTextI18n")
    private fun setValues(u: KotlinSignInModel?) {
        tv_tel_val.text = u?.phoneNumber
        number = u?.phoneNumber.toString()
        avatar_title.text = "${u?.firstName} ${u?.lastName}"
    }


    private fun openPopupMenu() {
        val popupMenu = PopupMenu(this@ProfileActivity, iv_menu)
        popupMenu.menuInflater.inflate(R.menu.menu3, popupMenu.getMenu())
        popupMenu.setOnMenuItemClickListener { i ->
            when (i.itemId) {
                R.id.etName -> startActivity(Intent(this, Login2Activity::class.java))
                R.id.logNut -> {

                    if (NetworkUtil().isNetworkAvailable(this)) {
                        LogOut()
                    } else {
                        CustomToast().Show_Toast(this@ProfileActivity, cl_parent,
                                getString(R.string.checkYourConnection))
                    }
                }


            }


            true
        }
        popupMenu.show()
    }

    private fun LogOut() {

        showMainProgressLayout()
        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
        Utils.setYekta()
        val yekta = VarableValues.yekta

        apiInterface.signIn(number, "f", "l", "pr", "0", "0",yekta).enqueue(object : Callback<TempModel> {
            override fun onResponse(call: Call<TempModel>, response: Response<TempModel>) {

                hideMainProgressLayout()

                if (response.isSuccessful) {
                    val `val`: String
                    val tempModel = response.body()!!
                    `val` = tempModel.getVal()

                    when (`val`) {

                        "U" -> {

                            realm.executeTransactionAsync { realm: Realm? ->

                                realm?.where(KotlinSignInModel::class.java)?.findAll()?.deleteAllFromRealm()

                                runOnUiThread {

                                    finish()

                                }
                            }
                        }
                        "no" -> {
                            CustomToast().Show_Toast(this@ProfileActivity, cl_parent,
                                    getString(R.string.vbkhmsh))
                        }
                        "empty" -> {
                            CustomToast().Show_Toast(this@ProfileActivity, cl_parent,
                                    getString(R.string.ltmkhshesh))
                        }
                    }

                }
            }

            override fun onFailure(call: Call<TempModel>, t: Throwable) {
                hideMainProgressLayout()
                Toast.makeText(this@ProfileActivity, R.string.khrda, Toast.LENGTH_SHORT).show()
            }
        })


    }



   /* private fun showExitDialog() {

        val builder = AlertDialog.Builder(this@ProfileActivity)
        val dialog: AlertDialog
        val view = View.inflate(this@ProfileActivity, R.layout.yourname_layout, null)
        val tv_continue: TextViewCompat = view.findViewById(R.id.btn_ok)
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
    }*/

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_tryAgain -> tryAgain()
            R.id.iv_menu -> openPopupMenu()
            R.id.iv_goback -> onBackPressed()
        }
    }

}

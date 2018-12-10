package myjin.pro.ahoora.myjin.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.button.MaterialButton
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_profile.*

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
    private var fn = ""
    private var ln = ""

    val realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        try {

            number = intent.getStringExtra("number")
            cv_netErrText.visibility = View.VISIBLE

            tryAgain()

        } catch (e: IllegalStateException) {
            realm.executeTransactionAsync { db ->
                val u = db.where(KotlinSignInModel::class.java).findFirst()
                setValues(u)

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
        cv_progressMain.visibility = View.GONE
    }

    fun showMainProgressLayout() {
        cv_progressMain.visibility = View.VISIBLE
    }

    private fun showErrLayout() {
        cv_netErrText.visibility = View.VISIBLE
    }

    private fun hideErrLayout() {
        cv_netErrText.visibility = View.GONE
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
        avatar_title.text = u?.firstName.toString() + " " + u?.lastName
    }


    private fun openPopupMenu() {
        val popupMenu = PopupMenu(this@ProfileActivity, iv_menu)
        popupMenu.menuInflater.inflate(R.menu.menu3, popupMenu.getMenu())
        popupMenu.setOnMenuItemClickListener { i ->
            when (i.itemId) {
                R.id.etName -> showEditNameDialog()
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

        apiInterface.signIn(number, "f", "l", "pr", "0", "0", yekta).enqueue(object : Callback<TempModel> {
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


    private fun showEditNameDialog() {

        val builder = AlertDialog.Builder(this@ProfileActivity)
        val dialog: AlertDialog
        val view = View.inflate(this@ProfileActivity, R.layout.yourname_dialog_layout, null)
        val tvContinue: MaterialButton = view.findViewById(R.id.tv_continue)
        val etFn: AppCompatEditText = view.findViewById(R.id.et_fn)
        val etLn: AppCompatEditText = view.findViewById(R.id.et_ln)

        builder.setView(view)
        dialog = builder.create()
        dialog.show()
        val listener = View.OnClickListener { v ->
            when (v.id) {
                R.id.tv_continue -> {
                    dialog.dismiss()

                    if (etFn.text != null && etLn.text != null) {
                        if (etFn.text!!.toString().trim { it <= ' ' } != "" && etLn.text!!.toString().trim { it <= ' ' } != "") {
                            fn = etFn.text!!.toString().trim { it <= ' ' }
                            ln = etLn.text!!.toString().trim { it <= ' ' }

                            if (NetworkUtil().isNetworkAvailable(this@ProfileActivity)) {
                                updateEditName()
                            } else {
                                CustomToast().Show_Toast(this@ProfileActivity, cl_parent,
                                        getString(R.string.checkYourConnection))
                            }

                        } else {
                            CustomToast().Show_Toast(this@ProfileActivity, cl_parent,
                                    getString(R.string.tmpsh))
                        }
                    }
                }

            }
        }
        tvContinue.setOnClickListener(listener)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_tryAgain -> tryAgain()
            R.id.iv_menu -> openPopupMenu()
            R.id.iv_goback -> onBackPressed()
            R.id.fab_camera-> getPhotoForAvatar()
        }
    }

    private fun getPhotoForAvatar() {

    }

    private fun updateEditName() {
        showMainProgressLayout()

        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
        Utils.setYekta()
        val yekta = VarableValues.yekta

        apiInterface.signIn(number, fn, ln, "pr", "1", "1", yekta).enqueue(object : Callback<TempModel> {
            override fun onResponse(call: Call<TempModel>, response: Response<TempModel>) {
                hideMainProgressLayout()
                if (response.isSuccessful) {
                    val `val`: String
                    val tempModel = response.body()!!
                    `val` = tempModel.getVal()

                    when (`val`) {

                        "U" -> {
                            realm.beginTransaction()
                            val kmdm = realm.where(KotlinSignInModel::class.java).findFirst()

                            kmdm?.firstName = fn
                            kmdm?.lastName = ln

                            setValues(kmdm)
                            realm.commitTransaction()


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

}

package myjin.pro.ahoora.myjin.activitys

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_spec.*
import kotlinx.android.synthetic.main.drawer_layout.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.SpecAdapter
import myjin.pro.ahoora.myjin.adapters.TAdapter
import myjin.pro.ahoora.myjin.customClasses.CustomToast
import myjin.pro.ahoora.myjin.customClasses.ThreeColGridDecorationSpec
import myjin.pro.ahoora.myjin.models.KotlinAboutContactModel
import myjin.pro.ahoora.myjin.models.KotlinSignInModel
import myjin.pro.ahoora.myjin.utils.SharedPer
import myjin.pro.ahoora.myjin.utils.StaticValues

class SpecActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {
    private var signIn = false
    private var provId = 19
    private var cityId = 0
    var filterArray = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spec)

        if (intent != null) {
            provId = intent.getIntExtra(StaticValues.PROVID, 19)
            cityId = intent.getIntExtra(StaticValues.CITYID, 1)

        }

        isLogin()
        initi()
    }

    override fun onResume() {
        super.onResume()
        isLogin()

     setFilterColor()
    }

    fun setFilterColor(){
        val draw = ContextCompat.getDrawable(this@SpecActivity, R.drawable.ic_filter_test)
        draw?.setColorFilter(ContextCompat.getColor(this@SpecActivity, R.color.green), PorterDuff.Mode.SRC_IN)
        if(filterArray.size>0){
            draw?.setColorFilter(ContextCompat.getColor(this@SpecActivity, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        }
        iv_filter.setImageDrawable(draw)
    }

    private fun isLogin() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val u = realm.where(KotlinSignInModel::class.java).findFirst()

        if (u != null) {
            signIn = true
            tv_login_outsign.text = "خوش آمدید " + u.firstName + " عزیز "
            SharedPer(this).setBoolean("signIn", signIn)
            if (u.allow == "1") {
                showViews()
            } else {
                hideViews()
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

    private fun openDrawerLayout() {
        drawerLayout.openDrawer(GravityCompat.END)
    }

    private fun initListener() {
        rl_filter.setOnClickListener(this)
        iv_goback.setOnClickListener(this)
        iv_menu.setOnClickListener(this)
        rl_drawer3.setOnClickListener(this)
        rl_drawer4.setOnClickListener(this)
        rl_salamat.setOnClickListener(this)
        rl_myjin_services.setOnClickListener(this)
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
        rl_setting.setOnClickListener(this)
        tv_healthCenters.setOnClickListener(this)
        tv_messages.setOnClickListener(this)
        rl_rules.setOnClickListener(this)
        rl_exit.setOnClickListener(this)
        rl_notifi.setOnClickListener(this)
        rl_onlineContact.setOnClickListener(this)
        rl_rate.setOnClickListener(this)
        rl_share.setOnClickListener(this)
    }
    private fun share() {
        var str = "لینک دانلود اپ ژین من : "
        str += "\n\n"
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val res = realm.where(KotlinAboutContactModel::class.java).findFirst()
        realm.commitTransaction()

        str+=res?.tKafeh!!
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, str)

        startActivity(Intent.createChooser(shareIntent, "Share via"))


    }
    private fun drawerClick(position: Int) {

        when (position) {

            1 -> {
                startActivity(Intent(this@SpecActivity, FavActivity::class.java))

            }
            2 -> {
                startActivity(Intent(this@SpecActivity, AboutUs::class.java))

            }
            3 -> {
                startActivity(Intent(this@SpecActivity, ContactUs::class.java))

            }

        }


    }

    private fun goToServicesActivity(title: String, i: Int) {
        val intentO = Intent(this@SpecActivity, ServicesActivity::class.java)
        intentO.putExtra("ServiceTitle", title)
        intentO.putExtra("groupId", i)
        startActivity(intentO)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_login_outsign -> {
                /*Toast.makeText(this@SpecActivity,
                        getString(R.string.early), Toast.LENGTH_SHORT).show()*/
                if (signIn) {
                    startActivity(Intent(this, ProfileActivity::class.java))
                } else {
                    startActivity(Intent(this, Login2Activity::class.java))
                }
            }
            R.id.rl_filter -> onFilterClick()
            R.id.iv_goback -> onBackPressed()
            R.id.iv_menu -> openDrawerLayout()
            R.id.rl_exit -> showExitDialog()
            R.id.tv_healthCenters -> drawerClick(1)
            R.id.rl_drawer3 -> drawerClick(2)
            R.id.rl_drawer4 -> drawerClick(3)

            R.id.rl_myjin_services -> goToServicesActivity(getString(R.string.khj), 1)
            R.id.rl_takapoo_services -> goToServicesActivity(getString(R.string.mnvfs), 2)
            R.id.rl_university_services -> goToServicesActivity(tv_university_services_Title1.text.toString(), 3)
            R.id.rl_salamat -> goToServicesActivity(tv_drawerTitlesalamat.text.toString(), 4)
            R.id.rl_tamin_services -> goToServicesActivity(tv_tamin_services.text.toString(), 5)
            R.id.rl_ict_services -> goToServicesActivity(tv_ict_services.text.toString(), 6)
            R.id.rl_pishkhan_services -> goToServicesActivity(tv_pishkhan_services.text.toString(), 7)
            R.id.rl_post_services -> goToServicesActivity(tv_post_services.text.toString(), 8)
            R.id.rl_setting -> startActivity(Intent(this, SettingActivity::class.java))
            R.id.tv_messages -> startActivity(Intent(this, FavMessageActivity::class.java))
            R.id.rl_notifi -> startActivity(Intent(this, NotificationActivity::class.java))
            R.id.rl_rules -> startActivity(Intent(this, RulesActivity::class.java))
            R.id.rl_onlineContact -> startActivity(Intent(this, ScripeChat::class.java))
            R.id.rl_share -> share()
            R.id.rl_rate -> rate()

        }
    }
    private fun rate() {

        val isInstalled = isPackageInstalled(applicationContext, "com.farsitel.bazaar")

        if (isInstalled){
            val intent = Intent(Intent.ACTION_EDIT)
            intent.data = Uri.parse("bazaar://details?id=${ContactsContract.Directory.PACKAGE_NAME}")
            intent.setPackage("com.farsitel.bazaar")
            startActivity(intent)
        }else{
            CustomToast().Show_Toast(this, drawerLayout,
                    getString(R.string.appbrnk))
        }

    }

    fun isPackageInstalled(context: Context, packageName: String): Boolean {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }

    }
    
    private fun onFilterClick() {
        val builder = AlertDialog.Builder(this@SpecActivity)
        val dialog: AlertDialog
        val view = View.inflate(this@SpecActivity, R.layout.filter, null)

        val tvAction: MaterialButton = view.findViewById(R.id.mbtn_action)
        val tvClose: MaterialButton = view.findViewById(R.id.mbtn_close)
        val tvClearSlecteds: MaterialButton = view.findViewById(R.id.mbtn_clearSlecteds)
        val tList: RecyclerView = view.findViewById(R.id.rv_tList)

        val adapter = TAdapter(this@SpecActivity, filterArray)
        tList.layoutManager = RtlGridLayoutManager(this@SpecActivity, 3)
        tList.adapter = adapter


        builder.setView(view)
        dialog = builder.create()
        dialog.show()

        val listener = View.OnClickListener { v ->
            when (v.id) {
                R.id.mbtn_action -> {
                    dialog.dismiss()
                    filterArray = adapter.idsArray
                    filter1(filterArray)
                }
                R.id.mbtn_close -> {
                    dialog.dismiss()
                }
                R.id.mbtn_clearSlecteds -> {
                    adapter.clearSelections()
                    filterArray.clear()
                    setFilterColor()
                }
            }
        }
        tvAction.setOnClickListener(listener)
        tvClose.setOnClickListener(listener)
        tvClearSlecteds.setOnClickListener(listener)
    }
    
    override fun onLongClick(v: View?): Boolean {
        when (v?.id) {
            R.id.iv_jinDrawer -> {
                startActivity(Intent(this@SpecActivity, LoginActivity::class.java))

                finish()
                return true
            }
        }
        return false
    }

    private fun closeDrawerLayout() {
        drawerLayout.closeDrawers()
    }

    private fun initi() {
        val adapter = SpecAdapter(this@SpecActivity, provId, cityId)
        rv_spec.layoutManager = RtlGridLayoutManager(this@SpecActivity, 3)
        rv_spec.adapter = adapter

        while (rv_spec.itemDecorationCount > 0) {
            rv_spec.removeItemDecorationAt(0)
        }

        val itemDecoration = ThreeColGridDecorationSpec(this@SpecActivity, 8)
        rv_spec.addItemDecoration(itemDecoration)

        initListener()
    }



    private lateinit var adapter: TAdapter


    private fun filter1(array: ArrayList<Int>) {
        val k = Intent(this@SpecActivity, OfficeActivity::class.java)

        k.putExtra(StaticValues.CATEGORY, 1)
        k.putExtra(StaticValues.PROVID, provId)
        k.putExtra(StaticValues.CITYID, cityId)
        k.putIntegerArrayListExtra("spArray", array)
        startActivity(k)
    }

    private fun showExitDialog() {

        val builder = AlertDialog.Builder(this@SpecActivity)
        val dialog: AlertDialog
        val view = View.inflate(this@SpecActivity, R.layout.exit_layout, null)
        val btnOk: AppCompatButton = view.findViewById(R.id.btn_ok)
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
    }

    override fun onBackPressed() {
            super.onBackPressed()
    }

    inner class RtlGridLayoutManager : GridLayoutManager {

        constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

        constructor(context: Context, spanCount: Int) : super(context, spanCount) {}

        constructor(context: Context, spanCount: Int, orientation: Int, reverseLayout: Boolean) : super(context, spanCount, orientation, reverseLayout) {}

        override fun isLayoutRTL(): Boolean {
            return true
        }
    }
}

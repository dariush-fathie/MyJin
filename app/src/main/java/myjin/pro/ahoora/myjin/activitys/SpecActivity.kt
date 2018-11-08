package myjin.pro.ahoora.myjin.activitys

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_spec.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.filter.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.SpecAdapter
import myjin.pro.ahoora.myjin.adapters.TAdapter
import myjin.pro.ahoora.myjin.customClasses.CustomBottomSheetBehavior
import myjin.pro.ahoora.myjin.customClasses.ThreeColGridDecorationSpec
import myjin.pro.ahoora.myjin.models.KotlinSignInModel
import myjin.pro.ahoora.myjin.utils.SharedPer
import myjin.pro.ahoora.myjin.utils.StaticValues

class SpecActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {
    private var signIn = false
    private var provId = 19
    private var cityId = 0
    var filterArray = ArrayList<Int>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>
    private lateinit var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback
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
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
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
        mbtn_action.setOnClickListener(this)
        mbtn_close.setOnClickListener(this)
        mbtn_clearSlecteds.setOnClickListener(this)
        toolbar.setOnClickListener(this)
        tv_title_filter.setOnClickListener(this)
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
            R.id.mbtn_action -> {
                filterArray = adapter.idsArray
                filter1(filterArray)
            }
            R.id.mbtn_close -> {
                closeFilterSheet()
            }
            R.id.mbtn_clearSlecteds -> {
                adapter.clearSelections()
            }
            R.id.tv_login_outsign -> {
                /*Toast.makeText(this@OfficeActivity,
                        getString(R.string.early), Toast.LENGTH_SHORT).show()*/
                if (signIn) {
                    startActivity(Intent(this, ProfileActivity::class.java))
                } else {
                    startActivity(Intent(this, Login2Activity::class.java))
                }
            }
            R.id.tv_title_filter -> openFilterSheet()
            R.id.toolbar -> openFilterSheet()
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

        }
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
        initBottomSheet()
    }

    private fun initBottomSheet() {


        bottomSheetBehavior = BottomSheetBehavior.from(cv_filter)
        if (bottomSheetBehavior is CustomBottomSheetBehavior) {
            (bottomSheetBehavior as CustomBottomSheetBehavior).setAllowUserDragging(false)
        }
       // bottomSheetBehavior.setBottomSheetCallback(getBottomSheetCallback())


        Thread {
            try {
                while (rv_tList.itemDecorationCount > 0) {
                    rv_tList.removeItemDecorationAt(0)
                }
                adapter = TAdapter(this@SpecActivity, filterArray)
                rv_tList.layoutManager = RtlGridLayoutManager(this@SpecActivity, 3)
                rv_tList.adapter = adapter
                val itemDecoration = ThreeColGridDecorationSpec(this@SpecActivity, 8)
                rv_tList.addItemDecoration(itemDecoration)
            } finally {

            }
        }.start()


    }

    private lateinit var adapter: TAdapter

/*    private fun getBottomSheetCallback(): BottomSheetBehavior.BottomSheetCallback {
        bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }
        }
        return bottomSheetCallback
    }*/

    private fun closeFilterSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun openFilterSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }


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
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {

            super.onBackPressed()
        }
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

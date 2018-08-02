package myjin.pro.ahoora.myjin.activitys


import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_hea.*
import kotlinx.android.synthetic.main.drawer_layout.*

import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.HeaIncAdapter

class HeaIncServiceActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {

    internal lateinit var adapter: HeaIncAdapter
    internal lateinit var layoutManger: LinearLayoutManager
    internal lateinit var rv_hea_inc: RecyclerView
    private var iv_goback: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hea)

        ABC()
        init()
    }


    private fun init() {
        adapter = HeaIncAdapter(this@HeaIncServiceActivity)
        layoutManger = LinearLayoutManager(applicationContext)
        rv_hea_inc.layoutManager = layoutManger
        rv_hea_inc.adapter = adapter
        iv_goback!!.setOnClickListener(this)
        iv_menu.setOnClickListener(this)
        rl_myjin_services.setOnClickListener(this)

        rl_drawer3.setOnClickListener(this)
        rl_drawer4.setOnClickListener(this)
        rl_salamat.setOnClickListener(this)
        iv_jinDrawer.setOnLongClickListener(this)

    }

    private fun ABC() {
        rv_hea_inc = findViewById(R.id.rv_hea_inc)
        iv_goback = findViewById(R.id.iv_goback)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_menu -> openDrawerLayout()
            R.id.iv_goback -> finish()
            R.id.rl_myjin_services -> drawerClick(0)

            R.id.rl_drawer3 -> drawerClick(2)
            R.id.rl_drawer4 -> drawerClick(3)
            R.id.rl_salamat -> drawerClick(4)
        }
    }

    fun early_Mth() {
        Toast.makeText(this, "بزودی", Toast.LENGTH_LONG).show()
    }

    override fun onLongClick(v: View?): Boolean {
        when (v?.id) {
            R.id.iv_jinDrawer -> {
                startActivity(Intent(this@HeaIncServiceActivity, LoginActivity::class.java))

                finish()
                return true
            }
        }
        return false
    }

    private fun drawerClick(position: Int) {
        when (position) {
            0 -> {
                startActivity(Intent(this@HeaIncServiceActivity, MainActivity2::class.java))

                finish()
            }
            1 -> {
                startActivity(Intent(this@HeaIncServiceActivity, FavActivity::class.java))

                finish()
            }
            2 -> {
                startActivity(Intent(this@HeaIncServiceActivity, AboutUs::class.java))

                finish()
            }
            3 -> {
                startActivity(Intent(this@HeaIncServiceActivity, ContactUs::class.java))

                finish()
            }
            4 -> closeDrawerLayout()
        }


    }

    private fun openDrawerLayout() {
        drawerLayout.openDrawer(GravityCompat.END)
    }

    private fun closeDrawerLayout() {
        drawerLayout.closeDrawers()
    }

}

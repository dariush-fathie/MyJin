package myjin.pro.ahoora.myjin.activitys

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.activity_app_intro.*

import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.IntroAdapter
import myjin.pro.ahoora.myjin.models.KotlinSlideMainModel
import myjin.pro.ahoora.myjin.utils.ApiInterface
import myjin.pro.ahoora.myjin.utils.KotlinApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AppIntro : AppCompatActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private var list: ArrayList<String>? = null

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        val getSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(baseContext)
        val e = getSharedPreferences.edit()
        if (p1) {
            e.putBoolean("firstStart", false)
            e.apply()
        } else {

            e.putBoolean("firstStart", true)
            e.apply()
        }
    }


    private fun introUrls() {
        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
        apiInterface.sliderMain(0).enqueue(object : Callback<List<KotlinSlideMainModel>> {

            override fun onResponse(call: Call<List<KotlinSlideMainModel>>?, response: Response<List<KotlinSlideMainModel>>?) {
                val list1 = response?.body()

                val urls = ArrayList<String>()

                list1?.get(0)!!.slideList?.forEach { i ->

                    urls.add(i.fileUrl!!)
                }

                list = urls
                val adapter = IntroAdapter(this@AppIntro, list!!)

                rv_intro.layoutManager = LinearLayoutManager(this@AppIntro, LinearLayoutManager.HORIZONTAL, false)
                rv_intro.adapter = adapter
                list_indicator_i.attachToRecyclerView(rv_intro)
                val snapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(rv_intro)
                autoScrollSlide()
            }

            override fun onFailure(call: Call<List<KotlinSlideMainModel>>?, t: Throwable?) {
                Log.e("ERR", t?.message + "  ")
            }


        })
    }

    override fun onClick(p0: View?) {


        when (p0?.id) {
            R.id.tv_next -> {
                finish()
            }
            R.id.iv_next -> {
                next()
            }
            R.id.iv_back -> {
                back()
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_intro)

        tv_next.setOnClickListener(this)
        iv_next.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        cb_intro.setOnCheckedChangeListener(this)

        rv_intro.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == 0) {
                    val k = (rv_intro.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    iv_back.visibility = View.VISIBLE
                    iv_next.visibility = View.VISIBLE
                    if (k == rv_intro.adapter.itemCount - 1) {
                        iv_next.visibility = View.GONE
                    } else if (k == 0) {
                        iv_back.visibility = View.GONE
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        introUrls()

    }

    private fun next() {
        val k = (rv_intro.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() + 1
        (rv_intro.layoutManager as LinearLayoutManager).scrollToPosition(k)
        iv_back.visibility = View.VISIBLE

        if (k == rv_intro.adapter.itemCount - 1) {
            iv_next.visibility = View.GONE
        }
    }

    private fun back() {
        val k = (rv_intro.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() - 1
        (rv_intro.layoutManager as LinearLayoutManager).scrollToPosition(k)
        iv_next.visibility = View.VISIBLE

        if (k == 0) {
            iv_back.visibility = View.GONE
        }
    }

    private fun autoScrollSlide() {

        var n = 0
        val handler = Handler()
        var forward = true

        val t = Timer()

        t.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {


                    if (forward) {
                        rv_intro.smoothScrollToPosition(n++)

                    } else {
                        if (n == rv_intro.adapter.itemCount + 1) {
                            t.cancel()
                            rv_splash.visibility = View.GONE
                        }
                        n++
                    }

                    if (n == rv_intro.adapter.itemCount) {
                        forward = false
                        rv_intro.adapter.notifyItemChanged(n - 1)

                        rv_intro.smoothScrollToPosition(0)

                    }


                }

            }


        }, 1, 300)


    }


}

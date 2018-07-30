package myjin.pro.ahoora.myjin.activitys

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_app_intro.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.IntroAdapter
import myjin.pro.ahoora.myjin.models.KotlinSlideMainModel
import myjin.pro.ahoora.myjin.models.KotlinSlideModel
import myjin.pro.ahoora.myjin.utils.ApiInterface
import myjin.pro.ahoora.myjin.utils.KotlinApiClient
import myjin.pro.ahoora.myjin.utils.SharedPer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AppIntro : AppCompatActivity(), View.OnClickListener {

    private fun introUrls() {
        val apiInterface = KotlinApiClient.client.create(ApiInterface::class.java)
        apiInterface.sliderMain(0).enqueue(object : Callback<List<KotlinSlideMainModel>> {
            override fun onResponse(call: Call<List<KotlinSlideMainModel>>?, response: Response<List<KotlinSlideMainModel>>?) {
                val responseList = response?.body()

                responseList ?: onFailure(call, Throwable("null body"))
                responseList ?: return

                responseList[0].slideList ?: onFailure(call, Throwable("null slides"))
                responseList[0].slideList ?: return

                itemCount = responseList[0].slideList!!.size
                initList(responseList[0].slideList!!.toTypedArray())
            }

            override fun onFailure(call: Call<List<KotlinSlideMainModel>>?, t: Throwable?) {
                Log.e("ERR", t?.message + "  ")
                goToSplash()
            }


        })
    }

    private fun initList(slides: Array<KotlinSlideModel>) {
        val adapter = IntroAdapter(this@AppIntro, slides)
        rv_intro.layoutManager = LinearLayoutManager(this@AppIntro, LinearLayoutManager.HORIZONTAL, false)
        rv_intro.adapter = adapter
        list_indicator_i.attachToRecyclerView(rv_intro)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rv_intro)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_skip -> {
                goToSplash()
            }
            R.id.iv_next -> {
                next()
            }
        }
    }

    private fun goToSplash() {
        SharedPer(this).setBoolean(getString(R.string.introductionFlag), true)
        startActivity(Intent(this, SplashScreen::class.java))
        finish()
    }

    private var currentPosition = 0
    private var itemCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_intro)

        iv_next.setOnClickListener(this)
        tv_skip.setOnClickListener(this)

        rv_intro.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                currentPosition = (rv_intro.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                Log.e("p", "$currentPosition")
                if (currentPosition >= 0) {
                    if (currentPosition == rv_intro.adapter.itemCount - 1) {
                        iv_next.setImageResource(R.drawable.ic_ok)
                    } else {
                        iv_next.setImageResource(R.drawable.ic_next_arrow)
                    }
                } else {
                    iv_next.setImageResource(R.drawable.ic_next_arrow)
                }
            }
        })

        introUrls()
    }

    private fun next() {
        if (currentPosition < itemCount - 1) {
            Log.e("pp", "$currentPosition")
            rv_intro.smoothScrollToPosition(++currentPosition)
        } else {
            goToSplash()
        }
    }


}

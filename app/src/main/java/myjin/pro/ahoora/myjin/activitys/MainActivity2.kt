package myjin.pro.ahoora.myjin.activitys

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import ir.paad.audiobook.utils.Converter
import kotlinx.android.synthetic.main.activity_main2.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.adapters.PagerAdapter
import myjin.pro.ahoora.myjin.adapters.SliderAdapter
import myjin.pro.ahoora.myjin.customClasses.SliderDecoration
import myjin.pro.ahoora.myjin.utils.Colors
import org.greenrobot.eventbus.EventBus

class MainActivity2 : AppCompatActivity(), TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener, AppBarLayout.OnOffsetChangedListener {


    private val bankPosition = 0
    private var fabH = 0f
    private var isSearchVisible = true

    override fun onStart() {
        super.onStart()
        abp_main.addOnOffsetChangedListener(this)
        Log.e("sdklfjslf", "ffsdklfjsdlf")
    }

    override fun onStop() {
        super.onStop()
        abp_main.removeOnOffsetChangedListener(this)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (appBarLayout?.totalScrollRange == Math.abs(verticalOffset)) {
            view_gradient.visibility = View.VISIBLE
        } else {
            view_gradient.visibility = View.GONE
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        fabH = Converter.pxFromDp(this, 16f + 50f + 20)


        tbl_main?.addTab(tbl_main.newTab()
                .setText("بانک سلامت"), false)
        tbl_main?.addTab(tbl_main.newTab()
                .setText("پیغام ها"), false)
        tbl_main?.addTab(tbl_main.newTab()
                .setText("نشان شده ها"), false)

        tbl_main.addOnTabSelectedListener(this)

        vp_mainContainer.adapter = PagerAdapter(supportFragmentManager)
        vp_mainContainer.addOnPageChangeListener(this)
        tbl_main.setupWithViewPager(vp_mainContainer)

        Handler().postDelayed({
            tbl_main?.getTabAt(0)?.select()
            ipi_main.attachToViewPager(vp_mainContainer)
        }, 50)

        rv_mainSlider.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_mainSlider.addItemDecoration(SliderDecoration(this, 8))
        rv_mainSlider.adapter = SliderAdapter(this, arrayListOf("", "", "", ""))

    }


    override fun onPageScrollStateChanged(state: Int) {

    }

    private var mOffset = 0f


    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        /*if (positionOffset > mOffset) {
            Log.e("direction", "to right")
        } else if (positionOffset < mOffset && positionOffset != 0f) {
            Log.e("direction", "to left")
        } else {
            Log.e("direction", "no move")
        }
        mOffset = positionOffset
        val i = tbl_main.selectedTabPosition
        Log.e("offset", "$position - $positionOffset - $positionOffsetPixels")
        tbl_main.getTabAt(i)?.customView?.alpha = 1 - positionOffset*/
    }


    override fun onPageSelected(position: Int) {
        if (position != bankPosition) {
            hideLocation()
            if (isSearchVisible) {
                hideSearchFab()
            }
        } else {
            showLocation()
            if (!isSearchVisible) {
                showSearchFab()
            }
        }
        EventBus.getDefault().post(0)
    }


    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        addOrRemoveColorFilter(tab!!, false)
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        addOrRemoveColorFilter(tab!!, true)
        //newGenerSelected(tab.position)
    }

    private fun addOrRemoveColorFilter(tab: TabLayout.Tab, addFilter: Boolean) {
        val view = tab.customView
        val text = view?.findViewById<AppCompatTextView>(R.id.tv_customTabTitle)
        val cardView = view?.findViewById<CardView>(R.id.cv_customTabContainer)
        val colors = Colors(this)

        if (addFilter) {
            view?.scaleY = 1f
            view?.scaleX = 1f
            text?.setTextColor(colors.colorPrimaryDark)
            text?.setBackgroundResource(R.drawable.empty)
            cardView?.cardElevation = 3f
            cardView?.setCardBackgroundColor(colors.white)
        } else {
            view?.scaleY = 0.85f
            view?.scaleX = 0.85f
            text?.setTextColor(colors.title)
            text?.setBackgroundResource(R.drawable.dot_rect_back)
            cardView?.cardElevation = 0f
            cardView?.setCardBackgroundColor(colors.transparent)
        }
    }

    private fun showLocation() {
        iv_locationArrrow.visibility = View.VISIBLE
        tv_location.visibility = View.VISIBLE
    }

    private fun hideLocation() {
        iv_locationArrrow.visibility = View.GONE
        tv_location.visibility = View.GONE
    }


    private fun showSearchFab() {
        val animSet = AnimatorSet()
        val alphaAnimator = ObjectAnimator.ofFloat(fab_search, "alpha", 0f, 1f)
        val transitionAnimator = ObjectAnimator.ofFloat(fab_search, "translationY", fabH, 0f)
        animSet.playTogether(alphaAnimator, transitionAnimator)
        animSet.start()
        isSearchVisible = true
    }


    private fun hideSearchFab() {
        val animSet = AnimatorSet()
        val alphaAnimator = ObjectAnimator.ofFloat(fab_search, "alpha", 1f, 0f)
        val transitionAnimator = ObjectAnimator.ofFloat(fab_search, "translationY", 0f, fabH)
        animSet.playTogether(alphaAnimator, transitionAnimator)
        animSet.start()
        isSearchVisible = false
    }


}
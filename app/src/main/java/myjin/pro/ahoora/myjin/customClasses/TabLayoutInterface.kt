package myjin.pro.ahoora.myjin.customClasses;

import android.content.Context
import android.os.Handler
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.FragmentManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.fragments.MapFragment
import myjin.pro.ahoora.myjin.fragments.NearestFragment
import myjin.pro.ahoora.myjin.fragments.SearchFragment

class TabLayoutInterface(ctx: Context, fm: FragmentManager, behavior: BottomSheetBehavior<ConstraintLayout>, progressLayout: LinearLayout) : TabLayout.OnTabSelectedListener {

    val fragmentManager = fm
    val bottomSheetBehavior = behavior
    val progressLL = progressLayout
    val TAG = "TabLayoutInterface"


    override fun onTabSelected(tab: TabLayout.Tab) {
        openBottomLayout(tab.position)
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
        // remove color filter from preSelected tab
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
        Log.e("RESELECT", "${tab.position}")
        openBottomLayout(tab.position)
    }

    private fun openBottomLayout(tab: Int) {

            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            showProgress()
            Handler().postDelayed({

                loadFragment(tab)
            }, 450)


    }

    fun closeBottomLayout(tab: Int) {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun loadFragment(i: Int) {
       /* if (i == 0) {
            fragmentManager.beginTransaction().replace(R.id.fl_sheetContainer, MapFragment()).commit()
        } else  if (i == 2){*/
            fragmentManager.beginTransaction().replace(R.id.fl_sheetContainer, SearchFragment()).commit()
        /*}else{
            fragmentManager.beginTransaction().replace(R.id.fl_sheetContainer, NearestFragment()).commit()
        }*/
    }

    private fun showProgress() {
        progressLL.visibility = View.VISIBLE
    }

    fun hideProgress() {
        progressLL.visibility = View.GONE
    }


}
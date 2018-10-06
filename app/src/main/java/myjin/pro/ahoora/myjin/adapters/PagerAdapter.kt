package myjin.pro.ahoora.myjin.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.fragments.FavFragment
import myjin.pro.ahoora.myjin.fragments.HealthBankFragment
import myjin.pro.ahoora.myjin.fragments.MessagesFragment

class PagerAdapter(fm: FragmentManager,context:Context) : FragmentStatePagerAdapter(fm) {

    val mContext=context
    override fun getItem(position: Int): Fragment? {
        return when (position) {
            1 -> HealthBankFragment()
            0 -> MessagesFragment()
            else -> FavFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            1 -> mContext.getString(R.string.healthCenters)
            0 -> mContext.getString(R.string.etlaeieh)
            else -> "نشان شده ها"
        }
    }
}

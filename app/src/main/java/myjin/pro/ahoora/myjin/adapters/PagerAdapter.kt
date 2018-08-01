package myjin.pro.ahoora.myjin.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import myjin.pro.ahoora.myjin.fragments.FavFragment
import myjin.pro.ahoora.myjin.fragments.HealthBankFragment
import myjin.pro.ahoora.myjin.fragments.MessagesFragment

class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

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
            1 -> "بانک سلامت"
            0 -> "پیام ها"
            else -> "نشان شده ها"
        }
    }
}

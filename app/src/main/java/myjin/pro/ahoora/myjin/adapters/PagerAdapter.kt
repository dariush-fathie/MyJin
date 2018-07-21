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
            0 -> HealthBankFragment()
            1 -> MessagesFragment()
            else -> FavFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "بانک سلامت"
            1 -> "پیام ها"
            else -> "ذخیره شده ها"
        }
    }
}

package myjin.pro.ahoora.myjin.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.fragments.*

class PagerAdapter(fm: FragmentManager, context:Context) : FragmentStatePagerAdapter(fm) {

    val mContext=context
    override fun getItem(position: Int): Fragment? {
        return when (position) {
            4 -> HealthBankFragment()
            3 -> SpeOfferFragment()
            2 -> UsefullToolFragment()
            1 -> MessagesFragment()
            else -> AdviceFragment()
        }
    }

    override fun getCount(): Int {
        return 5
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            4 -> mContext.getString(R.string.healthCenters)
            3 -> mContext.getString(R.string.pishnahadvizheh)
            2 -> mContext.getString(R.string.abzarmofid)
            1 -> mContext.getString(R.string.etlaeieh)
            else -> mContext.getString(R.string.moshaverhoporsesh)
        }
    }

}

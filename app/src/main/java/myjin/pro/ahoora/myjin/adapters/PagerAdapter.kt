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
            5 -> HealthBankFragment()
            4 -> MessagesFragment()
            3 -> FilmAnimationFragment()
            2 -> SpeOfferFragment()
            1 -> UsefullToolFragment()
            else -> AdviceFragment()
        }
    }

    override fun getCount(): Int {
        return 6
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            5 -> mContext.getString(R.string.healthCenters)
            4 -> mContext.getString(R.string.etlaeieh)
            3 -> mContext.getString(R.string.filmoanimation)
            2 -> mContext.getString(R.string.pishnahadvizheh)
            1 -> mContext.getString(R.string.abzarmofid)
            else -> mContext.getString(R.string.moshaverhoporsesh)
        }
    }

}

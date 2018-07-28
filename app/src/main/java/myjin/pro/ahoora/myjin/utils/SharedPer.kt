package myjin.pro.ahoora.myjin.utils

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class SharedPer(mContext: Context) {

    private val editor1: SharedPreferences.Editor
    private val sharedPreference: SharedPreferences = mContext.getSharedPreferences("spz", 0)
    private val filter: ArrayList<Int>? = null

    var filterC = 0

    fun getFilter(): ArrayList<Int> {
        filter!!.clear()
        filterC = sharedPreference.getInt("filterC", 0)
        for (i in 0 until filterC) {
            filter.add(sharedPreference.getInt("f$i", 0))
        }

        return filter
    }

    fun setFilter(filter: ArrayList<Int>) {
        editor1.putInt("filterC", filter.size)
        editor1.apply()
        for (i in filter.indices) {
            editor1.putInt("f$i", filter[i])
            editor1.apply()
        }
    }

    init {
        editor1 = sharedPreference.edit()
        editor1.apply()
    }


    fun setString(name: String, value: String) {
        editor1.putString(name, value).apply()
    }

    fun getString(name: String): String {
        return sharedPreference.getString(name, "")
    }


    fun setBoolean(name: String, value: Boolean) {
        editor1.putBoolean(name, value).apply()
    }

    fun getBoolean(name: String): Boolean {
        return sharedPreference.getBoolean(name, false)
    }

}

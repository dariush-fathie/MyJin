package myjin.pro.ahoora.myjin.customClasses

import android.content.Context
import android.util.Log

import java.util.ArrayList

import myjin.pro.ahoora.myjin.R

class ProvincesAndCitys {
    var provId = 19
    var cityId = 1
    internal lateinit var context: Context

    internal lateinit var list1: ArrayList<ProvincesAndCitys>
    internal lateinit var list2: ArrayList<String>

    constructor(context: Context) {

        this.context = context
        list1 = ArrayList()
        list2 = ArrayList()
        getProvsAndCitys()
    }

    constructor() {}


    fun getProvsAndCitys() {

        val x = context.resources.getStringArray(R.array.cityArray)
        val y = context.resources.getStringArray(R.array.provArray)
        var m: ProvincesAndCitys

        for (i in x.indices) {
            list2.add(x[i])
            m = ProvincesAndCitys()
            m.provId = 19
            m.cityId = i
            list1.add(m)
        }
        for (i in y.indices) {
            list2.add(y[i])
            m = ProvincesAndCitys()
            m.provId = i
            m.cityId = -1
            list1.add(m)

        }

    }
}

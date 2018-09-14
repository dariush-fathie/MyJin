package myjin.pro.ahoora.myjin.customClasses

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.DisplayMetrics
import android.view.View
import android.widget.LinearLayout
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterItem
import myjin.pro.ahoora.myjin.R

class Clusters : ClusterItem {
    private val mPosition: LatLng
    private var mTitle: String = ""
    private var mSnippet: String = ""
    private lateinit var mContext: Context

    constructor(context: Context,lat: Double, lng: Double) {
        mPosition = LatLng(lat, lng)

    }


    constructor(context: Context, lat: Double, lng: Double, title: String, snippet: String) {

        mPosition = LatLng(lat, lng)
        mContext=context
        mTitle = title
        mSnippet = snippet


    }


    override fun getPosition(): LatLng {


        return mPosition
    }


}

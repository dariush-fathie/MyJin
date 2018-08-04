package myjin.pro.ahoora.myjin.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkUtil {

    val WIFI_STATE_CHANGE_ACTION = "android.net.wifi.WIFI_STATE_CHANGED"
    val manuallyTriggerOnReceive = "ir.paad.update.net.flag"

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo: NetworkInfo?
        activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun updateNetFlag(context: Context){
        val intent = Intent(NetworkUtil().manuallyTriggerOnReceive)
        context.sendBroadcast(intent)
    }

}
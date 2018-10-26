package myjin.pro.ahoora.myjin.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.IBinder
import android.util.Log
import android.view.inputmethod.InputMethodManager

object Utils {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun getScreenWidthPx(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getScreenWidthDp(context: Context): Int {
        return dpFromPx(context, getScreenWidthPx(context).toFloat()).toInt()
    }

    fun getScreenHeightPx(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun getScreenHeightDp(context: Context): Int {
        return dpFromPx(context, getScreenWidthPx(context).toFloat()).toInt()
    }


    fun dpFromPx(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    fun closeKeyBoard(token: IBinder, context: Context) {
        try {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(token, 0)
        } catch (e: Exception) {
            Log.e("ERR_KEYBOARD", e.message + " ")
        }
    }

    fun setLogin(b: Boolean) {
        VarableValues.IsLOGIN = b
    }

    fun setYekta() {
        VarableValues.yekta = DeviceId.deviceBuildInfo;
    }

    //Email Validation pattern
    val regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b"
    val regPhone="(0|\\+98)?([ ]|-|[()]){0,2}9[1|2|3|4]([ ]|-|[()]){0,2}(?:[0-9]([ ]|-|[()]){0,2}){8}"

    //Fragments Tags
    val Login_Fragment = "Login_Fragment"
    val Verification_Fragment = "Verification_Fragment"
    val YourName_Fragment = "YourName_Fragment"

}

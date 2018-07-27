package myjin.pro.ahoora.myjin.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import myjin.pro.ahoora.myjin.models.events.NetChangeEvent
import org.greenrobot.eventbus.EventBus

class NetworkStateReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        EventBus.getDefault().post(NetChangeEvent(Utils.isNetworkAvailable(context)))
    }

}
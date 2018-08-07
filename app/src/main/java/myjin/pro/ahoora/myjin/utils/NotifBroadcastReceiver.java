package myjin.pro.ahoora.myjin.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotifBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(context.getPackageName()+"co.ronash.pushe.NOTIF_CLICKED")){
            Log.e("Pushe", "Broadcast co.ronash.pushe.NOTIF_CLICKED received");
            //add your logic here
        }
        else if(intent.getAction().equals(context.getPackageName()+"co.ronash.pushe.NOTIF_DISMISSED")){
            Log.e("Pushe", "Broadcast co.ronash.pushe.NOTIF_DISMISSED received");
            //add your logic here
        }
        else if(intent.getAction().equals(context.getPackageName()+"co.ronash.pushe.NOTIF_BTN_CLICKED")){
            String btnId = intent.getStringExtra("pushe_notif_btn_id");
            Log.e("Pushe", "Broadcast co.ronash.pushe.NOTIF_BTN_CLICKED received. BtnId =  " + btnId);
            //add your logic here
        }
    }
}

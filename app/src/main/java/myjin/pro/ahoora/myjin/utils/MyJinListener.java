package myjin.pro.ahoora.myjin.utils;

import android.util.Log;

import org.json.JSONObject;

import co.ronash.pushe.PusheListenerService;

public class MyJinListener extends PusheListenerService {
    @Override
    public void onMessageReceived(final JSONObject message, JSONObject content) {
        Log.e("Pushe", "content json content: " + content.toString());

        if(message != null && message.length() > 0) {
            Log.e("Pushe", "Custom json Message: " + message.toString());

        }
    }
}
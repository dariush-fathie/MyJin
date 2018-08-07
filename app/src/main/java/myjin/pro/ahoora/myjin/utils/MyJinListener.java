package myjin.pro.ahoora.myjin.utils;

import android.util.Log;

import org.json.JSONObject;

import co.ronash.pushe.PusheListenerService;

public class MyJinListener extends PusheListenerService {
    @Override
    public void onMessageReceived(final JSONObject message, JSONObject content) {
        if(message != null && message.length() > 0) {
            Log.e("Pushe", "Custom json Message: " + message.toString());
//            //    your code
//            try {
//                String s1 = message.getString("key1");
//            } catch (JSONException e) {
//                android.util.Log.e("","Exception in parsin json" ,e);
//            }
        }
    }
}
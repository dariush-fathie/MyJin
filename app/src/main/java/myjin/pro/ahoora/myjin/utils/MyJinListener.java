package myjin.pro.ahoora.myjin.utils;

import android.util.Log;

import org.json.JSONObject;

import co.ronash.pushe.PusheListenerService;

public class MyJinListener extends PusheListenerService {
    @Override
    public void onMessageReceived(final JSONObject message, JSONObject content) {
        Log.e("Pushe", "content json content: " + content.toString());

        /*{"types":[],
"iconUrl":"http:\/\/api.pushe.co\/media\/notification-images\/20180807-eac83a786bd84179a07b92600647e1d4-xxh.png",
"title":"تیتر",
"bigContent":"متن",
"summary":"null",
"imageUrl":"http:\/\/api.pushe.co\/media\/notification-images\/20180807-a87a8dc4f93b4699876ef6159bee0d2b.png",
"content":"متن",
"ticker":"متن نوار اعلان",
"bigTitle":"تیتر کامل"}
*/

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
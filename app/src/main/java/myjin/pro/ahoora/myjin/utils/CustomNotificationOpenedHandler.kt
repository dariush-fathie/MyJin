package myjin.pro.ahoora.myjin.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.onesignal.OSNotificationAction
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal
import myjin.pro.ahoora.myjin.activitys.NotificationActivity
import myjin.pro.ahoora.myjin.models.NotificationModel


internal class CustomNotificationOpenedHandler(private val context: Context) : OneSignal.NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.
    override fun notificationOpened(result: OSNotificationOpenResult) {
        val actionType = result.action.type
        val data = result.notification.payload.additionalData
        val customKey: String?

        if (data != null) {
            customKey = data.optString("customkey", null)
            if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: $customKey")
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID)

        // The following can be used to open an Activity of your choice.
        //Replace - getApplicationContext() - with any Android Context.

        val intent = Intent(context, NotificationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("data", result.notification.payload.rawPayload)
        val notificationModel = Gson().fromJson(result.notification.payload.rawPayload, NotificationModel::class.java)
        intent.putExtra("data1", notificationModel)
        context.startActivity(intent)

        // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
        //   if you are calling startActivity above.
        /*
        <application ...>
          <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
        </application>
     */
    }
}
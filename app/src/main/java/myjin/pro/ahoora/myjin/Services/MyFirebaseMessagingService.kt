package myjin.pro.ahoora.myjin.Services


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.RemoteViews
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.realm.Realm
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.activitys.NotificationActivity
import myjin.pro.ahoora.myjin.models.KotlinNotificationModel
import android.net.Uri


class MyFirebaseMessagingServicse() : FirebaseMessagingService() {

    private val mNotification = KotlinNotificationModel()

    companion object {
        private val TAG = "MyFirebaseMsgService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        try{


            if (remoteMessage!!.data.isNotEmpty()) {
                Log.e(TAG, "Message data payload: " + remoteMessage.data)
                val realm = Realm.getDefaultInstance()

                mNotification.title = remoteMessage.data["title"]!!
                mNotification.message = remoteMessage.data["message"]!!

                mNotification.iconUrl = remoteMessage.data["iconUrl"]
                mNotification.sound = remoteMessage.data["sound"]
                mNotification.vibrate = remoteMessage.data["vibrate"]?.toBoolean()!!
                mNotification.showToUser = remoteMessage.data["showToUser"]?.toBoolean()!!
                mNotification.ledColor = remoteMessage.data["ledColor"]?.toInt()
                mNotification.regDate=remoteMessage.data["sendTime"]!!

                realm.executeTransaction { db ->
                    val maxId = db.where(KotlinNotificationModel::class.java).max("messageId")

                    val nextId = if (maxId == null) 1 else maxId.toInt() + 1

                    mNotification.messageId=nextId
                    realm.copyToRealmOrUpdate(mNotification)

                    if (mNotification.showToUser) {
                        sendNotification()
                    }
                }


            }
        }catch (e:Exception){

            Log.e(TAG,e.toString())
        }
    }



    override fun onNewToken(token: String?) {
        Log.e(TAG, "Refreshed token: " + token!!)
        subscribe()
        sendRegistrationToServer(token)
    }


    private fun subscribe() {
        FirebaseMessaging.getInstance().subscribeToTopic("all")
                .addOnCompleteListener { task ->
                    var msg = "success subscribe"
                    if (!task.isSuccessful) {
                        msg = "failed subscribe"
                    }
                    Log.d(TAG, msg)

                }
    }


    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
    }




    private fun sendNotification() {

        try{

            val intent = Intent(this, NotificationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            /*  val bundle = Bundle()
              bundle.putSerializable("remoteNotification", remoteNotification)
              intent.putExtras(bundle)*/

            val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT)

            val launcherBitmap = (ContextCompat.getDrawable(this, R.mipmap.ic_launcher) as BitmapDrawable).bitmap

            val channelId = getString(R.string.default_notification_channel_id)

            val uri = Uri.parse("android.resource://" + packageName + "/" +R.raw.notification)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(mNotification.title)
                    .setContentText(mNotification.message)
                    .setAutoCancel(true)
                    .setSound(uri)
                    .setContentIntent(pendingIntent)
                    .setShowWhen(false)

            notificationBuilder.color = ContextCompat.getColor(this, R.color.colorAccent)


            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }
            val smallView = RemoteViews(packageName, R.layout.notification_small_layout)


            smallView.setImageViewBitmap(R.id.iv_notification_smallImage, launcherBitmap)
            smallView.setTextViewText(R.id.tv_small_notification_title, mNotification.title)
            smallView.setTextViewText(R.id.tv__small_notification_message, mNotification.message)

            notificationBuilder.setCustomContentView(smallView)

            notificationManager.notify(0 , notificationBuilder.build())
        }catch (e:Exception){
            Log.e(TAG,e.toString())
        }

    }



}

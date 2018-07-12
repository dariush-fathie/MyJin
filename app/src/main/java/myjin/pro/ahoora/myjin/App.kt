package myjin.pro.ahoora.myjin

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.onesignal.OneSignal
import io.realm.Realm
import io.realm.RealmConfiguration
import myjin.pro.ahoora.myjin.utils.CustomNotificationOpenedHandler

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseAnalytics.getInstance(this)

        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .name("database.realm")
                //.encryptionKey(getKey())
                .schemaVersion(1)
                //.modules(new MySchemaModule())
                //.migration(new MyMigration())
                .build()

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .filterOtherGCMReceivers(true)
                .setNotificationOpenedHandler(CustomNotificationOpenedHandler(this))
                //.setNotificationReceivedHandler(CustomNotificationReceivedHandler())
                .init()

        Realm.setDefaultConfiguration(config)

    }
}

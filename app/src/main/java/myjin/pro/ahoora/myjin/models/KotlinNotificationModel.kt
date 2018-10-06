package myjin.pro.ahoora.myjin.models

import android.graphics.Color
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.io.Serializable

@RealmClass
open class KotlinNotificationModel : RealmObject() {

    @PrimaryKey
    open var messageId: Int = 1
    
    open var  title: String? = ""

    open var  message: String? = ""

    open var  iconUrl: String? = ""

    open var  sound: String? = "default.mp3"

    open var  vibrate: Boolean = false

    open var  showToUser: Boolean = true

    open var  ledColor: Int? = Color.GREEN

    open var regDate: String = ""


}

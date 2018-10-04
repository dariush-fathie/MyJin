package myjin.pro.ahoora.myjin.models

import android.graphics.Color
import java.io.Serializable

class NotificationModel : Serializable {
    
    var title: String? = null

    var message: String? = null

    var iconUrl: String? = null


    var sound: String? = "default..mp3"

    var vibrate: Boolean = false

    var showToUser: Boolean = true

    var ledColor: Int? = Color.GREEN




}

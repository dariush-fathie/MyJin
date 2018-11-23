package myjin.pro.ahoora.myjin.models

import com.google.gson.annotations.SerializedName

open class KotlinVideoByUserListModel {
    @SerializedName("videobyuser")
    var videobyuser: List< KotlinVideoByUserModel>? = null

}
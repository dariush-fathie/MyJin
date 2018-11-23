package myjin.pro.ahoora.myjin.models

import com.google.gson.annotations.SerializedName

class KotlinVideoByUserModel {
    @SerializedName("id")
    var id: Int = -1

    @SerializedName("title")
    var title: String? = ""

    @SerializedName("uid")
    var uid: String? = ""


    @SerializedName("username")
    var username: String? = ""


    @SerializedName("userid")
    var userid: String? = ""

    @SerializedName("small_poster")
    var small_poster: String? = ""

    @SerializedName("frame")
    var frame: String? = ""

}

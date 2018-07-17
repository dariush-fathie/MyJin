package myjin.pro.ahoora.myjin.models

import android.graphics.Color
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class KotlinServicesModel : RealmObject() {

    @SerializedName("Id")
    @Expose
    open var Id: Int? = 1

    @SerializedName("groupId")
    @Expose
    open var groupId: Int? = 1

    @SerializedName("groupName")
    @Expose
    open var groupName: String? = ""


    @SerializedName("title")
    @Expose
    open var title: String? = ""

    @SerializedName("context")
    @Expose
    open var context: String? = ""

    @SerializedName("priority")
    @Expose
    open var priority: Int? = 1

    @SerializedName("color")
    @Expose
    open var color_: String? ="#cbcbcc"
}
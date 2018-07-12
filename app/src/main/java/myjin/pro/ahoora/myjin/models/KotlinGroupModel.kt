package myjin.pro.ahoora.myjin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class KotlinGroupModel : RealmObject() {

    @SerializedName("counter")
    @Expose
    open var counter: Int = -1
    @SerializedName("name")
    @Expose
    open var name: String? = ""

    @SerializedName("group_id")
    @Expose
    open var groupId: Int = -1
    @SerializedName("g_url")
    @Expose
    open var g_url: String? = ""
}

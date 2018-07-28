package myjin.pro.ahoora.myjin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class KotlinAboutContactModel : RealmObject() {

    @SerializedName("id")
    @Expose
    open var id: Int? = 1
    @SerializedName("tAbout")
    @Expose
    open var tAbout: String? = ""
    @SerializedName("tContact")
    @Expose
    open var tContact: String? = ""
    @SerializedName("tKafeh")
    @Expose
    open var tKafeh: String? = ""
    @SerializedName("version")
    @Expose
    open var version: Int? = 1
    @SerializedName("force")
    @Expose
    open var force: Int? = 1

    @SerializedName("serverStatus")
    @Expose
    open var serverStatus: String = "somethig like not ok"
}


package myjin.pro.ahoora.myjin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class KotlinSlideModel : RealmObject() {

    @SerializedName("file_url")
    @Expose
    open var fileUrl: String? = ""
    @SerializedName("description")
    @Expose
    open var description: String? = ""
    @SerializedName("arrange")
    @Expose
    open var arrange: String? = ""

}
package myjin.pro.ahoora.myjin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class KotlinAdviceModel : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    open var Id: Int = -1

    @SerializedName("idType")
    @Expose
    open var idType: Int = -1

    @SerializedName("type")
    @Expose
    open var type: String? = ""

    open var saved: Boolean = false

    @SerializedName("title")
    @Expose
    open var title: String? = ""

    @SerializedName("context")
    @Expose
    open var context: String? = ""

}
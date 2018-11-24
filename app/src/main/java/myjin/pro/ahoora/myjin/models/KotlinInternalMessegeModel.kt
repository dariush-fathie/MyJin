package myjin.pro.ahoora.myjin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class KotlinInternalMessegeModel : RealmObject() {
    @PrimaryKey
    @SerializedName("Id")
    @Expose
    open var id: Int? = 1

    @SerializedName("title")
    @Expose
    open var title: String? = ""

    @SerializedName("context")
    @Expose
    open var context: String? = ""

    @SerializedName("newRecord")
    @Expose
    open var newRecord: String? = "no"

    @SerializedName("color")
    @Expose
    open var color_: String? = "#cbcbcc"
}
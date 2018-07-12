package myjin.pro.ahoora.myjin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class KotlinSpecialityModel : RealmObject() {

    @SerializedName("specialty_id")
    @Expose
    open var specialtyId: Int = -1

    @SerializedName("name")
    @Expose
    open var name: String? = ""

    open var saved: Boolean = false
}
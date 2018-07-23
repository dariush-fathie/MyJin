package myjin.pro.ahoora.myjin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class KotlinProvCityModel : RealmObject() {

    @SerializedName("provId")
    @Expose
    open var provId: Int? = 19

    @SerializedName("nameP")
    @Expose
    open var nameP: String? = "کردستان"

    @SerializedName("nameC")
    @Expose
    open var nameC: String? = "سنندج"

    @SerializedName("cityId")
    @Expose
    open var cityId: Int? = 1


}


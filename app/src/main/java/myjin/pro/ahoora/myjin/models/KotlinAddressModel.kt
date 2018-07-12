package myjin.pro.ahoora.myjin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class KotlinAddressModel : RealmObject() {
    @SerializedName("auto_id")
    @Expose
    open var autoId: Int = -1
    @SerializedName("loc_title")
    @Expose
    open var locTitle: String? = ""

    @SerializedName("provId")
    @Expose
    open var provId: Int = 19

    @SerializedName("cityId")
    @Expose
    open var cityId: Int = 1

    @SerializedName("postal_code")
    @Expose
    open var postalCode: String? = ""
    @SerializedName("tel1")
    @Expose
    open var tel1: String? = ""
    @SerializedName("tel1_desc")
    @Expose
    open var tel1Desc: String? = ""
    @SerializedName("tel2")
    @Expose
    open var tel2: String? = ""
    @SerializedName("tel2_desc")
    @Expose
    open var tel2Desc: String? = ""
    @SerializedName("mob1")
    @Expose
    open var mob1: String? = ""
    @SerializedName("mob1_desc")
    @Expose
    open var mob1Desc: String? = ""
    @SerializedName("mob2")
    @Expose
    open var mob2: String? = ""
    @SerializedName("mod2_desc")
    @Expose
    open var mod2Desc: String? = ""
    @SerializedName("gen_desc")
    @Expose
    open var genDesc: String? = ""
    @SerializedName("default_add")
    @Expose
    open var defaultAdd: String? = ""
    @SerializedName("longitude")
    @Expose
    open var longitude: Float? = -1f
    @SerializedName("latitude")
    @Expose
    open var latitude: Float? = -1f
    @SerializedName("site")
    @Expose
    open var site: String? = ""
    @SerializedName("mail")
    @Expose
    open var mail: String? = ""
    @SerializedName("sat_attend")
    @Expose
    open var sat_attend: Boolean?=true
    @SerializedName("sun_attend")
    @Expose
    open var sun_attend: Boolean?=true
    @SerializedName("mon_attend")
    @Expose
    open var mon_attend: Boolean?=true
    @SerializedName("tues_attend")
    @Expose
    open var tues_attend: Boolean?=true
    @SerializedName("wed_attend")
    @Expose
    open var wed_attend: Boolean?=true
    @SerializedName("thurs_attend")
    @Expose
    open var thurs_attend: Boolean?=true
    @SerializedName("fri_attend")
    @Expose
    open var fri_attend: Boolean?=false

}

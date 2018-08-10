package myjin.pro.ahoora.myjin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class KotlinItemModel : RealmObject() {
    @PrimaryKey
    @SerializedName("center_id")
    @Expose
    open var centerId: Int = -1

    open var saved: Boolean = false


    @SerializedName("firstName")
    @Expose
    open var firstName: String? = ""
    @SerializedName("lastName")
    @Expose
    open var lastName: String? = ""
    @SerializedName("reg_date")
    @Expose
    open var regDate: String? = ""
    @SerializedName("valid_date")
    @Expose
    open var validDate: String? = ""
    @SerializedName("active")
    @Expose
    open var active: Int = -1
    @SerializedName("logo_img")
    @Expose
    open var logoImg: String? = ""
    @SerializedName("building_img")
    @Expose
    open var buildingImg: String? = ""
    @SerializedName("short_desc")
    @Expose
    open var shortDesc: String? = ""
    @SerializedName("bio")
    @Expose
    open var bio: String? = ""
    @SerializedName("equipment")
    @Expose
    open var equipment: String? = ""
    @SerializedName("service_list")
    @Expose
    open var serviceList: String? = ""
    @SerializedName("work_team")
    @Expose
    open var workTeam: String? = ""
    @SerializedName("elc_rec")
    @Expose
    open var elcRec: String? = ""
    @SerializedName("grade")
    @Expose
    open var grade: Int = -1
    @SerializedName("group_id")
    @Expose
    open var groupId: Int = -1
    @SerializedName("gen")
    @Expose
    open var gen: String? = "1"

    @SerializedName("Addr")
    @Expose
    open var addressList: RealmList<KotlinAddressModel>? = null
    @SerializedName("Slideshow")
    @Expose
    open var slideList: RealmList<KotlinSlideModel>? = null
    @SerializedName("Specialties")
    @Expose
    open var specialityList: RealmList<KotlinSpecialityModel>? = null
    @SerializedName("Levels")
    @Expose
    open var levelList: RealmList<KotlinLevelModel>? = null
    @SerializedName("C_insurance")
    @Expose
    open var cInsuranceList: RealmList<KotlinCInsuranceModel>? = null


}
package myjin.pro.ahoora.myjin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class KotlinCInsuranceModel : RealmObject() {

    @SerializedName("insurance_id")
    @Expose
    open var insuranceId: Int = -1
    @SerializedName("name")
    @Expose
    open var name: String? = ""
    @SerializedName("description")
    @Expose
    open var description: String? = ""

}
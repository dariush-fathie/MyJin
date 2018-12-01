package myjin.pro.ahoora.myjin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class KotlinSpecialOffersModel : RealmObject() {
    @PrimaryKey
    @SerializedName("autoId")
    @Expose
    open var Id: Int = -1

    @SerializedName("adGroupId")
    @Expose
    open var adGroupId: Int = -1

    @SerializedName("adGroupName")
    @Expose
    open var adGroupName: String? = ""

    @SerializedName("arrange")
    @Expose
    open var arrange: Int=1

    @SerializedName("photoUrl")
    @Expose
    open var photoUrl: String? = ""

}
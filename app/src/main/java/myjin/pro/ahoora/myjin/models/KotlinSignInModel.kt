package myjin.pro.ahoora.myjin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class KotlinSignInModel : RealmObject() {
    @PrimaryKey
    @SerializedName("userId")
    @Expose
    open var userId: Int = -1


    @SerializedName("phoneNumber")
    @Expose
    open var phoneNumber: String? = ""

    @SerializedName("firstName")
    @Expose
    open var firstName: String? = ""


    @SerializedName("lastName")
    @Expose
    open var lastName: String? = ""

    @SerializedName("profileImage")
    @Expose
    open var profileImage: String? = ""

    @SerializedName("active")
    @Expose
    open var active: String = "1"
}
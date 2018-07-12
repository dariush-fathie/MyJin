package myjin.pro.ahoora.myjin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.RealmClass


open class KotlinSlideMainModel : RealmObject() {

    @SerializedName("Slideshow")
    @Expose
    open var slideList: RealmList<KotlinSlideModel>? = null

}
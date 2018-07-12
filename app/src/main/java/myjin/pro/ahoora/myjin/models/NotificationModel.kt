package myjin.pro.ahoora.myjin.models


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationModel() : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest ?: return
        dest.writeDouble(googleSentTime!!)
        dest.writeDouble(googleTtl!!)
        dest.writeString(custom)
        dest.writeString(othChnl)
        dest.writeString(pri)
        dest.writeString(vis)
        dest.writeString(from)
        dest.writeString(alert)
        dest.writeString(title)
        dest.writeString(grpMsg)
        dest.writeString(googleMessageId)
        dest.writeDouble(notificationId!!)
    }

    override fun describeContents(): Int {
        return hashCode()
    }

    @SerializedName("google.sent_time")
    @Expose
    var googleSentTime: Double? = null
    @SerializedName("google.ttl")
    @Expose
    var googleTtl: Double? = null
    @SerializedName("custom")
    @Expose
    var custom: String? = null
    @SerializedName("oth_chnl")
    @Expose
    var othChnl: String? = null
    @SerializedName("pri")
    @Expose
    var pri: String? = null
    @SerializedName("vis")
    @Expose
    var vis: String? = null
    @SerializedName("from")
    @Expose
    var from: String? = null
    @SerializedName("alert")
    @Expose
    var alert: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("grp_msg")
    @Expose
    var grpMsg: String? = null
    @SerializedName("google.message_id")
    @Expose
    var googleMessageId: String? = null
    @SerializedName("notificationId")
    @Expose
    var notificationId: Double? = null

    constructor(parcel: Parcel) : this() {
        googleSentTime = parcel.readDouble()
        googleTtl = parcel.readDouble()
        custom = parcel.readString()
        othChnl = parcel.readString()
        pri = parcel.readString()
        vis = parcel.readString()
        from = parcel.readString()
        alert = parcel.readString()
        title = parcel.readString()
        grpMsg = parcel.readString()
        googleMessageId = parcel.readString()
        notificationId = parcel.readDouble()
    }

    companion object CREATOR : Parcelable.Creator<NotificationModel> {
        override fun createFromParcel(parcel: Parcel): NotificationModel {
            return NotificationModel(parcel)
        }

        override fun newArray(size: Int): Array<NotificationModel?> {
            return arrayOfNulls(size)
        }
    }

}
package myjin.pro.ahoora.myjin.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class KotlinMessagesModel : RealmObject() {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    open var messageId: Int = -1

    @SerializedName("groupId")
    @Expose
    open var groupId: Int = -1

    @SerializedName("groupName")
    @Expose
    open var groupName: String = ""

    @SerializedName("title")
    @Expose
    open var title: String = ""

    @SerializedName("content")
    @Expose
    open var content: String = ""

    @SerializedName("shortDesc")
    @Expose
    open var shortDescription: String = ""

    @SerializedName("type")
    @Expose
    open var type: String = ""

    @SerializedName("typeId")
    @Expose
    open var typeId: Int = -1

    @SerializedName("priority")
    @Expose
    open var priority: Int = -1

    @SerializedName("bgColor")
    @Expose
    open var bgColor: String = ""

    @SerializedName("imgUrl")
    @Expose
    open var imageUrl: String = ""

    @SerializedName("regDate")
    @Expose
    open var regDate: String = ""

    override fun toString(): String {
        return "messageId: $messageId " +
                "- groupId: $groupId " +
                "- groupName: $groupName" +
                "- title: $title" +
                "- shortDesc: $shortDescription" +
                "- content: $content" + "" +
                "- type: $type" +
                "- priority: $priority" +
                "- bgColor: $bgColor" +
                "- imageUrl: $imageUrl" +
                "- regDate: $regDate"
    }

}
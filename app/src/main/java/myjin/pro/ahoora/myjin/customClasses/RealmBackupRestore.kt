package myjin.pro.ahoora.myjin.customClasses

import androidx.appcompat.app.AppCompatActivity
import android.os.Environment
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.Realm
import myjin.pro.ahoora.myjin.models.KotlinItemModel
import myjin.pro.ahoora.myjin.models.KotlinMessagesModel
import myjin.pro.ahoora.myjin.models.KotlinNotificationModel
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.InputStream


class RealmBackupRestore(activity: AppCompatActivity) {

    var realm: Realm
    private val EXPORT_REALM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val path1 = EXPORT_REALM_PATH.toString() + "/" + "kim.json"
    val path2 = EXPORT_REALM_PATH.toString() + "/" + "kmm.json"
    val path3 = EXPORT_REALM_PATH.toString() + "/" + "notif.json"

    init {
        realm = Realm.getDefaultInstance()
    }


    fun Backup(i: IRealmBackup) {
        try {
            val kim = File(EXPORT_REALM_PATH, "kim.json")
            val kmm = File(EXPORT_REALM_PATH, "kmm.json")
            val notif = File(EXPORT_REALM_PATH, "notif.json")

            kim.delete()
            kmm.delete()
            notif.delete()

            val writer1 = BufferedWriter(FileWriter(kim, true))
            val writer2 = BufferedWriter(FileWriter(kmm, true))
            val writer3 = BufferedWriter(FileWriter(notif, true))

            var json1 = ""
            var json2 = ""
            var json3 = ""

            realm.use { realm ->

                val kotlinItemModelResult = realm.where(KotlinItemModel::class.java!!)
                        .equalTo("saved", true).findAll()
                json1 = Gson().toJson(realm.copyFromRealm(kotlinItemModelResult))

                val kotlinMessagesModelResult = realm.where(KotlinMessagesModel::class.java!!)
                        .equalTo("saved", true).findAll()
                json2 = Gson().toJson(realm.copyFromRealm(kotlinMessagesModelResult))


                val kotlinNotificationModel = realm.where(KotlinNotificationModel::class.java).findAll()
                json3 = Gson().toJson(realm.copyFromRealm(kotlinNotificationModel))
            }

            writer1.write(json1)
            writer2.write(json2)
            writer3.write(json3)
            writer1.close()
            writer2.close()
            writer3.close()
            i.onBackup()

        } catch (e: Exception) {
            i.onErrorb()
            e.printStackTrace()
        }
    }

    fun restore(i: IRealmRestore) {


        val str1 = readFile(path1)
        val enums1: Collection<KotlinItemModel>
        val str2 = readFile(path2)
        val enums2: Collection<KotlinMessagesModel>
        val str3 = readFile(path3)
        val enums3: Collection<KotlinNotificationModel>

        if (!str1.equals("") || !str2.equals("") || !str3.equals("")) {
            try {
                val gson = Gson()

                val collectionType1 = object : TypeToken<Collection<KotlinItemModel>>() {

                }.type
                enums1 = gson.fromJson(str1, collectionType1)


                //******************Message*********

                val collectionType2 = object : TypeToken<Collection<KotlinMessagesModel>>() {

                }.type
                enums2 = gson.fromJson(str2, collectionType2)

                //******************notif*********

                val collectionType3 = object : TypeToken<Collection<KotlinNotificationModel>>() {

                }.type
                enums3 = gson.fromJson(str3, collectionType3)


                realm.beginTransaction()

                enums1.forEach { item1 ->
                    realm.copyToRealmOrUpdate(item1)
                }
                enums2.forEach { item2 ->
                    realm.copyToRealmOrUpdate(item2)
                }
                enums3.forEach { item3 ->
                    realm.copyToRealmOrUpdate(item3)
                }

                realm.commitTransaction()

                i.onRestore()


            } catch (e: Exception) {
                i.onErrorr()
                Log.e("onErr", e.toString())
            }
        } else {
            i.onErrorr()
        }
    }


    private fun readFile(path: String): String {
        var uploadedString = ""
        try {
            val inputStream: InputStream = File(path).inputStream()
            val lineList = mutableListOf<String>()

            inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it) } }
            lineList.forEach { uploadedString += it }
            return uploadedString
        } catch (e: Exception) {
            return ""
        }


    }

    interface IRealmBackup {
        fun onBackup()
        fun onErrorb()
    }

    interface IRealmRestore {
        fun onRestore()
        fun onErrorr()
    }


}

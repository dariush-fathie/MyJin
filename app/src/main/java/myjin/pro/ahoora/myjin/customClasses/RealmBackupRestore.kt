package myjin.pro.ahoora.myjin.customClasses

import android.app.Activity
import android.os.Environment
import android.util.Log
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.realm.Realm
import io.realm.RealmObject
import myjin.pro.ahoora.myjin.models.KotlinItemModel
import myjin.pro.ahoora.myjin.models.KotlinMessagesModel
import java.io.*
import com.google.gson.reflect.TypeToken




class RealmBackupRestore(activity: Activity) {

    var realm: Realm
    private val EXPORT_REALM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val path1 = EXPORT_REALM_PATH.toString() + "/" + "kim.json"
    val path2 = EXPORT_REALM_PATH.toString() + "/" + "kmm.json"

    init {
        realm = Realm.getDefaultInstance()
    }



    fun Backup(i: IRealmBackup) {
        try {
            val kim = File(EXPORT_REALM_PATH, "kim.json")
            val kmm = File(EXPORT_REALM_PATH, "kmm.json")

            kim.delete()
            kmm.delete()

            val writer1 = BufferedWriter(FileWriter(kim, true))
            val writer2 = BufferedWriter(FileWriter(kmm, true))

            var json1 = ""
            var json2 = ""

            realm.use { realm ->

                val KotlinItemModelResult = realm.where(KotlinItemModel::class.java!!)
                        .equalTo("saved", true).findAll()
                json1 = Gson().toJson(realm.copyFromRealm(KotlinItemModelResult))

                val KotlinMessagesModelResult = realm.where(KotlinMessagesModel::class.java!!)
                        .equalTo("saved", true).findAll()
                json2 = Gson().toJson(realm.copyFromRealm(KotlinMessagesModelResult))
            }

            writer1.write(json1)
            writer2.write(json2)
            writer2.close()
            writer1.close()
            i.onBackup()

        } catch (e: Exception) {
            i.onErrorb()
            e.printStackTrace()
        }
    }

    fun restore(i: IRealmRestore) {


        var str1 = readFile(path1)
        val enums1: Collection<KotlinItemModel>
        var str2 = readFile(path2)
        val enums2: Collection<KotlinMessagesModel>
        if (!str1.equals("") || !str2.equals("")) {
            try {
                val gson = Gson()

                val collectionType1 = object : TypeToken<Collection<KotlinItemModel>>() {

                }.type
                 enums1 = gson.fromJson(str1, collectionType1)


                //******************Message*********

                val collectionType2 = object : TypeToken<Collection<KotlinMessagesModel>>() {

                }.type
                enums2 = gson.fromJson(str2, collectionType2)



                realm.beginTransaction()

                    enums1.forEach { item1->
                        realm?.copyToRealmOrUpdate(item1)
                    }
                    enums2.forEach { item2->
                        realm?.copyToRealmOrUpdate(item2)
                    }

                realm.commitTransaction()

                    i.onRestore()



            } catch (e: Exception) {
                i.onErrorr()
                Log.e("onErr",e.toString())
            }
        }else{
            i.onErrorr()
        }
    }


    private fun readFile(path: String): String {
        var uploadedString=""
        try{
            val inputStream: InputStream = File(path).inputStream()
            val lineList = mutableListOf<String>()

            inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it)} }
            lineList.forEach{uploadedString+=it}
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

package myjin.pro.ahoora.myjin.customClasses

import android.app.Activity
import android.os.Environment
import android.util.Log
import android.widget.Toast
import io.realm.Realm
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class RealmBackupRestore(private val activity: Activity) {

    interface IRealmBackup {
        fun onBackup()
    }
    interface IRealmRestore {
        fun onRestore()
        fun onError()
    }

    private val EXPORT_REALM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    private val EXPORT_REALM_FILE_NAME = "backup.realm"
    private val IMPORT_REALM_FILE_NAME = "database.realm" // Eventually replace this if you're using a custom db name
    private val realm: Realm = Realm.getDefaultInstance()
    val restoreFilePath = EXPORT_REALM_PATH.toString() + "/" + EXPORT_REALM_FILE_NAME

    fun backup(i: IRealmBackup) {
        val exportRealmFile = File(EXPORT_REALM_PATH, EXPORT_REALM_FILE_NAME)
        Log.e(TAG, "Realm DB Path = " + realm.path)
        EXPORT_REALM_PATH.mkdirs()
        // create a backup file
        // if backup file already exists, delete it
        exportRealmFile.delete()
        // copy current realm to backup file
        realm.writeCopyTo(exportRealmFile)
        Toast.makeText(activity.applicationContext, "BACKUP",
                Toast.LENGTH_LONG).show()
        Log.e(TAG, "BACKUP")
        i.onBackup()
        realm.close()
    }

    fun restore(i: IRealmRestore) {
        Log.e(TAG, "oldFilePath = $restoreFilePath")
        copyBundledRealmFile(restoreFilePath, IMPORT_REALM_FILE_NAME, i)
    }

    private fun copyBundledRealmFile(oldFilePath: String, outFileName: String, i: IRealmRestore): String? {
        try {
            val file = File(activity.applicationContext.filesDir, outFileName)

            val outputStream = FileOutputStream(file)

            val inputStream = FileInputStream(File(oldFilePath))

            val buf = ByteArray(1024)
            var bytesRead = 0
            while ({ bytesRead = inputStream.read(buf);bytesRead }() > 0) {
                outputStream.write(buf, 0, bytesRead)
            }
            outputStream.close()
            i.onRestore()
            return file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            i.onError()
        }
        return null
    }

    companion object {
        private val TAG = RealmBackupRestore::class.java.name
        // Storage Permissions
    }
}
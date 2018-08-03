package myjin.pro.ahoora.myjin.activitys

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_setting.*
import myjin.pro.ahoora.myjin.R
import myjin.pro.ahoora.myjin.customClasses.RealmBackupRestore
import myjin.pro.ahoora.myjin.models.KotlinItemModel
import myjin.pro.ahoora.myjin.models.KotlinMessagesModel
import myjin.pro.ahoora.myjin.models.events.DeleteFavEvent
import myjin.pro.ahoora.myjin.utils.SharedPer
import org.greenrobot.eventbus.EventBus
import java.io.File

class SettingActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener, View.OnClickListener {


    val realmDatabase = Realm.getDefaultInstance()
    lateinit var rbr: RealmBackupRestore
    private var backup = true


    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.sc_intro -> {
                SharedPer(this).setIntro(getString(R.string.introductionFlag2), isChecked)
                SharedPer(this).setBoolean(getString(R.string.introductionFlag), isChecked)
            }
            R.id.rb_centers -> {
                SharedPer(this).setDefTab(getString(R.string.defTab), isChecked)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        onClick_()
        init_()

        if (!checkIfThereIsRestoreFile()) {
            btn_restore.isEnabled = false
        }
    }

    private fun init_() {
        rbr = RealmBackupRestore(this@SettingActivity)
        sc_intro.isChecked = SharedPer(this).getIntro(getString(R.string.introductionFlag2))

        if (SharedPer(this).getDefTab(getString(R.string.defTab)))
            rb_centers.isChecked = SharedPer(this).getDefTab(getString(R.string.defTab))
        else
            rb_messages.isChecked = !SharedPer(this).getDefTab(getString(R.string.defTab))
    }

    private fun onClick_() {
        btn_cleanCenters.setOnClickListener(this)
        btn_cleanMessages.setOnClickListener(this)
        sc_intro.setOnCheckedChangeListener(this)
        rb_centers.setOnCheckedChangeListener(this)
        iv_goback.setOnClickListener(this)
        btn_backup.setOnClickListener(this)
        btn_restore.setOnClickListener(this)
    }

    private fun checkIfThereIsRestoreFile(): Boolean {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + "backup.realm"
        val restoreFile = File(path)
        return restoreFile.exists()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == rwRequest) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (backup) {
                    performBackup()
                } else {
                    performRestore()
                }
            } else {
                Toast.makeText(this, "اجازه دسترسی به حافظه داده نشد", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deleteSaved(tf: Boolean) {
        realmDatabase.executeTransaction { db ->
            if (tf) {
                val item = db.where(KotlinItemModel::class.java)
                        .findAll()!!
                item.forEach { ii ->
                    ii.saved = false
                }
            } else {
                val item = db.where(KotlinMessagesModel::class.java)
                        .findAll()!!
                item.forEach { ii ->
                    ii.saved = false
                }
                EventBus.getDefault().post(DeleteFavEvent())
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_goback -> onBackPressed()
            R.id.btn_backup -> {
                backup = true
                performBackup()
            }
            R.id.btn_restore -> {
                backup = false
                performRestore()
            }
            R.id.btn_cleanCenters -> {
                deleteSaved(true)
            }
            R.id.btn_cleanMessages -> {
                deleteSaved(false)
            }
        }
    }

    private fun performBackup() {
        if (checkStoragePermissions()) {
            showWaitDialog()
            rbr.backup(object : RealmBackupRestore.IRealmBackup {
                override fun onBackup() {
                    closeWaitDialog()
                    showSuccessBackupDialog()
                }
            })
        }
    }

    private fun performRestore() {
        if (checkStoragePermissions()) {
            showWaitDialog()
            rbr.restore(object : RealmBackupRestore.IRealmRestore {
                override fun onRestore() {
                    closeWaitDialog()
                    showCloseDialog()
                }

                override fun onError() {
                    closeWaitDialog()
                    Toast.makeText(this@SettingActivity, "خطای غیرمنتظره", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private lateinit var alertDialog: AlertDialog

    private fun showSuccessBackupDialog() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("پشتیبان گیری با موفقیت انجام شد")
        alertBuilder.setMessage("در صورت نصب مجدد ژین من میتوانید اطلاعات قبلی خود را بازیابی کنید")
        alertBuilder.setPositiveButton("باشه") { dialog, _ ->
            dialog.dismiss()
        }
        alertBuilder.show()
    }

    private fun showWaitDialog() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("لطفا صبر کنید ...")
        alertBuilder.setView(R.layout.progress_dialog)
        alertBuilder.setCancelable(false)
        alertDialog = alertBuilder.create()
        alertDialog.show()
    }

    private fun closeWaitDialog() {
        if (this::alertDialog.isInitialized) {
            alertDialog.dismiss()
        }
    }

    private fun showCloseDialog() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("بازگردانی با موفقیت انجام شد")
        alertBuilder.setMessage("برای اعمال تغییرات از برنامه خارج شوید و دوباره اجرا کنید")
        alertBuilder.setCancelable(false)
        alertBuilder.setPositiveButton("باشه") { _, _ ->
            Log.e("realm count", "${Realm.getGlobalInstanceCount(Realm.getDefaultConfiguration()!!)}")
            realmDatabase.close()
            finish()
        }
        alertBuilder.show()
    }

    private val rwRequest = 1080
    private val rwPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun checkStoragePermissions(): Boolean {
        val write = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val read = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        rwPermissions,
                        rwRequest
                )
                false
            }
        } else {
            // API < 23
            true
        }
    }


}

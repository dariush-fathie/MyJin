package myjin.pro.ahoora.myjin.activitys

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
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


    var isKim = false
    var isKmm = false
    val realmDatabase = Realm.getDefaultInstance()
    lateinit var rbr: RealmBackupRestore
    private var backup = true
    private val EXPORT_REALM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val path1 = EXPORT_REALM_PATH.toString() + "/" + "kim.json"
    val path2 = EXPORT_REALM_PATH.toString() + "/" + "kmm.json"
    private var centersCleanFlag = false
    private var messagesCleanFlag = false

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.sc_intro -> {
                SharedPer(this).setIntro(getString(R.string.introductionFlag2), !isChecked)
                SharedPer(this).setBoolean(getString(R.string.introductionFlag), !isChecked)
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

    }

    private fun init_() {
        rbr = RealmBackupRestore(this@SettingActivity)
        checkIfThereIsRestoreFile()
        sc_intro.isChecked = !SharedPer(this).getIntro(getString(R.string.introductionFlag2))

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
                if (item.size > 0) {
                    centersCleanFlag = true
                }
                item.forEach { ii ->
                    ii.saved = false
                }

            } else {
                val item = db.where(KotlinMessagesModel::class.java)
                        .findAll()!!
                Log.e("item.size", "${item.size}")
                if (item.size > 0) {
                    messagesCleanFlag = true
                }
                item.forEach { ii ->
                    ii.saved = false
                }
                EventBus.getDefault().post(DeleteFavEvent())
            }
        }
    }

    private fun checkIfThereIsRestoreFile() {

        val restoreFile1 = File(path1)
        val restoreFile2 = File(path2)
        isKim = restoreFile1.exists()
        isKmm = restoreFile2.exists()

        if (!(isKim && isKmm)) {
            btn_restore.isEnabled = false
        }else{
            btn_restore.isEnabled = true
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
            rbr.Backup(object : RealmBackupRestore.IRealmBackup {

                override fun onBackup() {
                    closeWaitDialog()
                    showSuccessBackupDialog()
                }

                override fun onErrorb() {
                    closeWaitDialog()
                    Toast.makeText(this@SettingActivity, "خطای غیرمنتظره", Toast.LENGTH_LONG).show()
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

                override fun onErrorr() {
                    closeWaitDialog()
                    Toast.makeText(this@SettingActivity, "خطای غیرمنتظره", Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    private lateinit var alertDialog: AlertDialog



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

        val builder = AlertDialog.Builder(this@SettingActivity)
        val dialog: AlertDialog
        val view = View.inflate(this@SettingActivity, R.layout.restore_layout, null)
        val btn_ok: AppCompatButton = view.findViewById(R.id.btn_ok)
        builder.setView(view)
        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val listener = View.OnClickListener { v ->
            when (v.id) {
                R.id.btn_ok -> {
                    dialog.dismiss()
                    centersCleanFlag = true
                    messagesCleanFlag=true
                    realmDatabase.close()
                    onBackPressed()
                }

            }
        }
        btn_ok.setOnClickListener(listener)
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

    override fun onBackPressed() {
        if (messagesCleanFlag || centersCleanFlag) {
            val resIntent = Intent()
            resIntent.putExtra(getString(R.string.messagesClean), messagesCleanFlag)
            resIntent.putExtra(getString(R.string.centersClean), centersCleanFlag)
            setResult(AppCompatActivity.RESULT_OK, resIntent)
        }
        super.onBackPressed()
    }


    private fun showSuccessBackupDialog() {

        val builder = AlertDialog.Builder(this@SettingActivity)
        val dialog: AlertDialog
        val view = View.inflate(this@SettingActivity, R.layout.backup_layout, null)
        val btn_ok: AppCompatButton = view.findViewById(R.id.btn_ok)
        builder.setView(view)
        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val listener = View.OnClickListener { v ->
            when (v.id) {
                R.id.btn_ok -> {
                    checkIfThereIsRestoreFile()
                    dialog.dismiss()
                }

            }
        }
        btn_ok.setOnClickListener(listener)
    }

}

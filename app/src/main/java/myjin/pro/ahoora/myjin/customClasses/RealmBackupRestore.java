package myjin.pro.ahoora.myjin.customClasses;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;
import myjin.pro.ahoora.myjin.R;

public class RealmBackupRestore {

    private File EXPORT_REALM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private String EXPORT_REALM_FILE_NAME = "glucosio.realm";
    private String IMPORT_REALM_FILE_NAME = "database.realm"; // Eventually replace this if you're using a custom db name

    private final static String TAG = RealmBackupRestore.class.getName();

    private Activity activity;
    private Realm realm;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public RealmBackupRestore(Activity activity) {
        this.realm = Realm.getDefaultInstance();
        this.activity = activity;
    }

    public void backup() {


        File exportRealmFile;

        Log.e(TAG, "Realm DB Path = " + realm.getPath());

        EXPORT_REALM_PATH.mkdirs();

        // create a backup file
        exportRealmFile = new File(EXPORT_REALM_PATH, EXPORT_REALM_FILE_NAME);

        // if backup file already exists, delete it
        exportRealmFile.delete();

        // copy current realm to backup file
        realm.writeCopyTo(exportRealmFile);


        Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.msgBackup),
                Toast.LENGTH_LONG).show();
        Log.e(TAG, activity.getResources().getString(R.string.msgBackup));

        realm.close();
    }

    public void restore() {
        checkStoragePermissions(activity);
        //Restore
        String restoreFilePath = EXPORT_REALM_PATH + "/" + EXPORT_REALM_FILE_NAME;

        Log.e(TAG, "oldFilePath = " + restoreFilePath);

        copyBundledRealmFile(restoreFilePath, IMPORT_REALM_FILE_NAME);
    }

    private String copyBundledRealmFile(String oldFilePath, String outFileName) {
        try {
            File file = new File(activity.getApplicationContext().getFilesDir(), outFileName);

            FileOutputStream outputStream = new FileOutputStream(file);

            FileInputStream inputStream = new FileInputStream(new File(oldFilePath));

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.msgRestore), Toast.LENGTH_LONG).show();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.msgNotRestore), Toast.LENGTH_LONG).show();

        }
        return null;
    }

    public Boolean checkStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permission == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(
                        activity,
                PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
                Toast.makeText(activity,activity.getResources().getString(R.string.permission), Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            return true;
        }
    }
    private String dbPath(){
        return realm.getPath();
    }
}
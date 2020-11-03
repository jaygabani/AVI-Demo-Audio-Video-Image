package com.application.jump360.Utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Util {

    public static final int REQUEST_PERMISSION_CODE = 200;
    public static final int REQUEST_VIDEO_CODE = 1;
    public static final int REQUEST_AUDIO_CODE = 2;
    public static final int REQUEST_IMAGE_CODE = 3;

    private static final Util ourInstance = new Util();

    public static Util getInstance() {
        return ourInstance;
    }

    private Util() {
    }

    public boolean checkPermission(Activity activity) {
        int write = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE);

        return write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
    }

    public void comnToast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

}

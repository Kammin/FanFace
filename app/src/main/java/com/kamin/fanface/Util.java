package com.kamin.fanface;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import static com.kamin.fanface.Constants.RequestCodeCamera;

public class Util {

    public static boolean hasHardwareCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    public static Camera getCamera(Context context) {
        Camera camera = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions((MainActivity) context, new String[]{Manifest.permission.CAMERA}, RequestCodeCamera);
        try {
            camera = Camera.open(1);
        } catch (Exception e) {
            Log.d("", "Camera not avalable " + Camera.getNumberOfCameras() + " " + ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA));
        }
        return camera;
    }
}

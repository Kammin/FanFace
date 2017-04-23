package com.kamin.fanface;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Util.hasHardwareCamera(this))
            Log.d("---------", "has Hardware Camera");

        camera = Util.getCamera(this);

        if (camera != null)
            Log.d("---------", "camera not null");
        else
            Log.d("---------", "camera is null");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.RequestCodeCamera)
            if (grantResults.length > 0)
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    camera = Util.getCamera(this);
                else
                    finish();

        if (camera != null)
            Log.d("---------", "camera not null");
        else
            Log.d("---------", "camera is null");
    }
}

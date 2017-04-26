package com.kamin.fanface;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.kamin.fanface.Constants.RequestCodeCamera;
import static com.kamin.fanface.Constants.RequestCodeWriteExtStorage;

public class MainActivity extends AppCompatActivity {
    Camera camera;
    FrameLayout camPreview;
    Camera.PictureCallback pictureCallback;
    Button btSnap;

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                ActivityCompat.requestPermissions((MainActivity) this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestCodeWriteExtStorage);

        if (Util.hasHardwareCamera(this))
            Log.d("---------", "has Hardware Camera");

        camera = Util.getCamera(this, camera);

        if (camera != null)
            Log.d("---------", "camera not null");
        else
            Log.d("---------", "camera is null");

        CameraPreview preview = new CameraPreview(this, camera);
        camPreview = (FrameLayout) findViewById(R.id.flPreview);
        camera.setDisplayOrientation(90);
        camPreview.addView(preview);

        pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                File filePicture = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                Log.d(TAG," Absolute Path "+filePicture.getAbsolutePath());
                if (filePicture == null) {
                    Log.d(TAG, "Error creating media file, check permission settings");
                    return;
                }

                try {
                    FileOutputStream fos = new FileOutputStream(filePicture);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace() ;
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };
        btSnap = (Button) findViewById(R.id.btSnap);
        btSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null,null,pictureCallback);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RequestCodeCamera)
            if (grantResults.length > 0)
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    camera = Util.getCamera(this);
                else
                    finish();
        if (requestCode == Constants.RequestCodeWriteExtStorage)
            if (grantResults.length > 0)
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    camera = Util.getCamera(this);
                else
                    finish();

    }


    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "FanFace");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}

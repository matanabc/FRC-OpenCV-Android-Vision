package com.example.visiondroid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.visiondroid.vision.Consumer;
import com.example.visiondroid.vision.VisionConstant;
import com.example.visiondroid.vision.server.MjpgServer;
import com.example.visiondroid.vision.server.VisionConstantServer;
import com.example.visiondroid.vision.server.VisionDataServer;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

import androidx.core.app.ActivityCompat;

public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private CameraBridgeViewBase mOpenCvCameraView;

    private VisionConstantServer server;
    private Consumer consumer;

    private static int batteryLevel = 0;
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        public void onReceive(Context arg0, Intent intent) {
            batteryLevel = intent.getIntExtra("level", 0);
        }
    };

    /**
     * Called when the activity is first created.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setMaxFrameSize(VisionConstant.MAX_FRAME_WIDTH, VisionConstant.MAX_FRAME_HEIGHT);
        mOpenCvCameraView.enableView();
        OpenCVLoader.initDebug();

        try {
            VisionConstant.load(getSharedPreferences("vision", 0));
            consumer = new Consumer();
            server = new VisionConstantServer(getAssets().open("vision_config.html"));
            Toast.makeText(getApplicationContext(), Util.getIPAddress(true), Toast.LENGTH_LONG).show();
            MjpgServer.getInstance().startServer();
            VisionDataServer.getInstance().startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onPause() {
        super.onPause();
        ((ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)).moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        // processing image
        Mat img = consumer.execute(inputFrame.rgba());

        // draw on frame phone battery level and FPS
        Imgproc.putText(img, batteryLevel + "%", VisionConstant.BATTERY_LEVEL_POINT, Core.FONT_HERSHEY_PLAIN, 1, VisionConstant.WHITE);
        Imgproc.putText(img, Util.getFPSCount(), VisionConstant.FPS_POINT, Core.FONT_HERSHEY_PLAIN, 1, VisionConstant.WHITE);

        MjpgServer.getInstance().sendMat(Util.mat2ByteArray(img));

        return img;
    }
}
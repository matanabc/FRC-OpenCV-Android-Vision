package com.example.visiondroid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.visiondroid.vision.Consumer;
import com.example.visiondroid.vision.VisionConstant;
import com.example.visiondroid.vision.server.MjpgServer;
import com.example.visiondroid.vision.server.VisionConstantServer;
import com.example.visiondroid.vision.server.VisionDataServer;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

import androidx.core.app.ActivityCompat;

public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";
    private CameraBridgeViewBase mOpenCvCameraView;

    private VisionConstantServer server;
    private Consumer consumer;

    private static int batteryLevel = 0;
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        public void onReceive(Context arg0, Intent intent) {
            batteryLevel = intent.getIntExtra("level", 0);
        }
    };


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    /**
     * Called when the activity is first created.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        mOpenCvCameraView.setMaxFrameSize(320, 240);
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
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
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
        Mat img = consumer.execute(inputFrame.rgba());
        Imgproc.putText(img, batteryLevel + "%", VisionConstant.BATTERY_LEVEL_POINT, Core.FONT_HERSHEY_PLAIN, 1, VisionConstant.WHITE); // draw on frame phone battery level
        Imgproc.putText(img, Util.getFPSCount(), VisionConstant.FPS_POINT, Core.FONT_HERSHEY_PLAIN, 1, VisionConstant.WHITE); // draw on frame FPS
        if (!VisionDataServer.getInstance().isHaveConnection())
            Imgproc.putText(img, "Don't Have Connection!", VisionConstant.HAVE_CONNECTION_POINT, Core.FONT_HERSHEY_PLAIN, 1, VisionConstant.WHITE); // draw on frame if have connection
        MjpgServer.getInstance().sendMat(Util.mat2ByteArray(img));
        return img;
    }
}
package com.example.visiondroid.vision;

import android.content.SharedPreferences;

import org.opencv.core.Point;
import org.opencv.core.Scalar;

import java.io.IOException;

public class VisionConstant {
    public static final Scalar WHITE = new Scalar(255, 255, 255);
    public static final Scalar RED = new Scalar(255, 0, 0);
    public static final Scalar GREEN = new Scalar(0, 255, 0);
    public static final Scalar BLUE = new Scalar(0, 0, 255);

    public static final Point BATTERY_LEVEL_POINT = new Point(5, 15);
    public static final Point FPS_POINT = new Point(5, 30);
    public static final Point HAVE_CONNECTION_POINT = new Point(5, 45);

    public static final int MAX_FRAME_WIDTH = 320;
    public static final int MAX_FRAME_HEIGHT = 240;
    public static final int MAX_FRAME_SIZE = MAX_FRAME_HEIGHT * MAX_FRAME_WIDTH;

    public static boolean showHSV = false;

    private static int hMin = 0, sMin = 0, vMin = 0;
    private static int hMax = 255, sMax = 255, vMax = 255;

    public static double minArea = 0, maxArea = 100;
    public static double minRatio = 0, maxRatio = 100;
    public static double minWidth = 0, maxWidth = 100;
    public static double minHeight = 0, maxHeight = 100;

    public static Scalar thresholdMin = new Scalar(hMin, sMin, vMin), thresholdMax = new Scalar(hMax, sMax, vMax);

    public static int numberTargetContours = 1;

    public static double targetCenterXInFrame = MAX_FRAME_WIDTH / (double) 2;
    public static double targetCenterYInFrame = MAX_FRAME_HEIGHT / (double) 2;

    public static double pixel2AngleX = 65 / (double) MAX_FRAME_WIDTH;
    public static double pixel2AngleY = 50 / (double) MAX_FRAME_HEIGHT;

    private static SharedPreferences visionPreferences;
    private static SharedPreferences.Editor visionPreferencesEditor;

    public static void load(SharedPreferences preferences) throws IOException {
        visionPreferences = preferences;
        visionPreferencesEditor = visionPreferences.edit();

        showHSV = preferences.getBoolean("showHSV", false);
        numberTargetContours = preferences.getInt("numberTargetContours", 1);

        hMin = preferences.getInt("hMin", 0);
        hMax = preferences.getInt("hMax", 255);

        sMin = preferences.getInt("sMin", 0);
        sMax = preferences.getInt("sMax", 255);

        vMin = preferences.getInt("vMin", 0);
        vMax = preferences.getInt("vMax", 255);

        minArea = Double.parseDouble(preferences.getString("minArea", "0"));
        maxArea = Double.parseDouble(preferences.getString("maxArea", "100"));

        minRatio = Double.parseDouble(preferences.getString("minRatio", "0"));
        maxRatio = Double.parseDouble(preferences.getString("maxRatio", "100"));

        minWidth = Double.parseDouble(preferences.getString("minWidth", "0"));
        maxWidth = Double.parseDouble(preferences.getString("maxWidth", "100"));

        minHeight = Double.parseDouble(preferences.getString("minHeight", "0"));
        maxHeight = Double.parseDouble(preferences.getString("maxHeight", "100"));

        pixel2AngleX = Double.parseDouble(preferences.getString("pixel2AngleX", String.valueOf(pixel2AngleX)));
        pixel2AngleY = Double.parseDouble(preferences.getString("pixel2AngleY", String.valueOf(pixel2AngleY)));

        thresholdMin = new Scalar(hMin, sMin, vMin);
        thresholdMax = new Scalar(hMax, sMax, vMax);
    }

    public static void update(String key, String value) throws Exception {
        if (key.equals("hMin")) {
            hMin = Integer.parseInt(value);
            thresholdMin = new Scalar(hMin, sMin, vMin);
            visionPreferencesEditor.putInt("hMin", hMin);
        } else if (key.equals("hMax")) {
            hMax = Integer.parseInt(value);
            thresholdMax = new Scalar(hMax, sMax, vMax);
            visionPreferencesEditor.putInt("hMax", hMax);
        } else if (key.equals("sMin")) {
            sMin = Integer.parseInt(value);
            thresholdMin = new Scalar(hMin, sMin, vMin);
            visionPreferencesEditor.putInt("sMin", sMin);
        } else if (key.equals("sMax")) {
            sMax = Integer.parseInt(value);
            thresholdMax = new Scalar(hMax, sMax, vMax);
            visionPreferencesEditor.putInt("sMax", sMax);
        } else if (key.equals("vMin")) {
            vMin = Integer.parseInt(value);
            thresholdMin = new Scalar(hMin, sMin, vMin);
            visionPreferencesEditor.putInt("vMin", vMin);
        } else if (key.equals("vMax")) {
            vMax = Integer.parseInt(value);
            thresholdMax = new Scalar(hMax, sMax, vMax);
            visionPreferencesEditor.putInt("vMax", vMax);
        } else if (key.equals("minArea")) {
            minArea = Double.parseDouble(value);
            visionPreferencesEditor.putString("minArea", String.valueOf(minArea));
        } else if (key.equals("maxArea")) {
            maxArea = Double.parseDouble(value);
            visionPreferencesEditor.putString("maxArea", String.valueOf(maxArea));
        } else if (key.equals("minRatio")) {
            minRatio = Double.parseDouble(value);
            visionPreferencesEditor.putString("minRatio", String.valueOf(minRatio));
        } else if (key.equals("maxRatio")) {
            maxRatio = Double.parseDouble(value);
            visionPreferencesEditor.putString("maxRatio", String.valueOf(maxRatio));
        } else if (key.equals("minWidth")) {
            minWidth = Double.parseDouble(value);
            visionPreferencesEditor.putString("minWidth", String.valueOf(minWidth));
        } else if (key.equals("maxWidth")) {
            maxWidth = Double.parseDouble(value);
            visionPreferencesEditor.putString("maxWidth", String.valueOf(maxWidth));
        } else if (key.equals("minHeight")) {
            minHeight = Double.parseDouble(value);
            visionPreferencesEditor.putString("minHeight", String.valueOf(minHeight));
        } else if (key.equals("maxHeight")) {
            maxHeight = Double.parseDouble(value);
            visionPreferencesEditor.putString("maxHeight", String.valueOf(maxHeight));
        } else if (key.equals("showHSV")) {
            showHSV = Boolean.parseBoolean(value);
            visionPreferencesEditor.putBoolean("showHSV", showHSV);
        } else if (key.equals("numberTargetContours")) {
            numberTargetContours = Integer.parseInt(value);
            visionPreferencesEditor.putInt("numberTargetContours", numberTargetContours);
        } else if (key.equals("AngleX")) {
            pixel2AngleX = Double.parseDouble(value) / (double) MAX_FRAME_WIDTH;
            visionPreferencesEditor.putString("pixel2AngleX", String.valueOf(pixel2AngleX));
        } else if (key.equals("AngleY")) {
            pixel2AngleY = Double.parseDouble(value) / (double) MAX_FRAME_HEIGHT;
            visionPreferencesEditor.putString("pixel2AngleY", String.valueOf(pixel2AngleY));
        }

        visionPreferencesEditor.commit();
    }
}

package com.example.opencvvisiontest.vision;

import org.opencv.core.Scalar;

public class VisionConstant {
    public static boolean showHSV = false;

    private static double hMin = 0, sMin = 0, vMin = 0;
    private static double hMax = 255, sMax = 255, vMax = 255;

    public static double minArea = 0, maxArea = 100;
    public static double minRatio = 0, maxRatio = 10;
    public static double minWidth = 0, maxWidth = 100;
    public static double minHeight = 0, maxHeight = 100;

    public static Scalar thresholdMin = new Scalar(hMin, sMin, vMin), thresholdMax = new Scalar(hMax, sMax, vMax);

    public static void update(String key, String value) {
        if (key.equals("hMin")) {
            hMin = Integer.parseInt(value);
            thresholdMin = new Scalar(hMin, sMin, vMin);
        } else if (key.equals("hMax")) {
            hMax = Integer.parseInt(value);
            thresholdMax = new Scalar(hMax, sMax, vMax);
        } else if (key.equals("sMin")) {
            sMin = Integer.parseInt(value);
            thresholdMin = new Scalar(hMin, sMin, vMin);
        } else if (key.equals("sMax")) {
            sMax = Integer.parseInt(value);
            thresholdMax = new Scalar(hMax, sMax, vMax);
        } else if (key.equals("vMin")) {
            vMin = Integer.parseInt(value);
            thresholdMin = new Scalar(hMin, sMin, vMin);
        } else if (key.equals("vMax")) {
            vMax = Integer.parseInt(value);
            thresholdMax = new Scalar(hMax, sMax, vMax);
        }

        else if (key.equals("minArea")) minArea = Double.parseDouble(value);
        else if (key.equals("maxArea")) maxArea = Double.parseDouble(value);

        else if (key.equals("minRatio")) minRatio = Double.parseDouble(value);
        else if (key.equals("maxRatio")) maxRatio = Double.parseDouble(value);

        else if (key.equals("minWidth")) minWidth = Double.parseDouble(value);
        else if (key.equals("maxWidth")) maxWidth = Double.parseDouble(value);

        else if (key.equals("minHeight")) minHeight = Double.parseDouble(value);
        else if (key.equals("maxHeight")) maxHeight = Double.parseDouble(value);

        else if (key.equals("showHSV")) showHSV = Boolean.parseBoolean(value);
    }
}

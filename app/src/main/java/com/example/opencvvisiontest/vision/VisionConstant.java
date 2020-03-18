package com.example.opencvvisiontest.vision;

import org.opencv.core.Scalar;

public class VisionConstant {
    public static boolean showHSV = false;

    private static double hMin = 0, sMin = 0, vMin = 0;
    private static double hMax = 255, sMax = 255, vMax = 255;

    public static Scalar thresholdMin = new Scalar(hMin, sMin, vMin), thresholdMax = new Scalar(hMax, sMax, vMax);

    public static void update(String key, String value){
        if(key.equals("hMin")){
            hMin = Integer.parseInt(value);
            thresholdMin = new Scalar(hMin, sMin, vMin);
        } else if(key.equals("hMax")){
            hMax = Integer.parseInt(value);
            thresholdMax = new Scalar(hMax, sMax, vMax);
        }

        else if(key.equals("sMin")){
            sMin = Integer.parseInt(value);
            thresholdMin = new Scalar(hMin, sMin, vMin);
        } else if(key.equals("sMax")){
            sMax = Integer.parseInt(value);
            thresholdMax = new Scalar(hMax, sMax, vMax);
        }

        else if(key.equals("vMin")){
            vMin = Integer.parseInt(value);
            thresholdMin = new Scalar(hMin, sMin, vMin);
        } else if(key.equals("vMax")){
            vMax = Integer.parseInt(value);
            thresholdMax = new Scalar(hMax, sMax, vMax);
        }

        else if(key.equals("showHSV")) showHSV = Boolean.parseBoolean(value);
    }
}

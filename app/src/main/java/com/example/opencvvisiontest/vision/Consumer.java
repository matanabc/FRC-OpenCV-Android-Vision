package com.example.opencvvisiontest.vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Consumer {
    private Mat threshold;
    private List<MatOfPoint> thresholdContours, contours;

    private Scalar thresholdMin, thresholdMax;

    private double minArea = 0, maxArea = 100;
    private double minRatio = 0, maxRatio = 10;
    private double minWidth = 0, maxWidth = 100;
    private double minHeight = 0, maxHeight = 100;

    public Consumer() {
        threshold = new Mat();
        thresholdContours = new ArrayList<MatOfPoint>();
        contours = new ArrayList<MatOfPoint>();

        thresholdMin = new Scalar(25, 0, 0);
        thresholdMax = new Scalar(200, 255, 255);
    }

    public Mat consume(Mat img) {
        Imgproc.cvtColor(img, threshold, Imgproc.COLOR_BGR2HSV); //Change from rgb to hsv

        threshold(threshold);
        dilation(threshold);
        findContours(threshold, thresholdContours);
        filterContours(thresholdContours, contours);

        for (int i = 0; i < contours.size(); i++) {
            Imgproc.drawContours(img, contours, i, new Scalar(0, 255, 0));
        }

        return threshold;
    }

    private void threshold(Mat input) {
        Core.inRange(input, thresholdMin, thresholdMax, input);
    }

    private void dilation(Mat input) {
        Mat kernel = new Mat();
        Point anchor = new Point(-1, -1);
        Scalar borderValue = new Scalar(-1);

        Imgproc.dilate(input, input, kernel, anchor, 1, Core.BORDER_CONSTANT, borderValue);
    }

    private void erode(Mat input, Mat out) {
        Mat kernel = new Mat();
        Point anchor = new Point(-1, -1);
        Scalar borderValue = new Scalar(-1);

        Imgproc.erode(input, out, kernel, anchor, 1, Core.BORDER_CONSTANT, borderValue);
    }

    private void findContours(Mat input, List<MatOfPoint> contours) {
        Mat hierarchy = new Mat();
        contours.clear();
        Imgproc.findContours(input, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
    }

    private void filterContours(List<MatOfPoint> inputContours, List<MatOfPoint> output) {
        final MatOfInt hull = new MatOfInt();
        output.clear();

        for (int i = 0; i < inputContours.size(); i++) {
            final MatOfPoint contour = inputContours.get(i);

            final Rect bb = Imgproc.boundingRect(contour);
            final double width = bb.width / 320 * 100;
            final double height = bb.height / 240 * 100;
            if (width < minWidth || width > maxWidth) continue;
            if (height < minHeight || height > maxHeight) continue;

            final double area = Imgproc.contourArea(contour);
            if (area < minArea || area > maxArea) continue;

            final double ratio = width / (double) height * 10;
            if (ratio < minRatio || ratio > maxRatio) continue;

            output.add(contour);
        }
    }
}

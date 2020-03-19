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
    private PostConsumer postConsumer;

    public Consumer() {
        threshold = new Mat();
        thresholdContours = new ArrayList<MatOfPoint>();
        contours = new ArrayList<MatOfPoint>();
        postConsumer = new PostConsumer();
    }

    public Mat execute(Mat img) {
        Imgproc.cvtColor(img, threshold, Imgproc.COLOR_BGR2HSV); //Change from rgb to hsv

        threshold(threshold);
        dilation(threshold);
        findContours(threshold, thresholdContours);
        filterContours(thresholdContours, contours);

        return postConsumer.execute(threshold, img, contours);
    }

    private void threshold(Mat input) {
        Core.inRange(input, VisionConstant.thresholdMin, VisionConstant.thresholdMax, input);
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
            if (width < VisionConstant.minWidth || width > VisionConstant.maxWidth) continue;
            if (height < VisionConstant.minHeight || height > VisionConstant.maxHeight) continue;

            final double area = Imgproc.contourArea(contour) / (320 * 240) * 100;
            if (area < VisionConstant.minArea || area > VisionConstant.maxArea) continue;

            final double ratio = width / (double) height * 10;
            if (ratio < VisionConstant.minRatio || ratio > VisionConstant.maxRatio) continue;

            output.add(contour);
        }
    }
}

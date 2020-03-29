package com.example.visiondroid.vision;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
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
        Imgproc.findContours(input, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_TC89_KCOS);
    }

    private void filterContours(List<MatOfPoint> inputContours, List<MatOfPoint> output) {
        MatOfInt hull = new MatOfInt();
        output.clear();

        for (int i = 0; i < inputContours.size(); i++) {
            final MatOfPoint contour = inputContours.get(i);

            final Rect bb = Imgproc.boundingRect(contour);
            final double width = bb.width / (double) VisionConstant.MAX_FRAME_WIDTH * 100;
            final double height = bb.height / (double) VisionConstant.MAX_FRAME_HEIGHT * 100;
            if (width < VisionConstant.minWidth || width > VisionConstant.maxWidth) continue;
            if (height < VisionConstant.minHeight || height > VisionConstant.maxHeight) continue;

            final double area = Imgproc.contourArea(contour) / (VisionConstant.MAX_FRAME_SIZE) * 100;
            if (area < VisionConstant.minArea || area > VisionConstant.maxArea) continue;

            final double ratio = width / height * 100;
            if (ratio < VisionConstant.minRatio || ratio > VisionConstant.maxRatio) continue;

            Imgproc.convexHull(contour, hull);
            MatOfPoint mopHull = new MatOfPoint();
            mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
            for (int j = 0; j < hull.size().height; j++) {
                int index = (int) hull.get(j, 0)[0];
                double[] point = new double[] { contour.get(index, 0)[0], contour.get(index, 0)[1] };
                mopHull.put(j, 0, point);
            }
            final double solidity = area / (Imgproc.contourArea(mopHull) / (VisionConstant.MAX_FRAME_SIZE) * 100) * 100;
            if (solidity < VisionConstant.minSolidity || ratio > VisionConstant.maxSolidity) continue;

            output.add(contour);
        }
    }
}

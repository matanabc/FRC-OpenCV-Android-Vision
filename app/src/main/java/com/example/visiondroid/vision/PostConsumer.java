package com.example.visiondroid.vision;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PostConsumer {
    private TargetInfo targetInfo = new TargetInfo();

    public Mat execute(Mat threshold, Mat img, List<MatOfPoint> contours) {
        try {
            sortLeft2Right(contours);
            drawContours(img, contours);

            if (contours.size() == VisionConstant.numberTargetContours) {
                targetInfo = new TargetInfo(contours);
                targetInfo.drawTarget(img);
            } else {
                targetInfo = new TargetInfo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        drawFrameCenter(img);
        targetInfo.sendTargetInfo();
        return VisionConstant.showHSV ? threshold : img;
    }

    /**
     * This function sort contours from left to right (smaller to bigger X)
     *
     * @param contours that found
     */
    private void sortLeft2Right(List<MatOfPoint> contours) {
        Collections.sort(contours, new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint o1, MatOfPoint o2) {
                Rect rect1 = Imgproc.boundingRect(o1);
                Rect rect2 = Imgproc.boundingRect(o2);
                return Double.compare(rect1.tl().x, rect2.tl().x);
            }
        });
    }

    private void drawContours(Mat img, List<MatOfPoint> contours) {
        for (int i = 0; i < contours.size(); i++) {
            Imgproc.drawContours(img, contours, i, VisionConstant.GREEN);
        }
    }

    private void drawFrameCenter(Mat img) {
        double center_x = VisionConstant.targetCenterXInFrame;
        double center_y = VisionConstant.targetCenterYInFrame;

        Imgproc.line(img, new Point(center_x, center_y + 10), new Point(center_x, center_y + 20), VisionConstant.GREEN, 2);
        Imgproc.line(img, new Point(center_x, center_y - 10), new Point(center_x, center_y - 20), VisionConstant.GREEN, 2);
        Imgproc.line(img, new Point(center_x + 10, center_y), new Point(center_x + 20, center_y), VisionConstant.GREEN, 2);
        Imgproc.line(img, new Point(center_x - 10, center_y), new Point(center_x - 20, center_y), VisionConstant.GREEN, 2);
    }

    /**
     * This function will take the center parse of FRC 2019 target.
     *
     * @param img      to draw on
     * @param contours that found
     */
    private void calculate2019TargetInfo(Mat img, List<MatOfPoint> contours) {
        List<TargetInfo> parse = new ArrayList<>();
        for (int i = 1; i < contours.size(); i++) {
            if (Imgproc.fitEllipse(new MatOfPoint2f(contours.get(i - 1).toArray())).angle < 90 && Imgproc.fitEllipse(new MatOfPoint2f(contours.get(i).toArray())).angle > 90) {
                parse.add(new TargetInfo(contours.get(i - 1), contours.get(i)));
            }
        }

        if (parse.size() == 0) {
            TargetInfo targetInfo = new TargetInfo();
            drawFrameCenter(img);
            targetInfo.sendTargetInfo();
        }

        // sort -> first parse is the closest to the center
        Collections.sort(parse, new Comparator<TargetInfo>() {
            @Override
            public int compare(TargetInfo o1, TargetInfo o2) {
                return Double.compare(Math.abs(VisionConstant.targetCenterXInFrame - o1.targetCenterX), Math.abs(VisionConstant.targetCenterXInFrame - o2.targetCenterX));
            }
        });

        parse.get(0).drawTarget(img);
        drawFrameCenter(img);
        parse.get(0).sendTargetInfo();
    }
}

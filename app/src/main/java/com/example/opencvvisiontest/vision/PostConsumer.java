package com.example.opencvvisiontest.vision;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class PostConsumer {
    public Mat execute(Mat threshold, Mat img, List<MatOfPoint> contours) {
        try {
            drawContours(img, contours);
            if (contours.size() == VisionConstant.numberTargetContours) {
                drawTarget(img, contours);
            }
            drawFrameCenter(img);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return VisionConstant.showHSV ? threshold : img;
    }

    public void drawFrameCenter(Mat img) {
        double center_x = VisionConstant.MAX_FRAME_WIDTH / 2;
        double center_y = VisionConstant.MAX_FRAME_HEIGHT / 2;

        Imgproc.line(img, new Point(center_x, center_y + 10), new Point(center_x, center_y + 20), VisionConstant.GREEN, 2);
        Imgproc.line(img, new Point(center_x, center_y - 10), new Point(center_x, center_y - 20), VisionConstant.GREEN, 2);
        Imgproc.line(img, new Point(center_x + 10, center_y), new Point(center_x + 20, center_y), VisionConstant.GREEN, 2);
        Imgproc.line(img, new Point(center_x - 10, center_y), new Point(center_x - 20, center_y), VisionConstant.GREEN, 2);
    }

    public void drawContours(Mat img, List<MatOfPoint> contours) {
        for (int i = 0; i < contours.size(); i++) {
            Imgproc.drawContours(img, contours, i, VisionConstant.GREEN);
        }
    }


    private double center_x, center_y, x1, x2, y1, y2;
    private Rect r;

    public void drawTarget(Mat img, List<MatOfPoint> contours) {
        r = Imgproc.boundingRect(contours.get(0));
        x1 = r.tl().x;
        x2 = r.br().x;
        y1 = r.tl().y;
        y2 = r.br().y;

        for (int i = 1; i < contours.size(); i++) {
            r = Imgproc.boundingRect(contours.get(i));
            x1 = Math.min(r.tl().x, x1);
            x2 = Math.max(r.br().x, x2);
            y1 = Math.min(r.tl().y, y1);
            y2 = Math.max(r.br().y, y2);
        }
        center_x = (x1 + x2) / 2;
        center_y = (y1 + y2) / 2;

        Imgproc.rectangle(img, new Point(x1, y1), new Point(x2, y2), VisionConstant.BLUE, 2); //print the area from left point and the right point of the contor that he fond
        Imgproc.rectangle(img, new Point(center_x, center_y), new Point(center_x, center_y), VisionConstant.RED, 3); //print the centers
    }
}

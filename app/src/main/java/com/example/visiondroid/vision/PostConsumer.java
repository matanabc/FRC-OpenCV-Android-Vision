package com.example.visiondroid.vision;

import com.example.visiondroid.vision.server.VisionDataServer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PostConsumer {
    private double targetCenterX = 0, targetCenterY = 0, xError = 0, yError = 0;
    private Rect r;
    private Point targetTopLeft = new Point(0, 0), targetBottomRight = new Point(0, 0);

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

        VisionDataServer.getInstance().sendData(contours.size() == VisionConstant.numberTargetContours, xError, yError);
        return VisionConstant.showHSV ? threshold : img;
    }

    private void drawFrameCenter(Mat img) {
        double center_x = VisionConstant.targetCenterXInFrame;
        double center_y = VisionConstant.targetCenterYInFrame;

        Imgproc.line(img, new Point(center_x, center_y + 10), new Point(center_x, center_y + 20), VisionConstant.GREEN, 2);
        Imgproc.line(img, new Point(center_x, center_y - 10), new Point(center_x, center_y - 20), VisionConstant.GREEN, 2);
        Imgproc.line(img, new Point(center_x + 10, center_y), new Point(center_x + 20, center_y), VisionConstant.GREEN, 2);
        Imgproc.line(img, new Point(center_x - 10, center_y), new Point(center_x - 20, center_y), VisionConstant.GREEN, 2);
    }

    private void drawContours(Mat img, List<MatOfPoint> contours) {
        for (int i = 0; i < contours.size(); i++) {
            Imgproc.drawContours(img, contours, i, VisionConstant.GREEN);
        }
    }

    private void drawTarget(Mat img, List<MatOfPoint> contours) {
        r = Imgproc.boundingRect(contours.get(0));
        targetTopLeft.x = r.tl().x;
        targetTopLeft.y = r.tl().y;
        targetBottomRight.x = r.br().x;
        targetBottomRight.y = r.br().y;

        for (int i = 1; i < contours.size(); i++) {
            r = Imgproc.boundingRect(contours.get(i));
            targetTopLeft.x = Math.min(r.tl().x, targetTopLeft.x);
            targetTopLeft.y = Math.min(r.tl().y, targetTopLeft.y);
            targetBottomRight.x = Math.max(r.br().x, targetBottomRight.x);
            targetBottomRight.y = Math.max(r.br().y, targetBottomRight.y);
        }
        targetCenterX = (targetTopLeft.x + targetBottomRight.x) / 2;
        targetCenterY = (targetTopLeft.y + targetBottomRight.y) / 2;

        Imgproc.rectangle(img, targetTopLeft, targetBottomRight, VisionConstant.BLUE, 2);
        Imgproc.rectangle(img, new Point(targetCenterX, targetCenterY), new Point(targetCenterX, targetCenterY), VisionConstant.RED, 3);

        calculateTargetError();
        drawTargetError(img);
    }

    private void calculateTargetError() {
        xError = (VisionConstant.targetCenterXInFrame - targetCenterX) * VisionConstant.pixel2AngleX;
        yError = (VisionConstant.targetCenterYInFrame - targetCenterY) * VisionConstant.pixel2AngleY;
    }

    private void drawTargetError(Mat img) {
        double xError2Print = BigDecimal.valueOf(xError)
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();

        double yError2Print = BigDecimal.valueOf(yError)
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();

        Imgproc.putText(img, "X:" + xError2Print, new Point(targetTopLeft.x, targetTopLeft.y - 20), Core.FONT_HERSHEY_SIMPLEX, 0.5, VisionConstant.GREEN, 1);
        Imgproc.putText(img, "Y:" + yError2Print, new Point(targetTopLeft.x, targetTopLeft.y - 8), Core.FONT_HERSHEY_SIMPLEX, 0.5, VisionConstant.GREEN, 1);
    }
}

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

public class TargetInfo {
    public double targetCenterX = 0, targetCenterY = 0, xError = 0, yError = 0;
    private Point targetTopLeft = new Point(VisionConstant.MAX_FRAME_WIDTH, VisionConstant.MAX_FRAME_HEIGHT), targetBottomRight = new Point(0, 0);
    private boolean isValid = false;

    public TargetInfo(MatOfPoint... contours) {
        isValid = contours.length > 0;
        if (isValid) {
            for (int i = 1; i < contours.length; i++) {
                calculateTargetPoint(contours[i]);
            }

            targetCenterX = (targetTopLeft.x + targetBottomRight.x) / 2;
            targetCenterY = (targetTopLeft.y + targetBottomRight.y) / 2;

            xError = (VisionConstant.targetCenterXInFrame - targetCenterX) * VisionConstant.pixel2AngleX;
            yError = (VisionConstant.targetCenterYInFrame - targetCenterY) * VisionConstant.pixel2AngleY;
        }
    }

    public TargetInfo(List<MatOfPoint> contours) {
        isValid = contours.size() > 0;
        if (isValid) {
            for (int i = 0; i < contours.size(); i++) {
                calculateTargetPoint(contours.get(i));
            }

            targetCenterX = (targetTopLeft.x + targetBottomRight.x) / 2;
            targetCenterY = (targetTopLeft.y + targetBottomRight.y) / 2;

            xError = (VisionConstant.targetCenterXInFrame - targetCenterX) * VisionConstant.pixel2AngleX;
            yError = (VisionConstant.targetCenterYInFrame - targetCenterY) * VisionConstant.pixel2AngleY;
        }
    }

    private void calculateTargetPoint(MatOfPoint contour) {
        Rect r = Imgproc.boundingRect(contour);
        targetTopLeft.x = Math.min(r.tl().x, targetTopLeft.x);
        targetTopLeft.y = Math.min(r.tl().y, targetTopLeft.y);
        targetBottomRight.x = Math.max(r.br().x, targetBottomRight.x);
        targetBottomRight.y = Math.max(r.br().y, targetBottomRight.y);
    }

    public void drawTarget(Mat img) {
        Imgproc.rectangle(img, targetTopLeft, targetBottomRight, VisionConstant.BLUE);
        Imgproc.rectangle(img, new Point(targetCenterX, targetCenterY), new Point(targetCenterX, targetCenterY), VisionConstant.RED, 3);

        drawTargetError(img);
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

    public void sendTargetInfo() {
        VisionDataServer.getInstance().sendData(isValid, xError, yError);
    }
}
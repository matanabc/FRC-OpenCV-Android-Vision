package com.example.opencvvisiontest;

import android.os.AsyncTask;

import org.opencv.core.Mat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;

public class MJPEGStream extends AsyncTask<Mat, Void, Void> {
    private ServerSocket server;
    private OutputStream os;

    public MJPEGStream() throws IOException {
        server = new ServerSocket(8080);
        os = server.accept().getOutputStream();
    }

    @Override
    protected Void doInBackground(Mat... img) {
        try {
            int length = (int) (img[0].total() * img[0].elemSize());
            byte buffer[] = new byte[length];
            img[0].get(0, 0, buffer);

            os.write(("Content-type: image/jpeg\r\n" +
                    "Content-Length: " + buffer.length +
                    "\r\n\r\n").getBytes());
            os.write(buffer);
//            os.write(("\r\n--" + NanoHTTPD.MULTIPART_BOUNDARY + "\r\n").getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

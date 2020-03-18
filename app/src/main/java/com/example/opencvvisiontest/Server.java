package com.example.opencvvisiontest;

import com.example.opencvvisiontest.vision.VisionConstant;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

public class Server extends NanoHTTPD {
    public Server(int port) throws IOException {
        super(port);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            if (session.getMethod() == Method.GET && session.getParameters().size() != 0) {
                for (String key : session.getParameters().keySet()) {
                    VisionConstant.update(key, session.getParameters().get(key).get(0));
                    return newFixedLengthResponse(String.valueOf(VisionConstant.showHSV));
                }
                return newFixedLengthResponse("Update!");
            }
        } catch (Exception e) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_HTML, "Something went wrong!");
        }

        return newFixedLengthResponse(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_HTML, "Something went wrong!");
    }
}

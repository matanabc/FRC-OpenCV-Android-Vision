package com.example.opencvvisiontest.vision.server;

import com.example.opencvvisiontest.vision.VisionConstant;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

public class VisionConstantServer extends NanoHTTPD {
    public VisionConstantServer() throws IOException {
        super(5801);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Response response = newFixedLengthResponse(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_HTML, "Something went wrong!");
        try {
            if (session.getMethod() == Method.GET && session.getParameters().size() != 0) {
                for (String key : session.getParameters().keySet()) {
                    VisionConstant.update(key, session.getParameters().get(key).get(0));
                }
                response = newFixedLengthResponse("Update!");
            }
        } catch (Exception e) {
            response = newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_HTML, "Something went wrong!");
        }

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "X-Requested-With");
        response.addHeader("Access-Control-Allow-Headers", "Authorization");
        return response;
    }
}

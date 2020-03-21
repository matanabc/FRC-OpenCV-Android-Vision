package com.example.visiondroid.vision.server;

import com.example.visiondroid.vision.VisionConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import fi.iki.elonen.NanoHTTPD;

public class VisionConstantServer extends NanoHTTPD {
    private String visionConfigFile = "Failed to load vision config file";

    public VisionConstantServer(InputStream visionConfigStream) throws IOException {
        super(5801);

        try{
            StringBuilder textBuilder = new StringBuilder();
            Reader reader = new BufferedReader(new InputStreamReader(visionConfigStream, Charset.forName(StandardCharsets.UTF_8.name())));
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }

            visionConfigFile = textBuilder.toString();
        } catch (Exception e){
            e.printStackTrace();
        }

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Response response = newFixedLengthResponse(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_HTML, "Bad request!");
        try {
            if (session.getMethod() == Method.GET) {
                if (session.getParameters().size() != 0) {
                    for (String key : session.getParameters().keySet()) {
                        VisionConstant.update(key, session.getParameters().get(key).get(0));
                    }
                    response = newFixedLengthResponse("Update!");
                } else {
                    response = newFixedLengthResponse(visionConfigFile);
                }
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

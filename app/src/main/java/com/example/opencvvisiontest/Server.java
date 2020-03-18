package com.example.opencvvisiontest;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

public class Server extends NanoHTTPD {

    public Server(int port) throws IOException {
        super(port);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    @Override
    public Response serve(IHTTPSession session) {
        return newFixedLengthResponse(Response.Status.OK, NanoHTTPD.MIME_HTML, "<html><head><title>hoge</title></head><body><h1>test</h1></body></html>");
    }
}

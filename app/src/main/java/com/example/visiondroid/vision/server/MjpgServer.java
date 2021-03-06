package com.example.visiondroid.vision.server;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MjpgServer {
    public static final String K_BOUNDARY = "boundary";
    private static MjpgServer sInst = null;

    public static final String TAG = "MJPG";

    private ArrayList<Connection> mConnections = new ArrayList<>();
    private Object mLock = new Object();
    private ServerSocket mServerSocket;
    private boolean mRunning;
    private Thread mRunThread;

    private MjpgServer() {
    }

    public static MjpgServer getInstance() {
        if (sInst == null) {
            sInst = new MjpgServer();
        }
        return sInst;
    }

    public void startServer() {
        new SendStartServerTask().execute();
    }

    private boolean sendLastTime = false;
    public void sendMat(byte[] buffer) {
        if(sendLastTime){
            sendLastTime = false;
        } else {
            new SendUpdateTask().execute(buffer);
            sendLastTime = true;
        }
    }

    private class SendStartServerTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                mServerSocket = new ServerSocket(5800);
                mRunning = true;
                mRunThread = new Thread(runner);
                mRunThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class SendUpdateTask extends AsyncTask<byte[], Void, Void> {
        @Override
        protected Void doInBackground(byte[]... params) {
            update(params[0]);
            return null;
        }
    }

    private void update(byte[] bytes) {
        synchronized (mLock) {
            ArrayList<Integer> badIndices = new ArrayList<>(mConnections.size());
            for (int i = 0; i < mConnections.size(); i++) {
                Connection c = mConnections.get(i);
                if (c == null || !c.isAlive()) {
                    badIndices.add(i);
                } else {
                    c.writeImageUpdate(bytes);
                }
            }
            for (int i : badIndices) {
                mConnections.remove(i);
            }
        }
    }

    Runnable runner = new Runnable() {
        @Override
        public void run() {
            while (mRunning) {
                try {
                    Log.i(TAG, "Waiting for connections");
                    Socket s = mServerSocket.accept();
                    Log.i("MjpgServer", "Got a socket: " + s);
                    Connection c = new Connection(s);
                    synchronized (mLock) {
                        mConnections.add(c);
                    }
                    c.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private class Connection {
        private Socket mSocket;

        public Connection(Socket s) {
            mSocket = s;
        }

        public boolean isAlive() {
            return !mSocket.isClosed() && mSocket.isConnected();
        }

        public void start() {
            try {
                Log.i(TAG, "Starting a connection!");
                OutputStream stream = mSocket.getOutputStream();
                stream.write(("HTTP/1.0 200 OK\r\n" +
                        "Server: vision\r\n" +
                        "Cache-Control: no-cache\r\n" +
                        "Pragma: no-cache\r\n" +
                        "Connection: close\r\n" +
                        "Content-Type: multipart/x-mixed-replace;boundary=--" + K_BOUNDARY + "\r\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void writeImageUpdate(byte[] buffer) {
            if (!isAlive()) {
                return;
            }
            try {
                OutputStream stream = mSocket.getOutputStream();
                stream.write(("\r\n--" + K_BOUNDARY + "\r\n").getBytes());
                stream.write(("Content-type: image/jpeg\r\n" +
                        "Content-Length: " + buffer.length + "\r\n" +
                        "\r\n").getBytes());
                stream.write(buffer);
                stream.flush();
            } catch (IOException e) {
                // There is a broken pipe exception being thrown here I cannot figure out.
            }
        }
    }
}
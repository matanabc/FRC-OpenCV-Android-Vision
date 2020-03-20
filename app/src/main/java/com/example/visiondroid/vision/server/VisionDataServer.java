package com.example.visiondroid.vision.server;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class VisionDataServer {
    private static VisionDataServer sInst = null;

    public static final String TAG = "Vision Data Server";

    private ArrayList<Connection> mConnections = new ArrayList<>();
    private Object mLock = new Object();
    private ServerSocket mServerSocket;
    private boolean mRunning;
    private Thread mRunThread;

    private VisionDataServer() {
    }

    public static VisionDataServer getInstance() {
        if (sInst == null) {
            sInst = new VisionDataServer();
        }
        return sInst;
    }

    public void startServer() {
        new StartServerTask().execute();
    }

    public void sendData(boolean isTargetValid, double x, double y) {
        new SendDataTask().execute(isTargetValid + ";" + x + ";" + y);
    }


    private class StartServerTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                mServerSocket = new ServerSocket(5802);
                mRunning = true;
                mRunThread = new Thread(runner);
                mRunThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class SendDataTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            sendData(params[0]);
            return null;
        }
    }

    private void sendData(String data) {
        synchronized (mLock) {
            ArrayList<Integer> badIndices = new ArrayList<>(mConnections.size());
            for (int i = 0; i < mConnections.size(); i++) {
                Connection c = mConnections.get(i);
                if (c == null || !c.isAlive()) {
                    badIndices.add(i);
                } else {
                    c.sendData(data);
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
                    Log.i(TAG, "Got a socket: " + s);
                    Connection c = new Connection(s);
                    synchronized (mLock) {
                        mConnections.add(c);
                    }
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

        public void sendData(String data) {
            try {
                PrintWriter out = new PrintWriter(mSocket.getOutputStream(), true);
                out.println(data);
            } catch (IOException e) {
                // There is a broken pipe exception being thrown here I cannot figure out.
            }
        }
    }
}
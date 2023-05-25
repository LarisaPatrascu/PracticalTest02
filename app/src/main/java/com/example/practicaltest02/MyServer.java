package com.example.practicaltest02;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MyServer extends Thread {
    private ServerSocket serverSocket;

    private HashMap<String, String> data = new HashMap<>();

    public MyServer(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            Log.i("Constants.TAG", "[SERVER THREAD] Created object " + port);
        } catch (IOException e) {
            Log.e("Constants.TAG", "An exception has occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Log.i("Constants.TAG", "[SERVER THREAD] Waiting for a client invocation...");
                Socket socket = serverSocket.accept();
                Log.i("Constants.TAG", "[SERVER THREAD] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());

                CommunicationThread communicationThread = new CommunicationThread(socket, this);
                communicationThread.start();
            }
        } catch (IOException e) {
            Log.e("Constants.TAG", "[SERVER THREAD] An exception has occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void setData(String name, String info) {
        this.data.put(name, info);
    }

    public synchronized HashMap<String, String > getData() {
        return data;
    }

    public void stopThread() {
        interrupt();
        try {
            serverSocket.close();
        } catch (IOException e) {
            Log.e("Constants.TAG", "[SERVER THREAD] An exception has occurred: " + e.getMessage());
            e.printStackTrace();

        }
    }

}

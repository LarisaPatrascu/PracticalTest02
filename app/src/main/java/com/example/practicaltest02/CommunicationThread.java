package com.example.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class CommunicationThread extends Thread{
    private final Socket socket;

    private final MyServer serverThread;

    public CommunicationThread(Socket socket, MyServer serverThread) {
        this.socket = socket;

        this.serverThread = serverThread;
    }

    @Override
    public void run() {
        if (socket == null) {
            return;
        }

        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            Log.i("Constants.TAG", "[COMMUNICATION THREAD] Waiting for parameters from client...");
            String word = bufferedReader.readLine();
            if (word == null || word.isEmpty()) {
                Log.e("Constants.TAG", "[COMMUNICATION THREAD] Error receiving parameters from client!");
                return;
            }

            HashMap<String, String> data = MyServer.getData();
            String wordInformation;

            if (data.containsKey(word)) {
                Log.i("Constants.TAG", "[COMMUNICATION THREAD] Getting the information from the cache...");
                wordInformation = data.get(word);
            } else {
                
            }


        } catch (Exception e) {

        }
    }
}

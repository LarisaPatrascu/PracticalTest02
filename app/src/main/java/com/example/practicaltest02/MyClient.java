package com.example.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MyClient extends Thread{
    private final String word;

    private final int port;

    private final String address;

    private final TextView wordTextView;

    private Socket socket;

    public MyClient(String word, int port, String address, TextView wordTextView) {
        this.word = word;
        this.port = port;
        this.address = address;
        this.wordTextView = wordTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            Log.i("Constants.TAG", "[CLIENT THREAD] Sending the word to the server...");
            printWriter.println(word);
            printWriter.flush();
            Log.i("Constants.TAG", "[CLIENT THREAD] Waiting for the word from the server...");
            String wordInformation;

            while ((wordInformation = bufferedReader.readLine()) != null) {
                final String finalizedWordInformation = wordInformation;
                wordTextView.post(() -> wordTextView.setText(finalizedWordInformation));
            }
        } catch (Exception e) {
            Log.e("Constants.TAG", "[CLIENT THREAD] An exception has occurred: " + e.getMessage());
            e.printStackTrace();

        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                Log.e("Constants.TAG", "[CLIENT THREAD] An exception has occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }
}

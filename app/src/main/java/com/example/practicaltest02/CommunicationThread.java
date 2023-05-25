package com.example.practicaltest02;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

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

            HashMap<String, String> data = serverThread.getData();
            String wordInformation = null;

            if (data.containsKey(word)) {
                Log.i("Constants.TAG", "[COMMUNICATION THREAD] Getting the information from the cache...");
                wordInformation = data.get(word);
            } else {
                Log.i("Constants.TAG", "[COMMUNICATION THREAD] Getting the information from the webservice...");
                HttpClient httpClient = new DefaultHttpClient();
                String pageSourceCode = "";

                HttpGet httpGet = new HttpGet("https://api.dictionaryapi.dev/api/v2/entries/en/" + word);
                HttpResponse httpGetResponse = httpClient.execute(httpGet);
                HttpEntity httpGetEntity = httpGetResponse.getEntity();

                if (httpGetEntity != null) {
                    pageSourceCode = EntityUtils.toString(httpGetEntity);
                }

                if (pageSourceCode == null) {
                    Log.e("Constants.TAG", "[COMMUNICATION THREAD] Error getting the information from the webservice!");
                    return;
                } else {
                    Log.i("Constants.TAG", pageSourceCode);
                }

                JSONArray content = new JSONArray(pageSourceCode);
                JSONObject object = content.getJSONObject(0);
                JSONArray meanings = object.getJSONArray("meanings");
                JSONArray definitions = meanings.getJSONObject(0).getJSONArray("definitions");
                String definition = definitions.getJSONObject(0).getString("definition");

                wordInformation = definition;
                data.put(word, wordInformation);

                serverThread.setData(word, wordInformation);
            }

            if (wordInformation == null) {
                Log.e("Constants.TAG", "[COMMUNICATION THREAD] Weather Forecast Information is null!");
                return;
            }

            printWriter.println(wordInformation);
            printWriter.flush();

        } catch (IOException | JSONException ioException) {
            Log.e("Constants.TAG", "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();

        } finally {
            try {
                socket.close();
            } catch (IOException ioException) {
                Log.e("Constants.TAG", "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    ioException.printStackTrace();
            }
        }
    }
}

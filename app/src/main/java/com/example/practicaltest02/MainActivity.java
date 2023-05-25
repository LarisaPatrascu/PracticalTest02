package com.example.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText serverPortEditText = null;
    private MyServer serverThread = null;

    private EditText clientPortEditText = null;

    private EditText clientAddressEditText = null;

    private EditText wordEditText = null;

    private TextView wordDefinitionTextView = null;

    private final ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private final GetWordDefinitionButtonClickListener getWordDefinitionButtonClickListener = new GetWordDefinitionButtonClickListener();

    private class ConnectButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.i("Constants.TAG", "[MAIN ACTIVITY] Server port is: " + serverPort);

            serverThread = new MyServer(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e("Constants.TAG", "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }

    private class GetWordDefinitionButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            String clientPort = clientPortEditText.getText().toString();
            String clientAddress = clientAddressEditText.getText().toString();
            if (clientPort.isEmpty() || clientAddress.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.i("Constants.TAG", "[MAIN ACTIVITY] Client port is: " + clientPort);

            String word = wordEditText.getText().toString();
            if (word.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Word should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.i("Constants.TAG", "[MAIN ACTIVITY] Word is: " + word);

            MyClient client = new MyClient(word, Integer.parseInt(clientPort), clientAddress, wordDefinitionTextView);
            client.start();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        Button connectButton = (Button)findViewById(R.id.connect_button);

        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        wordEditText = (EditText)findViewById(R.id.word_edit_text);
        Button getWordDefinitionButton = (Button)findViewById(R.id.get_info_button);
        connectButton.setOnClickListener(connectButtonClickListener);
        getWordDefinitionButton.setOnClickListener(getWordDefinitionButtonClickListener);

        wordDefinitionTextView = (TextView)findViewById(R.id.word_definition_text_view);
    }

    @Override
    protected void onDestroy() {
        Log.i("Constants.TAG", "[MAIN ACTIVITY] onDestroy() callback method was invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
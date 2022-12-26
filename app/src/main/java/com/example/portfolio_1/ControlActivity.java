package com.example.portfolio_1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.clientserversocket.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ControlActivity extends AppCompatActivity {
    private static final String SERVER_ADDRESS = "Put here your IP";
    private static final int SERVER_PORT = 4100;
    private ImageButton[] buttons = new ImageButton[4];
    private Button buttonStop;
    private Button buttonDisconnect;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private TextView connectionStatus;
    private boolean stopThread = false;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        buttons[0] = findViewById(R.id.forwardBtn);
        buttons[1] = findViewById(R.id.backwardBtn);
        buttons[2] = findViewById(R.id.leftBtn);
        buttons[3] = findViewById(R.id.rightBtn);
        buttonStop = findViewById(R.id.stopBtn);
        buttonDisconnect = findViewById(R.id.stopBtn);
        connectionStatus = findViewById(R.id.connection_status);

        // Connect to the server
        new ConnectTask().execute(SERVER_ADDRESS, Integer.toString(SERVER_PORT));

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "";
                switch (view.getId()) {
                    case R.id.forwardBtn:
                        message = "e";
                        break;
                    case R.id.backwardBtn:
                        message = "s";
                        break;
                    case R.id.leftB

                    case R.id.rightBtn:
                        message = "d";
                        break;
                    case R.id.stopBtn:
                        message = "x";
                        break;
                }
                // Send a message to the server on a separate thread
                new SendMessageTask().execute(message);
            }
        };

        for (ImageButton button : buttons) {
            button.setOnClickListener(buttonClickListener);
        }

        buttonStop.setOnClickListener(buttonClickListener);

        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Disconnect from the server
                disconnectFromServer();
            }
        });

        // Set up the WebView for image streaming from an IP camera.
        setUpWebView();
    }

    private void setUpWebView() {
        webView = findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl("http://" + SERVER_ADDRESS + ":8000/stream.mjpg");
    }

    private class ConnectTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                socket = new Socket(params[0], Integer.parseInt(params[1]));
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                connectionStatus.setText("Connected to the server");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class SendMessageTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            out.println(params[0]);
            return null;
        }
    }

    private void disconnectFromServer() {
        stopThread = true;
        try {
            socket.close();
            connectionStatus.setText("Disconnected from the server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.example.portfolio_1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
    private ImageButton buttonForward;
    private ImageButton buttonBackward;
    private ImageButton buttonLeft;
    private ImageButton buttonRight;
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

        buttonForward = findViewById(R.id.forwardBtn);
        buttonBackward = findViewById(R.id.backwardBtn);
        buttonLeft = findViewById(R.id.leftBtn);
        buttonRight = findViewById(R.id.rightBtn);
        buttonStop = findViewById(R.id.stopBtn);
        buttonDisconnect = findViewById(R.id.stopBtn);
        connectionStatus = findViewById(R.id.connection_status);

        // Connect to the server
        new ConnectTask().execute(SERVER_ADDRESS, Integer.toString(SERVER_PORT));

        buttonForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send a message to the server on a separate thread
                new SendMessageTask().execute("e");

            }
        });

        buttonBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send a message to the server on a separate thread
                new SendMessageTask().execute("s");

            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send a message to the server on a separate thread
                new SendMessageTask().execute("a");

            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send a message to the server on a separate thread
                new SendMessageTask().execute("d");

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send a message to the server on a separate thread
                new SendMessageTask().execute("x");

            }
        });


        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Disconnect from the server
                disconnectFromServer();
            }
        });

        // Webview for image streaming from an IP camera.
        webView = findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl("http://" + SERVER_ADDRESS + ":8000/stream.mjpg");

    }

// Class for sending messages from a seperate thread.
    private class SendMessageTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String message = params[0];

            // Send the message to the server
            if (out != null) {
                out.println(message);
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            // Show a progress indicator or do something else before the network operation starts
        }

        @Override
        protected void onPostExecute(Void result) {
            // Hide the progress indicator or do something else after the network operation finishes
        }
    }

// Class for connecting to a server with a seperate thread.
    private class ConnectTask extends AsyncTask<String, Void, Socket> {
        @Override
        protected Socket doInBackground(String... params) {
            String serverAddress = params[0];
            int serverPort = Integer.parseInt(params[1]);

            try {
                Socket socket = new Socket(serverAddress, serverPort);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //for continuously receiving messages from server
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!stopThread) {
                            try {
                                final String message = in.readLine();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        connectionStatus.setText(message);
                                        // Get distance value from message
                                        //int distance = Integer.parseInt(message);
                                        // Update distanceTextview with distance value
                                        //distanceTextview.setText(String.valueOf(distance));
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();



                return socket;
            } catch (IOException e) {
                Log.e("SocketThread", "Error creating socket", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Socket socket) {
            if (socket == null) {
                // Connection failed
                connectionStatus.setText("Connection failed");
            } else {
                // Connection succeeded
                connectionStatus.setText("Connected to server");
            }
        }
    }

    // A function that handles disconnecting from server.
    private void disconnectFromServer() {
        try {
            // Close the socket and input/output streams
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            Log.e("disconnectFromServer", "Error while disconnecting from server", e);
        } finally {
            stopThread = true;

            // Update the UI
            connectionStatus.setText("Disconnected");
            buttonForward.setEnabled(false);
            buttonDisconnect.setEnabled(false);
        }
    }
}

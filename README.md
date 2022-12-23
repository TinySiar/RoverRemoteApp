# RoverRemoteApp

This Android app allows you to control a device over a network using a simple interface. The app connects to a server using sockets and sends commands to the server to control the device. The app also displays a live video stream from an IP camera using a WebView.

## Requirements

- Android device with Android 4.4 or higher
- Server running on a device on the same network as the Android device

## Features

- Connect to a server using sockets
- Send commands to the server to control the device
- Display a live video stream from an IP camera in a WebView
- Disconnect from the server

## Usage

1. Make sure that the Android device and the server are connected to the same network.
2. Enter the IP address of the server in the `SERVER_ADDRESS` constant in the `ControlActivity` class.
3. Enter the port number of the server in the `SERVER_PORT` constant in the `ControlActivity` class.
4. Run the app on the Android device.
5. Press the connect button to connect to the server.
6. Use the buttons to send commands to the server and control the device.
7. Press the disconnect button to disconnect from the server.

## Troubleshooting

- If the app is unable to connect to the server, check that the IP address and port number

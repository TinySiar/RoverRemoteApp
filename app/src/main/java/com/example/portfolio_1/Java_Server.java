/* Created by Siar Sedig
*
* */
package com.example.portfolio_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Java_Server {

    static Java_Server server = new Java_Server();
    public static void main(String[] args){

        try(ServerSocket serverSocket = new ServerSocket(4141)) {
            while (true) {
                System.out.println("Listening on port 4141, CRTL-C to quit ");
                Socket socket = serverSocket.accept();
                try (PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ){
                    System.out.println("Connection accepted.");
                    String in = "";
                    while ((in = input.readLine()) != null) {

                        if (in.equals("w"))
                        {   server.moveForward(output);

                        }
                        if (in.equals("s"))
                        {   server.moveBackward(output);

                        }
                        if (in.equals("d"))
                        {   server.moveRight(output);

                        }
                        if (in.equals("a"))
                        {   server.moveLeft(output);

                        }
                        if (in.equals("b")){
                            server.stopMoving(output);
                            break;
                        }

                    }
                    System.out.print("Closing socket.\n\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void write(PrintWriter output, String message) {
        System.out.println("Sending: "+message);
        output.println(message);
    }


    static void moveForward(PrintWriter output){
        server.write(output, "MOVING FORWARD"); // moving forward


    }
    static void moveBackward(PrintWriter output){
        server.write(output, "MOVING BACKWARD"); // moving backward

    }
    static void moveRight(PrintWriter output){
        server.write(output, "MOVING RIGHT"); // moving right
    }
    static void moveLeft(PrintWriter output){

        server.write(output, "MOVING LEFT"); // moving left
    }
    static void stopMoving(PrintWriter output){
        server.write(output, "STOP"); // Stopping the vehicle from moving
    }


}
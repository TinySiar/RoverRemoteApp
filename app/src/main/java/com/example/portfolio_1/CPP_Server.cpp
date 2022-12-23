//
// Created by Siar Sedig
//

#include <iostream>
#include <string>
#include <cstring>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <wiringPi.h>
#include <wiringPiI2C.h>

#include "sensors.h"
using namespace std;

const int SERVER_PORT = 4141;

void moveForward(){
int fd = wiringPiI2CSetup(0x32);
forward(fd);
}

void moveBackward(){
int fd = wiringPiI2CSetup(0x32);
backward(fd);
}

void moveLeft(){
int fd = wiringPiI2CSetup(0x32);
turnLeft(fd);

}

void moveRight(){
int fd = wiringPiI2CSetup(0x32);
turnRight(fd);
}

void stop(){
int fd = wiringPiI2CSetup(0x32);
stop(fd);
}


int main() {
// Create a socket
int sockfd = socket(AF_INET, SOCK_STREAM, 0);
if (sockfd < 0) {
cerr << "Error creating socket" << endl;
return 1;
}

// Bind the socket to a local address
sockaddr_in server_addr;
memset(&server_addr, 0, sizeof(server_addr));
server_addr.sin_family = AF_INET;
server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
server_addr.sin_port = htons(SERVER_PORT);
if (bind(sockfd, (sockaddr*) &server_addr, sizeof(server_addr)) < 0) {
    cerr << "Error binding socket" << endl;
    return 1;
}

// Listen for incoming connections
if (listen(sockfd, 1) < 0) {
    cerr << "Error listening for connections" << endl;
    return 1;
}

// Accept a connection
sockaddr_in client_addr;
socklen_t client_addr_len = sizeof(client_addr);
int client_sockfd = accept(sockfd, (sockaddr*) &client_addr, &client_addr_len);
if (client_sockfd < 0) {
cerr << "Error accepting connection" << endl;
return 1;
}

while (true) {
    // Receive a message from the client
    char buffer[256];
    int bytes_received = recv(client_sockfd, buffer, sizeof(buffer), 0);
    if (bytes_received < 0) {
        cerr << "Error receiving message from client" << endl;
        break;
else if (bytes_received == 0) {
cout << "Client disconnected" << endl;
break;
}
buffer[bytes_received] = '\0';
cout << "Received from client: " << buffer << endl;

    if (buffer[0] == 'e') {
        // Forward message received, take appropriate action
        cout << "Forward message received" << endl;
        moveForward();
    }

    if (buffer[0] == 's') {
        // Backward message received, take appropriate action
        cout << "Backward message received" << endl;
        moveBackward();
    }

    if (buffer[0] == 'a') {
        // Left message received, take appropriate action
        cout << "Left message received" << endl;
        moveLeft();
    }

    if (buffer[0] == 'd') {
        // Right message received, take appropriate action
        cout << "Right message received" << endl;
        moveRight();
    }

    if (buffer[0] == 'x') {
        // Stop message received, take appropriate action
        cout << "Stop message received" << endl;
        stop();
    }
}

close(client_sockfd);
close(sockfd);
return 0;
}

package com.example.hazrd;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocket implements Runnable {
    Socket socket;
    ObjectOutputStream objectOutputStream;

    public ClientSocket() {

    }

    @Override
    public void run() {
        try {
            socket = new Socket("192.168.0.209", 44334);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject("Test");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

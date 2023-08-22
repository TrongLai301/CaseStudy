package com.example.casestudyclient;

import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private final Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public Client(Socket socket) {
        this.socket = socket;
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, dataOutputStream, dataInputStream);
        }
    }

    public void sendMessageToServer(String messageToServer) {
        try {
            dataOutputStream.writeUTF(messageToServer);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, dataOutputStream, dataInputStream);
        }
    }

    public void receiveMessageFromServer(VBox vBox) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String messageFromClient = dataInputStream.readUTF();
                        ClientController.addLabel(messageFromClient, vBox);
                    } catch (IOException e) {
                        e.printStackTrace();
                        closeEverything(socket, dataOutputStream, dataInputStream);
                        break;
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, DataOutputStream dataOutputStream, DataInputStream dataInputStream) {

        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

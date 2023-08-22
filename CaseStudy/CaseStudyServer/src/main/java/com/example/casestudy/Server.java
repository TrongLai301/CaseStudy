package com.example.casestudy;

import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Server {
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String queryClient = "Insert into Chat (messSendFromClient) values (?)";
    private String queryServer = "Insert into Chat (messSendFromServer) values (?)";
//    ConnectToJDBC connectToJDBC;
    public void writeDataToSql(String check, String mess) {
        ConnectToJDBC connectJDBC = new ConnectToJDBC();
        Connection conn = connectJDBC.connect();
        PreparedStatement preparedStatement = null;
        try {
            if (check.equals("send")) {
                preparedStatement = conn.prepareStatement(queryServer);
                preparedStatement.setString(1, mess);
            }

            if (check.equals("receive")) {
                preparedStatement = conn.prepareStatement(queryClient);
                preparedStatement.setString(1, mess);
            }
            assert preparedStatement != null;
            int row = preparedStatement.executeUpdate();
            if (row != 0) {
                System.out.println("Complete update " + row);
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Server(ServerSocket serverSocket) {
        try {
            this.socket = serverSocket.accept();
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, dataOutputStream, dataInputStream);
        }
    }

    public void sendMessageToClient(String messageToClient) {
        try {
            String send = "send";
            writeDataToSql(send, messageToClient);
            dataOutputStream.writeUTF(messageToClient);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, dataOutputStream, dataInputStream);
        }
    }

    public void receiveMessageFromClient(VBox vBox) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String receive = "receive";
                        String messageFromClient = dataInputStream.readUTF();
                        writeDataToSql(receive,messageFromClient);
                        ServerController.addLabel(messageFromClient, vBox);
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


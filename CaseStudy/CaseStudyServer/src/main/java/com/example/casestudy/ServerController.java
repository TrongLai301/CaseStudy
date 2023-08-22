package com.example.casestudy;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    @FXML
    private Button button_send;
    @FXML
    private TextField text_message;
    @FXML
    private VBox vbox_message;
    @FXML
    private ScrollPane sp_main;

    private Server server;
    private int serverPort = 8088;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            server = new Server(new ServerSocket(serverPort));
            oldMessLabel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        vbox_message.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                sp_main.setVvalue((Double) t1);
            }
        });

        server.receiveMessageFromClient(vbox_message);
        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String messageToSend = text_message.getText();
                if (!messageToSend.isEmpty()) {
                    setHBox(messageToSend,true);
                    server.sendMessageToClient(messageToSend);
                    text_message.clear();
                }
            }
        });

    }

    public static void addLabel(String messageFromClient, VBox vbox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                "-fx-background-radius: 20px"
        );

        textFlow.setPadding(new Insets(5, 10, 5, 10));


        hBox.getChildren().add(textFlow);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hBox);
            }
        });
    }


    public void setHBox(String mess, boolean check) {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(mess);
        TextFlow textFlow = new TextFlow(text);
        if (check) {
            hBox.setAlignment(Pos.CENTER_RIGHT);
            textFlow.setStyle("-fx-color: rgb(239,242,255);" +
                    "-fx-background-color: rgb(15,125,242);" +
                    "-fx-background-radius: 20px"
            );

            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, 0.945, 0.966));
        } else {

            hBox.setAlignment(Pos.CENTER_LEFT);
            textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                    "-fx-background-radius: 20px"
            );

            textFlow.setPadding(new Insets(5, 10, 5, 10));
        }
        hBox.getChildren().add(textFlow);
        vbox_message.getChildren().add(hBox);
    }
    public void oldMessLabel() {
        String query = "SELECT * FROM Chat";
        ConnectToJDBC connectToJDBC = new ConnectToJDBC();
        Connection connection = connectToJDBC.connect();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String messSendFromClient = resultSet.getString("messSendFromClient");
                String messSendFromServer = resultSet.getString("messSendFromServer");
                if (messSendFromClient == null) {
                    setHBox(messSendFromServer, true);
                } else {
                    setHBox(messSendFromClient, false);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


/*
Y/c:
        Ứng dụng chat qua lại không dùng Thread (Client và Server chat lần lượt với nhau) (dễ) da xong
        Tự xây dựng database và các bảng để lưu lại thông tin nhận và gửi của ứng dụng (dễ) da xong
        Sử dụng JavaFX làm giao diện (trung bình) da xong
        Khi bật ứng dụng tự động hiển thị lại nội dung chat cũ (khó) ?
        Sử dụng Thread (khó) da xong
        Giao diện đẹp (khó) da xong
        */

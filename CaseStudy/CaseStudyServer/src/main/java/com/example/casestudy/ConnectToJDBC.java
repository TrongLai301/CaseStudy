package com.example.casestudy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectToJDBC {
    private String localHost = "localhost:3306";
    private String dbName = "QuanLyTinNhan";
    private String userName = "root";
    private String passWord = "password";

    private String connect = "jdbc:mysql://" +localHost+"/"+dbName;

    public ConnectToJDBC(){

    }

    public Connection connect(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connect,userName,passWord);
            System.out.println("Connect Complete to Database");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}


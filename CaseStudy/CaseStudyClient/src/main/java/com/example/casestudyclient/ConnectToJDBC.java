package com.example.casestudyclient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectToJDBC {
    private String localhost = "localhost:3306";
    private String dbName ="QuanLyTinNhan";
    private String userName ="root";
    private String password ="password";
    private String connect ="jdbc:mysql://"+localhost+"/"+dbName;

    public ConnectToJDBC(){

    }

    public Connection connect(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connect,userName,password);
            System.out.println("Connect complete to database");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}

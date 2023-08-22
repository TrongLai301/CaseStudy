module com.example.casestudyclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.casestudyclient to javafx.fxml;
    exports com.example.casestudyclient;
}
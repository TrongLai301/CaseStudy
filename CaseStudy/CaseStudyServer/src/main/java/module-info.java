module com.example.casestudy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.casestudy to javafx.fxml;
    exports com.example.casestudy;
}
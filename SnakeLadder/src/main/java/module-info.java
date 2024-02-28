module com.example.snakeladder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.snakeladder to javafx.fxml;
    exports com.example.snakeladder;
}
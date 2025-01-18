module com.example.javafxxx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.javafxxx to javafx.fxml;
    exports com.example.javafxxx;
}
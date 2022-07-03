module com.example.picturemanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.picturemanager to javafx.fxml;
    exports com.example.picturemanager;
    exports com.example.picturemanager.function;
    opens com.example.picturemanager.function to javafx.fxml;
}
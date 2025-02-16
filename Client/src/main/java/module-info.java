module ru.itis.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;
    requires ru.itis.protocol;
    requires static lombok;
    requires java.xml;

    opens ru.itis.client to javafx.fxml;
    exports ru.itis.client;
    exports ru.itis.client.controller;
    opens ru.itis.client.controller to javafx.fxml;
}
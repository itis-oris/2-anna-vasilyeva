package ru.itis.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ru.itis.client.HelloApplication;
import ru.itis.client.connectToServ.Client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class MainController {
    @FXML
    private Button startButton;

    public void initialize() {
        startButton.setOnAction(event -> {
            try {
                Client.getInstance();
                HelloApplication.changeScne("choice_room.fxml");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}

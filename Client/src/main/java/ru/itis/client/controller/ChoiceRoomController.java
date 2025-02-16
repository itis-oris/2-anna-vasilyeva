package ru.itis.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.w3c.dom.ls.LSOutput;
import ru.itis.client.HelloApplication;
import ru.itis.client.connectToServ.Client;
import ru.itis.protocol.Message;

import java.util.Arrays;

public class ChoiceRoomController {
    @FXML
    private TextField inputRoom;
    @FXML
    private Button sendRoomButton;

    public void initialize() {
        sendRoomButton.setOnAction(event -> {
            String room = inputRoom.getText();
            if(room.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.show();
            } else {
                Client client = Client.getInstance();
                client.connect();
                client.sendMessage(Message.createMessage(
                        Message.JOINTOROOM,
                        room.getBytes()
                ));
                Message message = client.getMessage();
                RoomEntryController.setRoomName(room);
                HelloApplication.changeScne("room_entry.fxml");
            }
        });
    }
}

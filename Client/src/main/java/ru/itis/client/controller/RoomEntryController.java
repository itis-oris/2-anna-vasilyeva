package ru.itis.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import lombok.Setter;
import ru.itis.client.HelloApplication;
import ru.itis.client.connectToServ.Client;
import ru.itis.protocol.Message;

public class RoomEntryController {
    @Setter
    private static String roomName;
    @FXML
    public Button exitButton;
    @FXML
    private Text roomNameText;
    @FXML
    private Button readyButton;

    public void initialize() {
        roomNameText.setText(roomName);
        Message lastMessage = Client.getInstance().getLastMessage();
        if(lastMessage.getData()[0] == 0){
            readyButton.setDisable(true);
            exitButton.setOnAction(event -> {
                HelloApplication.changeScne("choice_room.fxml");
            });
        }else{
            exitButton.setDisable(true);
            exitButton.setVisible(false);
            readyButton.setOnAction(event -> {
                Client.getInstance().sendMessage(
                        Message.createMessage(
                                Message.READYTOGAME,
                                new byte[0]
                        )
                );
                Message message = Client.getInstance().getMessage();
                if(message.getType() == Message.READYTOGAME){
                    HelloApplication.changeScne("game.fxml");
                }
            });

        }

    }
}

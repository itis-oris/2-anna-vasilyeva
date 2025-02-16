package ru.itis.client.connectToServ;

import ru.itis.protocol.Message;

public interface ClientInterface {
    void connect();
    void sendMessage(Message message);
    void disconnect();
    Message getMessage();
    Message getLastMessage();
}

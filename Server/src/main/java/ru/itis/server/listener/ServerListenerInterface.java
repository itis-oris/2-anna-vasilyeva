package ru.itis.server.listener;

import ru.itis.protocol.Message;
import ru.itis.server.ServerInterface;

import java.net.Socket;

public interface ServerListenerInterface {
    void init(ServerInterface server);

    void handleMessage(Socket socket, Message message);

    int getType();
}

package ru.itis.server;

import ru.itis.protocol.Message;
import ru.itis.server.listener.ServerListenerInterface;
import ru.itis.server.utils.Room;

import java.net.Socket;
import java.util.List;

public interface ServerInterface {
    void sendMessage(Socket socket, Message message);

    void start();

    void registerListener(ServerListenerInterface listener);

    List<Room> getRooms();
}

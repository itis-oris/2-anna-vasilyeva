package ru.itis;

import ru.itis.server.MyServer;
import ru.itis.server.ServerInterface;
import ru.itis.server.listener.JoinToRoomListener;
import ru.itis.server.listener.ReadyListener;

public class ServerMain {
    public static void main(String[] args) {
        ServerInterface server = new MyServer(50000);
        server.registerListener(new JoinToRoomListener());
        server.registerListener(new ReadyListener());
        server.start();
    }
}

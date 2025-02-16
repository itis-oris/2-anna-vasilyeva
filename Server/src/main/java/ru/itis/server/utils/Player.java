package ru.itis.server.utils;

import lombok.Data;

import java.net.Socket;


@Data
public class Player {
    private Socket socket;

    private boolean isReady = false;

    public Player(Socket socket) {
        this.socket = socket;
    }
}

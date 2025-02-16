package ru.itis.server.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Room {
    private List<Player> playerList;
    private String name;

    public Room(String name) {
        this.name = name;
        playerList = new ArrayList<>();
    }
}

package ru.itis.server.listener;

import ru.itis.protocol.Message;
import ru.itis.server.utils.Player;
import ru.itis.server.utils.Room;

import java.net.Socket;
import java.util.List;

public class JoinToRoomListener extends ServerListenerAbstract {
    @Override
    public void handleMessage(Socket socket, Message message) {
        List<Room> rooms = server.getRooms();
        String roomName = new String(message.getData());
        for (Room room : rooms) {
            if (room.getName().equals(roomName)) {
                if(room.getPlayerList().size() == 2){
                    server.sendMessage(socket, Message.createMessage(
                            Message.JOINTOROOM,
                            new byte[]{0}
                    ));
                } else {
                    room.getPlayerList().add(new Player(socket));
                    server.sendMessage(socket, Message.createMessage(
                            Message.JOINTOROOM,
                            new byte[]{1}
                    ));
                }
                return;
            }
        }
        Room room = new Room(roomName);
        rooms.add(room);
        room.getPlayerList().add(new Player(socket));
        server.sendMessage(socket, Message.createMessage(
                Message.JOINTOROOM,
                new byte[]{1}
        ));

    }

    @Override
    public int getType() {
        return Message.JOINTOROOM;
    }
}

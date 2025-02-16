package ru.itis.server.listener;

import ru.itis.protocol.Message;
import ru.itis.server.game.MyGameRunnableClass;
import ru.itis.server.utils.Player;
import ru.itis.server.utils.Room;

import java.net.Socket;
import java.util.List;

public class ReadyListener extends ServerListenerAbstract{
    @Override
    public void handleMessage(Socket socket, Message message) {
        List<Room> rooms = server.getRooms();
        for (Room room : rooms) {
            List<Player> playerList = room.getPlayerList();
            for (Player player : playerList) {
                if(player.getSocket() == socket){
                    player.setReady(true);
                    break;
                }
            }
            if(room.getPlayerList().size() == 2){
                boolean flag = true;
                for (Player player : playerList) {
                    flag = flag && player.isReady();
                }
                if(flag){
                    for (Player player : playerList) {
                        server.sendMessage(player.getSocket(), Message.createMessage(
                                Message.READYTOGAME,
                                new byte[0]
                        ));
                    }
                    Thread thread = new Thread(new MyGameRunnableClass(server, playerList.get(0).getSocket(), playerList.get(1).getSocket()));
                    thread.setDaemon(true);
                    thread.start();
                }
            }


        }
    }

    @Override
    public int getType() {
        return Message.READYTOGAME;
    }
}

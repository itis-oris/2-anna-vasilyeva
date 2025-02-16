package ru.itis.server.game.listener;

import ru.itis.protocol.Message;

import java.net.Socket;

public class ReturnImageToUserListener extends GameListener {
    @Override
    public int getType() {
        return Message.CHOICE;
    }

    @Override
    public void handle(int i, Message message) {
        int x = message.getData()[0], y = message.getData()[1];
        int[][] board = myGameRunnableClass.getBoard();
        int image = board[x][y];
        if(i == 1){
            Socket socket1 = myGameRunnableClass.getSocket1();
            myGameRunnableClass.getServer().sendMessage(socket1, Message.createMessage(
                    Message.IMAGE,
                    new byte[]{(byte) image}
            ));
            Socket socket2 = myGameRunnableClass.getSocket2();
            myGameRunnableClass.getServer().sendMessage(socket2, Message.createMessage(
                    Message.IMAGE,
                    new byte[]{(byte) image, (byte) x, (byte) y}
            ));
        }else{
            Socket socket2 = myGameRunnableClass.getSocket2();
            myGameRunnableClass.getServer().sendMessage(socket2, Message.createMessage(
                    Message.IMAGE,
                    new byte[]{(byte) image}
            ));
            Socket socket1 = myGameRunnableClass.getSocket1();
            myGameRunnableClass.getServer().sendMessage(socket1, Message.createMessage(
                    Message.IMAGE,
                    new byte[]{(byte) image, (byte) x, (byte) y}
            ));
        }
    }
}

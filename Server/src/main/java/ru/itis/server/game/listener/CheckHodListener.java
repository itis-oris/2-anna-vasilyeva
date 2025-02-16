package ru.itis.server.game.listener;

import ru.itis.protocol.Message;

public class CheckHodListener extends GameListener {

    @Override
    public int getType() {
        return Message.ACCESS;
    }

    @Override
    public void handle(int i, Message message) {
        int result = message.getData()[0];
        if(result == 0){
            System.out.println(i + " я передаю ход");
            myGameRunnableClass.setHod(i == 1 ? 2 : 1);
            myGameRunnableClass.getServer().sendMessage(
                    i == 1? myGameRunnableClass.getSocket2() : myGameRunnableClass.getSocket1(),
                    Message.createMessage(
                            Message.SENDBOARD,
                            new byte[0]
                    )
            );
        }
    }
}

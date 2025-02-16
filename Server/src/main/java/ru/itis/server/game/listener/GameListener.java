package ru.itis.server.game.listener;

import ru.itis.protocol.Message;
import ru.itis.server.ServerInterface;
import ru.itis.server.game.MyGameRunnableClass;

public abstract class GameListener {
    protected boolean init;
    protected MyGameRunnableClass myGameRunnableClass;

    public void init(MyGameRunnableClass myGameRunnableClass) {
        this.myGameRunnableClass = myGameRunnableClass;
        this.init = true;
    }

    public abstract int getType();

    public abstract void handle(int i, Message message);
}

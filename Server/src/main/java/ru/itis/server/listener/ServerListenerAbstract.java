package ru.itis.server.listener;

import ru.itis.server.ServerInterface;

public abstract class ServerListenerAbstract implements ServerListenerInterface {
    protected boolean isStarted = false;
    protected ServerInterface server;
    @Override
    public void init(ServerInterface server) {
        this.server = server;
        isStarted = true;
    }
}

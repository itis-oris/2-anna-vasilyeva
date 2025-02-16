package ru.itis.server;

import lombok.Getter;
import ru.itis.protocol.Message;
import ru.itis.server.listener.ServerListenerInterface;
import ru.itis.server.utils.Room;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer implements ServerInterface {
    private int port;
    private List<ServerListenerInterface> listeners;
    private boolean started = false;
    private ServerSocket serverSocket;
    @Getter
    private List<Room> rooms;

    public MyServer(int port) {
        this.port = port;
        this.listeners = new ArrayList<>();
    }

    @Override
    public void sendMessage(Socket socket, Message message) {
        if(!started) {
            throw new IllegalStateException("Server has not started");
        }
        try{
            socket.getOutputStream().write(Message.getBytesView(message));
            socket.getOutputStream().flush();
        }catch (Exception e){
            throw new RuntimeException("Can't send message", e);
        }
    }

    @Override
    public void start() {
        try {
            started = true;
            this.serverSocket = new ServerSocket(this.port);
            rooms = new ArrayList<>();
            while(true){
                Socket socket = this.serverSocket.accept();
                handleRoomMessage(socket);
                createNewThreadWaitingReady(socket);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void createNewThreadWaitingReady(Socket socket) {
            Thread thread = new Thread(() -> {
                try {
                    Message message = Message.readMessage(socket.getInputStream());
                    for (ServerListenerInterface listener : listeners) {
                        if(listener.getType() == message.getType()) {
                            listener.handleMessage(socket, message);
                            break;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.setDaemon(true);
            thread.start();
    }

    private void handleRoomMessage(Socket socket) {
        try {
            Message message = Message.readMessage(socket.getInputStream());
            for (ServerListenerInterface listener : listeners) {
                if(listener.getType() == message.getType()){
                    listener.handleMessage(socket, message);
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerListener(ServerListenerInterface listener) {
        if(started){
            throw new IllegalStateException("Server is already started");
        }
        listener.init(this);
        this.listeners.add(listener);
    }
}

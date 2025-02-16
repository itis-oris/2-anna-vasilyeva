package ru.itis.client.connectToServ;

import lombok.Getter;
import ru.itis.protocol.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements ClientInterface {
    private static Client instance;
    protected InetAddress address;
    protected int port;
    protected Socket socket;
    @Getter
    private Message lastMessage;


    private Client(int port, InetAddress address) {
        this.port = port;
        this.address = address;
    }

    public static Client getInstance() {
        if(instance == null) {
            try {
                instance = new Client(50000, InetAddress.getByName("127.0.0.1"));
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    @Override
    public void connect() {
        try {
            socket = new Socket(address, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessage(Message message) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            byte[] bytesView = Message.getBytesView(message);
            outputStream.write(bytesView);
            outputStream.flush();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disconnect() {
        try {
            socket.close();
            instance = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message getMessage() {
        try {
            InputStream inputStream = socket.getInputStream();
            Message message = Message.readMessage(inputStream);
            lastMessage = message;
            return message;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

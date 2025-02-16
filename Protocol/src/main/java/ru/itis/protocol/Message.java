package ru.itis.protocol;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

// FIRST BYTES(2), MESSAGE TYPE(4), DATA LENGTH(4), DATA(?)
@Getter
public class Message {
    public static final int JOINTOROOM = 1;
    public static final int READYTOGAME = 2;
    public static final int SENDBOARD = 3;
    public static final int CHOICE = 4;
    public static final int IMAGE = 5;
    public static final int ACCESS = 6;

    private static final int MAX_LENGTH = 255;

    private static final byte[] START_BYTES = {0xC, 0xD};

    protected byte[] data;
    protected int type;

    public Message(byte[] data, int type) {
        this.data = data;
        this.type = type;
    }

    public static Message createMessage(int type, byte[] data) {
        if (data.length > MAX_LENGTH) {
            throw new RuntimeException("Bigger then max length");
        }
        if(type < 1 || type > 6) {
            throw new RuntimeException("Invalid type");
        }
        return new Message(data, type);
    }

    public static byte[] getBytesView(Message message) {
        byte[] result = new byte[2 + 4 + 4 + message.data.length];
        int nowIndex = 0;
        result[0] = START_BYTES[0];
        nowIndex++;
        result[1] = START_BYTES[1];
        nowIndex++;
        byte[] type = ByteBuffer.allocate(4).putInt(message.type).array();
        for(int i = 0; i < type.length; i++) {
            result[nowIndex++] = type[i];
        }
        byte[] lengthBytes = ByteBuffer.allocate(4).putInt(message.data.length).array();
        for(int i = 0; i < lengthBytes.length; i++) {
            result[nowIndex++] = lengthBytes[i];
        }
        for(int i = 0; i < message.data.length; i++) {
            result[nowIndex++] = message.data[i];
        }
        return result;
    }

    public static Message readMessage(InputStream in) {
        try{
        byte byte0 = (byte) in.read(), byte1 = (byte) in.read();
        if(byte0 != START_BYTES[0] || byte1 != START_BYTES[1]) {
            throw new RuntimeException("Invalid message");
        }
        byte[] typeBytes = new byte[4];
        for(int i = 0; i < typeBytes.length; i++) {
            typeBytes[i] = (byte) in.read();
        }
        int messageType = ByteBuffer.wrap(typeBytes).getInt();

        byte[] lengthBytes = new byte[4];
        for(int i = 0; i < lengthBytes.length; i++) {
            lengthBytes[i] = (byte) in.read();
        }
        int dataLength = ByteBuffer.wrap(lengthBytes).getInt();

        byte[] messageData = new byte[dataLength];
        for(int i = 0; i < dataLength; i++) {
            messageData[i] = (byte) in.read();
        }
        return new Message(messageData, messageType);
    } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package ru.itis.server.game;

import lombok.Data;
import ru.itis.protocol.Message;
import ru.itis.server.ServerInterface;
import ru.itis.server.game.listener.CheckHodListener;
import ru.itis.server.game.listener.GameListener;
import ru.itis.server.game.listener.ReturnImageToUserListener;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Data
public class MyGameRunnableClass implements Runnable {
    private Socket socket1;
    private Socket socket2;
    private int hod = 1;
    private ServerInterface server;
    private List<GameListener> listenerList;
    private int[][] board;

    public MyGameRunnableClass(ServerInterface server, Socket socket1, Socket socket2) {
        this.server = server;
        this.socket1 = socket1;
        this.socket2 = socket2;
        listenerList = new ArrayList<>();
        ReturnImageToUserListener returnImageToUserListener = new ReturnImageToUserListener();
        returnImageToUserListener.init(this);
        listenerList.add(returnImageToUserListener);
        CheckHodListener checkHodListener = new CheckHodListener();
        checkHodListener.init(this);
        listenerList.add(checkHodListener);
        initBoard();
    }

    private void initBoard() {
        board = new int[10][10];
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            list.add(i);
        }
        ArrayList<Map.Entry<Integer, Integer>> list2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                list2.add(Map.entry(i, j));
            }
        }

        Random random = new Random();
        while (list.size() > 0) {
            Integer image = list.remove(random.nextInt(list.size()));
            Map.Entry<Integer, Integer> pos1 = list2.remove(random.nextInt(list2.size()));
            Map.Entry<Integer, Integer> pos2 = list2.remove(random.nextInt(list2.size()));
            board[pos1.getKey()][pos1.getValue()] = image;
            board[pos2.getKey()][pos2.getValue()] = image;
        }
    }

    @Override
    public void run() {
        server.sendMessage(socket1, Message.createMessage(
                Message.SENDBOARD,
                new byte[0]
        ));
        while (true) {
            Message message;
            if(hod == 1) {
                try {
                    message = Message.readMessage(socket1.getInputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                try {
                    message = Message.readMessage(socket2.getInputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            for(GameListener listener : listenerList) {
                if(listener.getType() == message.getType()) {
                    listener.handle(hod, message);
                }
            }
        }
    }
}

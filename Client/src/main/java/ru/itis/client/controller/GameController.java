package ru.itis.client.controller;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ru.itis.client.connectToServ.Client;
import ru.itis.client.utils.Game;
import ru.itis.protocol.Message;

import java.util.Arrays;
import java.util.Objects;



public class GameController {
    @FXML
    private GridPane gameBoardGridPane;
    @FXML
    private Text hodText;
    @FXML
    private Text youPointsText;
    @FXML
    private Text opponentPointText;
    private boolean hod;
    private WaitingMessageService service;
    private Game game;
    private MyService myService;

    // первое в add - колонка
    public void initialize() {
        game = new Game();
        myService = new MyService();
        hodText.setText("Ход соперника");
        service = new WaitingMessageService();
        youPointsText.setText("0");
        opponentPointText.setText("0");
        for(int i = 0; i < gameBoardGridPane.getColumnCount(); i++){
            for(int j = 0; j < gameBoardGridPane.getRowCount(); j++){
                Pane pane = new Pane();
                pane.setStyle("-fx-background-color: #BDE5CC; -fx-border-color: black; -fx-border-width: 2;");
                gameBoardGridPane.add(pane, i, j);
            }
        }
        service.start();
        service.setOnSucceeded(event -> {
            Message message = service.getValue();;
            if(message.getType() == Message.SENDBOARD){
                updateBoard();
                hod = true;
                hodText.setText("Твой ход");
                addMouseClicked();
            } else {
                if(message.getType() == Message.IMAGE && !hod){
                    int x = message.getData()[1], y = message.getData()[2];
                    int image = message.getData()[0];
                    Pane pane = (Pane) gameBoardGridPane.getChildren().stream().filter((a) ->
                            Objects.equals(GridPane.getColumnIndex(a), y) &&
                                    Objects.equals(GridPane.getRowIndex(a),x)).findAny().orElseThrow();
                    image(image, pane);
                    service.reset();
                    service.start();
                    return;
                }
            }
            service.reset();
        });
    }

    private void addMouseClicked() {
        for (int i = 0; i < gameBoardGridPane.getColumnCount(); i++) {
            for (int j = 0; j < gameBoardGridPane.getRowCount(); j++) {
                for (Node node : gameBoardGridPane.getChildren()) {

                    if (Objects.equals(GridPane.getColumnIndex(node), i) && Objects.equals(GridPane.getRowIndex(node), j)) {
                        // Устанавливаем обработчик нажатия на каждую ячейку
                        Pane pane = (Pane) node;
                        int finalJ = j;
                        int finalI = i;
                        if (game.isOpened(j, i)) {
                            pane.setOnMouseClicked(event -> {
                            });
                        } else {
                            pane.setOnMouseClicked(event -> {
                                if (hod) {
                                    animatePane(pane);
                                    Client.getInstance().sendMessage(Message.createMessage(
                                            Message.CHOICE,
                                            new byte[]{(byte) finalJ, (byte) finalI}
                                    ));
                                    if (!service.isRunning()) {
                                        service.start();
                                        service.setOnSucceeded((event1) -> {
                                            Message value = service.getValue();
                                            int datum = value.getData()[0];
                                            int check = game.check(datum, finalJ, finalI);
                                            if (check == 2) {
                                                youPointsText.setText(String.valueOf(Integer.valueOf(youPointsText.getText()) + 1));
                                                Client.getInstance().sendMessage(Message.createMessage(
                                                        Message.ACCESS,
                                                        new byte[]{1}
                                                ));
                                            }
                                            image(datum, pane);
                                            service.reset();
                                            if (check == 3) {
                                                System.out.println("Зашел сюда");
                                                myService.start();
                                                myService.setOnSucceeded((event2) -> {
                                                    updateBoard();
                                                    Client.getInstance().sendMessage(Message.createMessage(
                                                            Message.ACCESS,
                                                            new byte[]{0}
                                                    ));
                                                    service.start();
                                                    System.out.println("запустили сервис");
                                                    service.setOnSucceeded((event3) -> {
                                                        Message message = service.getValue();
                                                        System.out.println("жду результат");
                                                        if (message.getType() == Message.SENDBOARD) {
                                                            updateBoard();
                                                            hod = true;
                                                            hodText.setText("Твой ход");
                                                            addMouseClicked();
                                                        }else{
                                                            if(message.getType() == Message.IMAGE && !hod){
                                                                int x = message.getData()[1], y = message.getData()[2];
                                                                int image = message.getData()[0];
                                                                Pane pane2 = (Pane) gameBoardGridPane.getChildren().stream().filter((a) ->
                                                                        Objects.equals(GridPane.getColumnIndex(a), y) &&
                                                                                Objects.equals(GridPane.getRowIndex(a),x)).findAny().orElseThrow();
                                                                image(image, pane2);
                                                                service.reset();
                                                                service.start();
                                                                return;
                                                            }
                                                        }
                                                        service.reset();
                                                    });

                                                    myService.reset();
                                                });
                                                hod = false;
                                                hodText.setText("Opponent hod");
                                                setDisablePanes();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    private void updateBoard() {
        for (Node node : gameBoardGridPane.getChildren()) {
            Integer col = GridPane.getColumnIndex(node);
            Integer row = GridPane.getRowIndex(node);
            if (col != null && row != null) {
                Pane pane = (Pane) node;
                if (!game.isOpened(row, col)) {
                    pane.getChildren().clear();
                }
            }
        }
    }

    private void image(int datum, Pane pane) {
        //устанавливаем картинку
        Image image = new Image(GameController.class.getResourceAsStream("/images/" + datum + ".png")); // Укажите путь к изображению
        ImageView imageView = new ImageView(image);
        // Устанавливаем размеры изображения (по желанию)
        imageView.setFitWidth(pane.getWidth());
        imageView.setFitHeight(pane.getHeight());

        imageView.setPreserveRatio(true); // Сохраняем пропорции изображения

        // Добавляем изображение в ячейку (pane)
        pane.getChildren().clear(); // Очистить текущие элементы, если нужно
        pane.getChildren().add(imageView); // Добавить изображение
        System.out.println(datum + " добавил изображение");
        animateImageView(imageView);
    }

    private void closeImages(int row, int col) {
        gameBoardGridPane.getChildren().forEach(node -> {
            if(Objects.equals(GridPane.getColumnIndex(node), col) && Objects.equals(GridPane.getRowIndex(node), row)){
                ((Pane) node).getChildren().clear();

            }
            if(Objects.equals(GridPane.getColumnIndex(node), game.getPrevCol()) && Objects.equals(GridPane.getRowIndex(node), game.getPrevRow())){
                ((Pane) node).getChildren().clear();
            }
        });
    }

    private void setDisablePanes(){
        for(Node node : gameBoardGridPane.getChildren()){
            node.setOnMouseClicked(event -> {});
        }
    }

    private void animatePane(Pane pane) {
        // Создаем анимацию для плавного появления
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), pane);
        fadeTransition.setFromValue(0); // Начальная прозрачность (0 = полностью прозрачный)
        fadeTransition.setToValue(1);   // Конечная прозрачность (1 = полностью непрозрачный)
        fadeTransition.play(); // Запускаем анимацию
    }

    private void animateImageView(ImageView imageView) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), imageView);
        fadeTransition.setFromValue(0); // Начальная прозрачность
        fadeTransition.setToValue(1);   // Конечная прозрачность
        fadeTransition.play();
    }

    private static class WaitingMessageService extends Service<Message> {
        @Override
        public void start() {
            super.start();
            System.out.println("start");
        }

        @Override
        protected void running() {
            super.running();
            System.out.println("running");
        }

        @Override
        protected void ready() {
            super.ready();
            System.out.println("ready");
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            System.out.println("succeeded");
        }

        @Override
        protected Task<Message> createTask() {
            return new Task<>() {
                @Override
                protected Message call() throws Exception {
                    System.out.println("жду сообщение");
                    Client client = Client.getInstance();
                    Message message = client.getMessage();
                    System.out.println(Arrays.toString(Message.getBytesView(message)));
                    updateValue(message);
                    return message;
                }
            };
        }
    }


    private static class MyService extends Service<Boolean> {
        @Override
        protected Task<Boolean> createTask() {
            return new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }
            };
        }
    }
}

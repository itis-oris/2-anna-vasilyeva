package ru.itis.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage primaryStage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main_menu.fxml"));
        primaryStage = stage;
        Scene scene = new Scene(fxmlLoader.load(), 700, 700);
        scene.getStylesheets().add(HelloApplication.class.getResource("/styles/styles.css").toExternalForm());
        stage.setTitle("CardFlip");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void changeScne(String fxml){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml));
            Scene scene = new Scene(fxmlLoader.load(), 700, 700);
            scene.getStylesheets().add(HelloApplication.class.getResource("/styles/styles.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
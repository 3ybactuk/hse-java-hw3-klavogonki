package ru.hse.javaprogramming.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class KlavogonkiApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(KlavogonkiApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 400);
        stage.setTitle("Клавогонки");
        stage.setScene(scene);

        LoginController controller = fxmlLoader.getController();
        controller.setLoginStage(stage);


        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
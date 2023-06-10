package ru.hse.javaprogramming.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    public TextField nameInput;
    public TextField portInput;
    public TextField ipAddressInput;

    private Stage loginStage;

    public void setLoginStage(Stage loginStage) {
        this.loginStage = loginStage;
    }

    @FXML
    protected void playButtonClicked() {
        System.out.println("Play button clicked!");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("play-view.fxml"));

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            PlayController playController = loader.getController();
            if (!playController.setClient(ipAddressInput.getText(), Integer.parseInt(portInput.getText()), nameInput.getText(), stage)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Пользователь с таким именем уже существует", ButtonType.OK);
                alert.setHeaderText("Ошибка подключения");
                alert.showAndWait();
                return;
            }

            Runtime.getRuntime().addShutdownHook(new Thread(playController::disconnectClient));

            loginStage.close();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void aboutButtonClicked() {
        System.out.println("About button clicked!");
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Автор программы: Никита Шубин БПИ211", ButtonType.OK);
        alert.setHeaderText("");
        alert.showAndWait();
    }


//    public void setStage(Stage stage) {
//        loginStage = stage;
//    }
}
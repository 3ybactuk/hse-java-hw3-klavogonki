package ru.hse.javaprogramming.client;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class PlayController {
    @FXML
    public TextField userTextInput;

    @FXML
    public Label givenTextLabel;
    @FXML
    public Label playerStats;
    @FXML
    public Label timerLabel;

    public Client client;
    private Stage stage;

    public void initialize() {
        userTextInput.setEditable(false);
        userTextInput.setOnKeyReleased(this::handleKeyReleased);
    }

    private void handleKeyReleased(KeyEvent event) {
        if (userTextInput.isEditable()) {
            String inputText = userTextInput.getText();
            if (inputText.length() > 0 && inputText.charAt(inputText.length() - 1) == ' ') {
                userTextInput.setText("");
            }
            System.out.println(inputText);
            client.sendPlayerUpdate();
        }
    }

    public boolean setClient(String ipAddress, int port, String name, Stage stage) throws IOException {
        client = new Client(ipAddress, port, name, this);
        if (!client.connectToServer()) {
            disconnectClient();

            return false;
        }

//        setText();
        client.sendPlayerUpdate();
        startTimer();

        stage.setOnCloseRequest(event -> {
            event.consume();
            disconnectClient();
            stage.close();
        });

        return true;
    }

    public void startTimer() {
        final int[] initialSeconds = {60}; // Initial number of seconds
        final int[] remainingSeconds = {initialSeconds[0]}; // Variable to track remaining seconds

        // Create a label to display the remaining seconds
        Label secondsLabel = new Label();
        secondsLabel.setText(String.valueOf(remainingSeconds[0]));

        // Create a timeline to update the remaining seconds
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        int secondsTimer = client.getTimerSeconds();
                        setTimerLabel(secondsTimer);

                        if (client.isGameEnded) {
                            userTextInput.setEditable(false);
                        } else if (client.isGameStarted) {
                            userTextInput.setEditable(true);
                        } else if (!client.isTextHidden) {
                            setText();
                        }

                        setPlayerStats(client.getPlayerStats());
                    }
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();
    }

    public void disconnectClient() {
        if (client != null) {
            client.disconnectFromServer();
            client = null;
        }
    }

    public void setTimerLabel(int seconds) {
        if (!client.isGameStarted) {
            timerLabel.setText("До начала игры " + seconds + " секунд");
        } else {
            timerLabel.setText("До конца игры " + seconds + " секунд");
        }
    }

    public void setPlayerStats(String text) {
        playerStats.setText(text);
    }

    public void setText() {
        String text = client.getText();
        givenTextLabel.setText(text);
    }
}

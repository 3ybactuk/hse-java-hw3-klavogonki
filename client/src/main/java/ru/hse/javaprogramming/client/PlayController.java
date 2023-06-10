package ru.hse.javaprogramming.client;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class PlayController {
    @FXML
    public TextField userTextInput;

    @FXML
    public TextFlow givenTextLabel;
    private Text textToInput;
    @FXML
    public Label playerStats;
    @FXML
    public Label timerLabel;

    public Client client;
    private boolean newWord = true;
    private int wordEndPos = 0;
    private int wordStartPos = 0;

    public void initialize() {
        textToInput = new Text();
        givenTextLabel.getChildren().add(textToInput);

        userTextInput.setEditable(false);
        userTextInput.textProperty().addListener(this::handleKeyReleased);
    }

    private void handleKeyReleased(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//        System.out.println("textfield changed from " + oldValue + " to " + newValue);
        client.sendPlayerUpdate();

        if (client.charPos >= textToInput.getText().length() - 1) {
            userTextInput.setEditable(false);
            Platform.runLater(() -> {
                userTextInput.clear();
            });
            return;
        }

        if (newValue.length() > 0) {
            newWord = false;
            char curChar = newValue.charAt(newValue.length() - 1);
            char textChar = client.getText().charAt(client.charPos);
//            System.out.println(curChar + " " + textChar);


            if (curChar == ' ') {
                Platform.runLater(() -> {
                    userTextInput.clear();
                });
                client.charPos++;
                newWord = true;
                wordEndPos = client.charPos;
                while (wordEndPos < textToInput.getText().length() && (textToInput.getText().charAt(wordEndPos) != ' ')) { wordEndPos++; }
            } else if (newValue.compareTo(oldValue) < 0) {
//                System.out.println("Char deleted");
                client.charPos--;
            } else {
                if (client.charPos < wordEndPos) {
                    if (curChar != textChar) {
                        client.wrongSymbols++;
                    } else {
//                        client.totalSymbols++;
                    }
                    client.charPos++;
                }
            }

//            client.totalSymbols++;
        } else if (!newWord && oldValue.length() != 0) {
//            System.out.println("Word deleted");
            client.charPos -= oldValue.length();
        }

        selectWord(client.charPos, wordEndPos);
    }

    private void selectWord(int wordStart, int wordEnd) {
        String labelText = textToInput.getText();
        givenTextLabel.getChildren().clear(); // Clear existing text

        if (wordStart >= 0 && wordEnd <= labelText.length() && wordStart < wordEnd) {
            Text before = new Text(labelText.substring(0, wordStart));
            Text selected = new Text(labelText.substring(wordStart, wordEnd));
            Text after = new Text(labelText.substring(wordEnd));

            selected.setFill(Color.GREEN);

            givenTextLabel.getChildren().addAll(before, selected, after);
        } else {
            textToInput.setText(labelText);
            givenTextLabel.getChildren().add(textToInput);
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

                        setPlayerStats(client.getPlayerStatsString());
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

    public String getTextToInput() {
        return textToInput.getText();
    }

    public void setText() {
        String text = client.getText();
        textToInput.setText(text);
        while (textToInput.getText().charAt(wordEndPos) != ' ') { wordEndPos++; }
        selectWord(wordStartPos, wordEndPos);
    }
}

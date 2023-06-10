package ru.hse.javaprogramming.client;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class PlayControllerTest {

    private PlayController playController;

    @BeforeEach
    public void setUp() {
        playController = new PlayController();
        playController.userTextInput = new TextField();
        playController.givenTextLabel = new TextFlow();
        playController.playerStats = new Label();
        playController.timerLabel = new Label();
        playController.client = null;
    }

    @Test
    public void testInitialize() {
        Assertions.assertNotNull(playController.getTextToInput());
        Assertions.assertFalse(playController.userTextInput.isEditable());
    }

    @Test
    public void testStartTimer() {
        playController.startTimer();

        Assertions.assertEquals("До начала игры 60 секунд", playController.timerLabel.getText());
    }

    @Test
    public void testDisconnectClient() throws IOException {
        playController.client = new Client("127.0.0.1", 8080, "John", playController);

        playController.disconnectClient();

        Assertions.assertNull(playController.client);
    }

    @Test
    public void testSetTimerLabel_gameNotStarted() {
        playController.setTimerLabel(30);

        Assertions.assertEquals("До начала игры 30 секунд", playController.timerLabel.getText());
    }

    @Test
    public void testSetTimerLabel_gameStarted() {
        playController.client.isGameStarted = true;

        playController.setTimerLabel(20);

        Assertions.assertEquals("До конца игры 20 секунд", playController.timerLabel.getText());
    }

    @Test
    public void testSetPlayerStats() {
        playController.setPlayerStats("Player Stats: Score=100, Level=2");

        Assertions.assertEquals("Player Stats: Score=100, Level=2", playController.playerStats.getText());
    }

    @Test
    public void testSetText() {
        playController.client.setText("Hello world");
        playController.setText();

        Assertions.assertEquals("Hello world", playController.getTextToInput());
        Assertions.assertEquals(1, playController.givenTextLabel.getChildren().size());
        Assertions.assertEquals(playController.getTextToInput(), playController.givenTextLabel.getChildren().get(0));
    }
}
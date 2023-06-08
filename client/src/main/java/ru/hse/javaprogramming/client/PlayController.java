package ru.hse.javaprogramming.client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class PlayController {
    @FXML
    public TextField userTextInput;

    @FXML
    public Label givenTextLabel;

    private Client client;

    public void setClient(String ipAddress, int port, String name) throws IOException {
        client = new Client(ipAddress, port, name);
        client.connectToServer();
    }

    public void disconnectClient() {
        if (client != null) {
            client.disconnectFromServer();
        }
    }

    public void initialize() {
        // Read the text from the file or any other source
//        String textFromFile = client.getText();

        // Set the text to the label
//        givenTextLabel.setText(textFromFile);
    }

    public void setText() {
        String text = client.getText();
        givenTextLabel.setText(text);
    }
}

package ru.hse.javaprogramming;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private final List<Player> players = new ArrayList<>();
    private final Timer timer;
    private final String text;
    private boolean isTextHidden = true;

    public Game(String text) {
        timer = new Timer();
        this.text = text;
    }

    public void addPlayer(Player player) {
        players.add(player);
        if (players.size() == 1) {
            // Start the game if it's the first player
            startGame();
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
        if (players.isEmpty()) {
            // End the game if there are no players left
            endGame();
        }
    }

    private void startGame() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isTextHidden = false;
            }
        }, 25_000);
    }

    private void endGame() {
        timer.cancel();
    }

    private void sendTextToPlayers(String text) {
        // Send the text to all players
        for (Player player : players) {
            // Send the text to the player
        }
    }
}
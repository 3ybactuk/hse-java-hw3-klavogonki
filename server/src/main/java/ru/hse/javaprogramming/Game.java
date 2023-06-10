package ru.hse.javaprogramming;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    public final int MAX_PLAYERS = 3;
    public final int WAIT_TIME = 6;
    public final int GAME_TIME = 30;
    public final int SHOW_TEXT_TIME = 5;
    private final List<Player> players = new ArrayList<>();
    private final Timer timer;
    private final String text;
    public boolean isTextHidden = true;
    public boolean isGameStarted = false;
    public boolean isGameEnded = false;
    public int secondsRemaining;


    public Game(String text) {
        timer = new Timer();
        this.text = text;
        secondsRemaining = WAIT_TIME;
    }

    public int countPlayers() {
        return players.size();
    }

    public void addPlayer(Player player) {
        players.add(player);
        if (players.size() == 1) {
            startGame();
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
        if (players.isEmpty()) {
            endGame();
        }
    }

    private void startGame() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                secondsRemaining--;

                if (!isGameStarted) {
                    if (secondsRemaining <= 0) {
                        isGameStarted = true;
                        secondsRemaining = GAME_TIME;
                    } else if (secondsRemaining <= SHOW_TEXT_TIME) {
                        isTextHidden = false;
                    }
                } else if (secondsRemaining <= 0) {
                    isGameEnded = true;
                    endGame();
                }
            }
        }, 1_000, 1_000);


    }

    private void endGame() {
        timer.cancel();
    }

    public String getText() {
        return text;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
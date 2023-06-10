package ru.hse.javaprogramming;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The Game class represents a game instance with multiple players.
 */
public class Game {
    public final int MAX_PLAYERS = 3;
    public final int WAIT_TIME = 6;
    public final int GAME_TIME = 100;
    public final int SHOW_TEXT_TIME = 5;
    private final List<Player> players = new ArrayList<>();
    private final Timer timer;
    private final String text;
    public boolean isTextHidden = true;
    public boolean isGameStarted = false;
    public boolean isGameEnded = false;
    public int secondsRemaining;
    public int timeElapsed;

    /**
     * Creates a new instance of the Game class with the specified text.
     *
     * @param text the text to be displayed in the game
     */
    public Game(String text) {
        timer = new Timer();
        this.text = text;
        secondsRemaining = WAIT_TIME;
    }

    /**
     * Returns the number of players currently in the game.
     *
     * @return the number of players
     */
    public int countPlayers() {
        return players.size();
    }

    /**
     * Adds a player to the game.
     *
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        players.add(player);
        if (players.size() == 1) {
            startGame();
        }
    }

    /**
     * Removes a player from the game.
     *
     * @param player the player to remove
     */
    public void removePlayer(Player player) {
        players.remove(player);
        if (players.isEmpty()) {
            endGame();
        }
    }

    /**
     * Starts the game by scheduling the timer task.
     */
    private void startGame() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                secondsRemaining--;
                timeElapsed++;
                if (!isGameStarted) {
                    timeElapsed = 0;
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

    /**
     * Ends the game by canceling the timer.
     */
    public void endGame() {
        isGameEnded = true;
        timer.cancel();
    }

    /**
     * Returns the text displayed in the game.
     *
     * @return the game text
     */
    public String getText() {
        return text;
    }

    /**
     * Returns a list of players currently in the game.
     *
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return players;
    }
}
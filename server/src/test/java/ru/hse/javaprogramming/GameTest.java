package ru.hse.javaprogramming;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GameTest {
    private Game game;
    private String text;

    @BeforeEach
    public void setup() {
        text = "test text";
        game = new Game(text);
    }

    @Test
    public void testCountPlayers() {
        Assertions.assertEquals(0, game.countPlayers());

        Player player1 = new Player("Player 1", text);
        game.addPlayer(player1);
        Assertions.assertEquals(1, game.countPlayers());

        Player player2 = new Player("Player 2", text);
        game.addPlayer(player2);
        Assertions.assertEquals(2, game.countPlayers());

        game.removePlayer(player1);
        Assertions.assertEquals(1, game.countPlayers());

        game.removePlayer(player2);
        Assertions.assertEquals(0, game.countPlayers());
    }

    @Test
    public void testAddPlayer() {
        Player player1 = new Player("Player 1", text);
        game.addPlayer(player1);
        Assertions.assertTrue(game.getPlayers().contains(player1));
    }

    @Test
    public void testRemovePlayer() {
        Player player1 = new Player("Player 1", text);
        Player player2 = new Player("Player 2", text);

        game.addPlayer(player1);
        game.addPlayer(player2);

        game.removePlayer(player1);
        Assertions.assertFalse(game.getPlayers().contains(player1));
        Assertions.assertTrue(game.getPlayers().contains(player2));

        game.removePlayer(player2);
        Assertions.assertFalse(game.getPlayers().contains(player2));
    }

    @Test
    public void testEndGame() {
        Player player1 = new Player("Player 1", text);
        game.addPlayer(player1);

        game.endGame();

        Assertions.assertTrue(game.isGameEnded);
    }

    @Test
    public void testGetText() {
        String expectedText = "test text";
        String actualText = game.getText();

        Assertions.assertEquals(expectedText, actualText);
    }

    @Test
    public void testGetPlayers() {
        Player player1 = new Player("Player 1", text);
        Player player2 = new Player("Player 2", text);

        game.addPlayer(player1);
        game.addPlayer(player2);

        List<Player> players = game.getPlayers();

        Assertions.assertEquals(2, players.size());
        Assertions.assertTrue(players.contains(player1));
        Assertions.assertTrue(players.contains(player2));
    }
}

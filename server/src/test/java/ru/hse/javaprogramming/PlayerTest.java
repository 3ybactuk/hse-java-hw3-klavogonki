package ru.hse.javaprogramming;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class PlayerTest {
    private Player player;
    private Game game;
    private String text;

    @BeforeEach
    public void setup() {
        text = "test text";
        game = new Game(text);
        player = new Player("John", "127.0.0.1");
        player.setGame(game);
    }

    @Test
    public void testGetPlayerInfoMap() {
        Map<String, String> playerInfo = player.getPlayerInfoMap();

        Assertions.assertEquals("John", playerInfo.get("name"));
        Assertions.assertEquals("0", playerInfo.get("total_symbols"));
        Assertions.assertEquals("0", playerInfo.get("wrong_symbols"));
        Assertions.assertEquals("false", playerInfo.get("is_you"));
        Assertions.assertEquals("false", playerInfo.get("connection_interrupted"));
        Assertions.assertEquals(text.length(), Integer.parseInt(playerInfo.get("text_length")));
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals("John", player.getName());
    }

    @Test
    public void testGetIpAddress() {
        Assertions.assertEquals("127.0.0.1", player.getIpAddress());
    }

    @Test
    public void testGetGame() {
        Assertions.assertNotNull(player.getGame());
        Assertions.assertEquals(game, player.getGame());
    }

    @Test
    public void testEquals() {
        Player player1 = new Player("John", "127.0.0.1");
        Player player2 = new Player("John", "127.0.0.1");
        Player player3 = new Player("Alice", "127.0.0.1");
        Player player4 = new Player("John", "192.168.0.1");

        Assertions.assertEquals(player1, player2);
        Assertions.assertNotEquals(player1, player3);
        Assertions.assertNotEquals(player1, player4);
    }

    @Test
    public void testHashCode() {
        Player player1 = new Player("John", "127.0.0.1");
        Player player2 = new Player("John", "127.0.0.1");
        Player player3 = new Player("Alice", "127.0.0.1");
        Player player4 = new Player("John", "192.168.0.1");

        Assertions.assertEquals(player1.hashCode(), player2.hashCode());
        Assertions.assertNotEquals(player1.hashCode(), player3.hashCode());
        Assertions.assertNotEquals(player1.hashCode(), player4.hashCode());
    }
}

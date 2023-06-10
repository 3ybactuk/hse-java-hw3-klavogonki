package ru.hse.javaprogramming.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerStatTest {
    private PlayerStat playerStat;

    @BeforeEach
    public void setup() {
        playerStat = new PlayerStat("John", 100);
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals("John", playerStat.getName());
    }

    @Test
    public void testGetPercentage() {
        playerStat.totalSymbols = 50;

        Assertions.assertEquals("50,00%", playerStat.getPercentage());

        playerStat.totalSymbols = 0;

        Assertions.assertEquals("0,00%", playerStat.getPercentage());
    }

    @Test
    public void testGetSpeed() {
        playerStat.secondsElapsed = 30;
        playerStat.totalSymbols = 60;

        Assertions.assertEquals("120 сим/мин", playerStat.getSpeed());

        playerStat.secondsElapsed = 0;
        playerStat.totalSymbols = 0;

        Assertions.assertEquals("0 сим/мин", playerStat.getSpeed());
    }

    @Test
    public void testToString() {
        playerStat.totalSymbols = 50;
        playerStat.wrongSymbols = 2;
        playerStat.secondsElapsed = 30;

        Assertions.assertEquals("John 50,00%, 2 ошибки, 100 сим/мин", playerStat.toString());
    }

    @Test
    public void testEquals() {
        PlayerStat playerStat1 = new PlayerStat("John", 100);
        PlayerStat playerStat2 = new PlayerStat("John", 100);
        PlayerStat playerStat3 = new PlayerStat("Alice", 100);

        Assertions.assertEquals(playerStat1, playerStat2);
        Assertions.assertNotEquals(playerStat1, playerStat3);
    }

    @Test
    public void testHashCode() {
        PlayerStat playerStat1 = new PlayerStat("John", 100);
        PlayerStat playerStat2 = new PlayerStat("John", 100);
        PlayerStat playerStat3 = new PlayerStat("Alice", 100);

        Assertions.assertEquals(playerStat1.hashCode(), playerStat2.hashCode());
        Assertions.assertNotEquals(playerStat1.hashCode(), playerStat3.hashCode());
    }

    @Test
    public void testCompareTo() {
        PlayerStat playerStat1 = new PlayerStat("John", 100);
        PlayerStat playerStat2 = new PlayerStat("Alice", 100);
        PlayerStat playerStat3 = new PlayerStat("Bob", 100);
        playerStat1.totalSymbols = 100;
        playerStat2.totalSymbols = 60;
        playerStat3.totalSymbols = 100;

        Assertions.assertTrue(playerStat1.compareTo(playerStat2) > 0);
        Assertions.assertTrue(playerStat2.compareTo(playerStat3) < 0);
        Assertions.assertEquals(0, playerStat1.compareTo(playerStat1));
    }
}
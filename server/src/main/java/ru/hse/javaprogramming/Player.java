package ru.hse.javaprogramming;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Player {
    private final String name;
    private final String ipAddress;
    private Game game;
    public int totalSymbols = 0;
    public int wrongSymbols = 0;
    public boolean isYou = false;
    public boolean isConnectionInterrupted = false;

    public Player(String name, String ipAddress) {
        this.name = name;
        this.ipAddress = ipAddress;
    }

    public Map<String, String> getPlayerInfoMap() {
        Map<String, String> playerInfo = new HashMap<>();

        playerInfo.put("name", name);
        playerInfo.put("total_symbols", Integer.toString(totalSymbols));
        playerInfo.put("wrong_symbols", Integer.toString(wrongSymbols));
        playerInfo.put("is_you", Boolean.toString(isYou));
        playerInfo.put("connection_interrupted", Boolean.toString(isConnectionInterrupted));
        playerInfo.put("text_length", Integer.toString(game.getText().length()));

        return playerInfo;
    }

    public String getName() {
        return name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name) && ipAddress.equals(player.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ipAddress);
    }
}
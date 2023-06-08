package ru.hse.javaprogramming;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Player {
    private final String name;
    private final String ipAddress;
    public int symbolsTyped;
    public int mistakesMade;
    public boolean isYou;
    public boolean isConnectionInterrupted;

    public Player(String name, String ipAddress) {
        this.name = name;
        this.ipAddress = ipAddress;
    }

    public Map<String, String> getPlayerInfo() {
        Map<String, String> playerInfo = new HashMap<>();

        playerInfo.put("name", name);
        playerInfo.put("symbols_typed", Integer.toString(symbolsTyped));
        playerInfo.put("mistakes_made", Integer.toString(mistakesMade));
        playerInfo.put("is_you", Boolean.toString(isYou));
        playerInfo.put("connection_interrupted", Boolean.toString(isConnectionInterrupted));

        return playerInfo;
    }

    public String getName() {
        return name;
    }

    public String getIpAddress() {
        return ipAddress;
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
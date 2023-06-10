package ru.hse.javaprogramming.client;

import java.text.DecimalFormat;
import java.util.Objects;

public class PlayerStat implements Comparable<PlayerStat> {
    private final String name;
    private final int textLength;
    public int totalSymbols = 0;
    public int wrongSymbols = 0;
    public int secondsElapsed;
    public boolean isYou = false;
    public boolean isConnectionInterrupted = false;

    public PlayerStat(String name, int textLength) {
        this.name = name;
        this.textLength = textLength;
    }

    public String getName() {
        return name;
    }

    public String getPercentage() {
//        System.out.println(((double) totalSymbols / textLength) * 100 + "%");
        if (textLength == 0) {
            return "0%";
        }

        DecimalFormat df = new DecimalFormat("####0.00");

        return df.format(((double) totalSymbols / textLength) * 100) + "%";
    }

    public String getSpeed() {
        if (secondsElapsed == 0) {
            return "0 сим/мин";
        }

        return (int) (((double) totalSymbols / secondsElapsed) * 60) + " сим/мин";
    }

    @Override
    public String toString() {
        return name + " " + getPercentage() + ", " + wrongSymbols + " ошибки, " + getSpeed();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerStat that = (PlayerStat) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(PlayerStat ps) {
        return totalSymbols - ps.totalSymbols;
    }
}

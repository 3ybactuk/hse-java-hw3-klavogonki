package ru.hse.javaprogramming;

import java.time.LocalTime;

public class Logging {
    public static String getTimestamp() {
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        int second = currentTime.getSecond();
        String strHour = hour < 10 ? "0" + hour : String.valueOf(hour);
        String strMinute = minute < 10 ? "0" + minute : String.valueOf(minute);
        String strSecond = second < 10 ? "0" + second : String.valueOf(second);

        return "[" + strHour + ":" + strMinute + ":" + strSecond + "]";
    }

    public static void printTimestampMessage(String message) {
        System.out.println(getTimestamp() + " " + message);
    }
}

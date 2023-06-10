package ru.hse.javaprogramming;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalTime;

public class LoggingTest {

    @Test
    public void testGetTimestamp() {
        LocalTime currentTime = LocalTime.now();

        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        int second = currentTime.getSecond();
        String strHour = hour < 10 ? "0" + hour : String.valueOf(hour);
        String strMinute = minute < 10 ? "0" + minute : String.valueOf(minute);
        String strSecond = second < 10 ? "0" + second : String.valueOf(second);
        String expectedTimestamp = "[" + strHour + ":" + strMinute + ":" + strSecond + "]";

        String actualTimestamp = Logging.getTimestamp();

        Assertions.assertEquals(expectedTimestamp, actualTimestamp);
    }

    @Test
    public void testPrintTimestampMessage() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        PrintStream originalOut = System.out;
        System.setOut(printStream);

        try {
            String testMessage = "Test message";
            Logging.printTimestampMessage(testMessage);
            printStream.flush();

            String capturedOutput = outputStream.toString();
            String expectedOutput = Logging.getTimestamp() + " " + testMessage + System.lineSeparator();

            capturedOutput = normalizeLineSeparators(capturedOutput);
            expectedOutput = normalizeLineSeparators(expectedOutput);

            Assertions.assertEquals(expectedOutput, capturedOutput);
        } finally {
            System.setOut(originalOut);
        }
    }

    private String normalizeLineSeparators(String text) {
        return text.replace("\r\n", "\n").replace("\r", "\n");
    }
}
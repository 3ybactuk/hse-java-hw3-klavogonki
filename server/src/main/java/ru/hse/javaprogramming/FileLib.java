package ru.hse.javaprogramming;

import java.io.*;

public class FileLib {
    public static String getTextFromFile(String filename) {
        StringBuilder contentBuilder = new StringBuilder();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("texts/" + filename);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(contentBuilder.toString());
        return contentBuilder.toString();
    }
}

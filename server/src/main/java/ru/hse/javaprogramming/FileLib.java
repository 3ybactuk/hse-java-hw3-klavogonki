package ru.hse.javaprogramming;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

        return contentBuilder.toString();
    }

    public static List<String> getTextNames() {
        List<String> filenames = new ArrayList<>();
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        try (InputStream inputStream = classLoader.getResourceAsStream("text_list.json")) {
//            JSONTokener tokener = new JSONTokener(inputStream);
//
//            JSONObject jsonObject = new JSONObject(tokener);
//
//            JSONArray filenamesArray = jsonObject.getJSONArray("text_filenames");
//
//            for (int i = 0; i < filenamesArray.length(); i++) {
//                String filename = filenamesArray.getString(i);
//                filenames.add(filename);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        filenames.add("amogus.txt");
        filenames.add("hard_text.txt");
        filenames.add("hard_text2.txt");
        filenames.add("hse_msu.txt");
        filenames.add("java_and_c#.txt");
        filenames.add("nuance.txt");

        return filenames;
    }

    public static String getRandomTextName() {
        List<String> list = getTextNames();
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be empty");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }

    public static String getRandomText() {
        return getTextFromFile(getRandomTextName());
    }
}

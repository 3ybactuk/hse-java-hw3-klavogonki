package ru.hse.javaprogramming;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class FileLibTest {
    @Test
    void getTextNamesTest() {
        List<String> textNames = FileLib.getTextNames();

        assertEquals(6, textNames.size());
        assertTrue(textNames.contains("amogus.txt"));
        assertTrue(textNames.contains("hard_text.txt"));
    }

    @Test
    void getRandomTextNameTest() {
        String textName = FileLib.getRandomTextName();

        assertTrue(FileLib.getTextNames().contains(textName));
    }

    @Test
    void getRandomTextTest() {
        String text = FileLib.getRandomText();

        assertNotNull(text);
        assertTrue(text.length() > 200);
    }
}

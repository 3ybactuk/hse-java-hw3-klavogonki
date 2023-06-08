package ru.hse.javaprogramming;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        try (DefaultServer server = new DefaultServer(5619)) {
            server.start();

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext() && !"exit".equals(scanner.nextLine())) {
            }
        }
    }
}

package ru.hse.javaprogramming;

import java.io.Closeable;

public interface Server extends Closeable {
    void start();
}

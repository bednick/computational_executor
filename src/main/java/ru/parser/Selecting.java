package ru.parser;

import java.io.InputStream;

/**
 * Производит выбор среди версий Parser'ов,
 * на основе входного потока
 */
public class Selecting {

    public IParser select(InputStream stream) {
        // TODO Выбор делается по строке #vervion
        return new Parser_v1();
    }

}

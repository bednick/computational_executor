package ru.nikkollaii.parser;

import java.io.IOException;
import java.io.InputStream;

/**
 * Производит выбор среди версий Parser'ов,
 * на основе входного потока
 */
public class Selecting {

    public IParser select(InputStream stream) {
        // TODO Выбор делается по строке #vervion
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Parser_v1();
    }

}

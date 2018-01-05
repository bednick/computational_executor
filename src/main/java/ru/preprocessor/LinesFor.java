package ru.preprocessor;

import java.io.BufferedReader;
import java.util.List;

/**
 *
 */
class LinesFor extends ILinesReader {
    @Override
    public boolean isRead(String line) {
        return line.startsWith("#for");
    }

    @Override
    public List<String> read(BufferedReader reader, String thisLine) {
        return null;
    }

    @Override
    public boolean isReplace(String line) {
        return true;
    }

    @Override
    public String replace(String line) {
        return line;
    }
}

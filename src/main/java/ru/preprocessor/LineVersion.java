package ru.preprocessor;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class LineVersion extends ILinesReader {
    @Override
    public boolean isRead(String line) {
        return line.startsWith("#version");
    }

    @Override
    public List<String> read(BufferedReader reader, String thisLine) {
        return new ArrayList<>(0);
    }

    @Override
    List<String> modify(String line) {
        return Collections.singletonList(line);
    }

    @Override
    protected String replace(List<String> args) {
        return null;
    }

}

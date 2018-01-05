package ru.preprocessor;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
class LinesInclude extends ILinesReader {
    @Override
    public boolean isRead(String line) {
        return line.startsWith("#include ");
    }

    @Override
    public List<String> read(BufferedReader reader, String thisLine) {
        return Collections.singletonList(thisLine);
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

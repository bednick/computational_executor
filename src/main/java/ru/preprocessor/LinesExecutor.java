package ru.preprocessor;

import ru.bricks.Pair;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
class LinesExecutor extends ILinesReader {
    private Pair<String, String> lastExecutor;

    public LinesExecutor() {
        lastExecutor = new Pair<>("Local", "");
    }

    @Override
    public boolean isRead(String line) {
        return line.startsWith("@");
    }

    @Override
    public List<String> read(BufferedReader reader, String thisLine) {

        return new ArrayList<>(0);
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

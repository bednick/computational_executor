package ru.preprocessor;

import ru.bricks.Pair;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
class LinesExecutor extends ILinesReader {
    private Pair<String, String> lastExecutor;

    public LinesExecutor() {
        lastExecutor = new Pair<>("Local", "executor");
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
    List<String> modify(String line) {
        String res;
        if(line.contains("#")) {
            res =  String.format("%s exec=%s,%s", line, lastExecutor.getKey(), lastExecutor.getValue());
        } else {
            res = String.format("%s # exec=%s,%s", line, lastExecutor.getKey(), lastExecutor.getValue());
        }
        return Collections.singletonList(res);
    }

}

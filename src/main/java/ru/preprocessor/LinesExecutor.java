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
    private boolean def;

    public LinesExecutor() {
        lastExecutor = new Pair<>("Local", "localhost");
        def = true;
    }

    @Override
    public boolean isRead(String line) {
        return line.startsWith("#exec ");
    }

    @Override
    public List<String> read(BufferedReader reader, String thisLine) {
        return new ArrayList<>(0);
    }

    @Override
    List<String> modify(String line) {
        if (def) {
            return Collections.singletonList(line);
        }
        if(line.contains("#")) {
            line =  String.format("%s exec=%s,%s", line, lastExecutor.getKey(), lastExecutor.getValue());
        } else {
            line = String.format("%s # exec=%s,%s", line, lastExecutor.getKey(), lastExecutor.getValue());
        }
        return Collections.singletonList(line);
    }

}

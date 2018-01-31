package ru.preprocessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 *
 */
class LinesData extends ILinesReader {
    @Override
    public boolean isRead(String line) {
        return true;
    }

    @Override
    public List<String> read(BufferedReader reader, String thisLine) {
        return Collections.singletonList(thisLine);
    }

    @Override
    List<String> modify(String line) {
        if (!line.startsWith("#") && !line.contains("#")) {
            line = String.format("%s #", line);
        }
        return Collections.singletonList(line);
    }
}

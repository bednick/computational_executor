package ru.preprocessor;

import ru.bricks.Pair;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
class LinesDefine extends ILinesReader {
    private final String DEFINE = "#define ";
    private List<Pair<String, String>> defines;

    public LinesDefine() {
        defines = new ArrayList<>();
    }

    @Override
    public boolean isRead(String line) {
        return line.startsWith(DEFINE);
    }

    @Override
    public List<String> read(BufferedReader reader, String thisLine) {
        String[] def = thisLine.split(" ", 3);
        defines.add(new Pair<>(def[1], def[2]));
        return new ArrayList<>(0);
    }

    @Override
    public boolean isReplace(String line) {
        return line.contains("%") && line.contains("define");
    }

    @Override
    public String replace(String line) {
        return line;
    }
}

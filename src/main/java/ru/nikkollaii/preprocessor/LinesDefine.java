package ru.nikkollaii.preprocessor;

import java.io.BufferedReader;
import java.util.*;

/**
 *
 */
class LinesDefine extends ILinesReader {
    private Map<String, String> defines;

    public LinesDefine() {
        defines = new HashMap<>();
    }

    @Override
    public boolean isRead(String line) {
        return line.startsWith("#define ");
    }

    @Override
    public List<String> read(BufferedReader reader, String thisLine) {
        String[] def = thisLine.split(" ", 3);
        defines.put(def[1], def[2]);
        return new ArrayList<>(0);
    }

    @Override
    List<String> modify(String line) {
        return Collections.singletonList(patternReplace(line, "define", 0));
    }

    @Override
    protected String replace(List<String> args) {
        return defines.get(args.get(1));
    }
}

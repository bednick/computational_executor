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
        // TODO Включть файл ? (Проверить что он есть)
        return Collections.singletonList(thisLine);
    }

    @Override
    List<String> modify(String line) {
        return Collections.singletonList(patternReplace(line, "include"));
    }

    @Override
    protected String replace(String[] args) {
        // TODO Запомнить, какой файл включаем ()
        return String.format("%s::%s", args[1], args[2]);
    }
}

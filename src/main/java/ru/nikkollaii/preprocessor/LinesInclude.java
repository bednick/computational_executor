package ru.nikkollaii.preprocessor;

import java.io.BufferedReader;
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
        // TODO Включть файл ? (Проверить что он есть (скорее нет))
        return Collections.singletonList(thisLine);
    }

    @Override
    List<String> modify(String line) {
        return Collections.singletonList(patternReplace(line, "include", 0));
    }

    @Override
    protected String replace(List<String> args) {
        // Нормализовать синтаксис, сделать проверки? (нет, созможно файл подстановки будет изменён позже и тп)
        //
        //System.out.println("    BUILD "+ Arrays.toString(args.toArray()));
        StringBuilder builder = new StringBuilder();
        builder.append("%{");
        for (String str: args) {
            builder.append('[').append(str).append(']');
        }
        builder.append('}');
        return builder.toString();
    }
}

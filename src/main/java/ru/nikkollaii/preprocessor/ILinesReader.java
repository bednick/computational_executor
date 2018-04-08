package ru.nikkollaii.preprocessor;

import ru.nikkollaii.bricks.Replace;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 *
 */
abstract class ILinesReader extends Replace {

    ILinesReader() {}

    public abstract boolean isRead(String line);

    public abstract List<String> read(BufferedReader reader, String thisLine) throws IOException;

    String readLine(BufferedReader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.endsWith("\\")) {
                builder.append(line);
                builder.deleteCharAt(builder.length()-1);
                builder.append(' ');
            } else {
                break;
            }
        }
        if (builder.length() > 0) {
            line = builder.toString();
        }
        if (line != null){
            line = line.trim();
        }
        return line;
    }

    abstract List<String> modify(String line);

    protected String replace(List<String> args) {
        throw new RuntimeException("not implements replace");
    }
}

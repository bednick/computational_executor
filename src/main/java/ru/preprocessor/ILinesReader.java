package ru.preprocessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 *
 */
abstract class ILinesReader {
    public abstract boolean isRead(String line);

    public abstract List<String> read(BufferedReader reader, String thisLine);

    public abstract boolean isReplace(String line);

    public abstract String replace(String line);

    public String readLine(BufferedReader reader) throws IOException {
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
}

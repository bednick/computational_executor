package java.ru.preprocessor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Preprocessor implements IPreprocessor {
    private final String START_COMMENT = "##";
    private final String EXTEND_LINE = "\\";
    private final String EXTENSION_IN = ".cm";
    private final String EXTENSION_OUT = ".cmc";

    public Preprocessor() {}

    private String getNewName(String oldName) {
        String name = oldName;
        if(name.endsWith(EXTENSION_IN)) {
            name = name.substring(0, name.lastIndexOf(EXTENSION_IN));
        }
        return name + EXTENSION_OUT;
    }

    @Override
    public File process(File file) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        String line;
        List<String> lines = new ArrayList<String>();
        while ((line = readLine(reader)) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith(START_COMMENT)) {
                continue;
            }
            lines.add(line);
        }
        if (!lines.isEmpty()) {
            Files.write(Paths.get(getNewName(file.getName())), lines, StandardOpenOption.CREATE);
            return new File(getNewName(file.getName()));
        }
        // throw file is empty
        return null;
    }
    private StringBuilder builder = new StringBuilder();

    private String readLine(BufferedReader reader) throws IOException {
        String line;
        while ((line = readLine(reader)) != null) {
            if (line.endsWith(EXTEND_LINE)) {
                builder.append(line);
                builder.deleteCharAt(builder.length()-1);
            } else {
                if (builder.length() > 0) {
                    line = builder.toString();
                    builder.deleteCharAt(builder.length()-1);
                }
                return line;
            }
        }
        if (builder.length() > 0) {
            line = builder.toString();
            builder.deleteCharAt(builder.length()-1);
        }
        return line;
    }

}

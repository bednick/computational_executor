package java.ru.preprocessor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Preprocessor implements IPreprocessor {
    private final String EXTENSION_IN = ".cm";
    private final String EXTENSION_OUT = ".cmc";

    private StringBuilder builder = new StringBuilder();
    private String nextLine;

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
        List<String> lines = new ArrayList<String>();
        readLine(reader);
        while (nextLine != null) {
            if (nextLine.isEmpty() || nextLine.startsWith("##")) {
                continue;
            }
            if (nextLine.startsWith("#")) {
                if (nextLine.startsWith("#define")) {
                    lines.addAll(readFor(reader));
                }
                if (nextLine.startsWith("#for")) {
                    lines.addAll(readFor(reader));
                }
            } else {
                lines.add(getCorrectLine(nextLine));
            }
        }
        if (!lines.isEmpty()) {
            Files.write(Paths.get(getNewName(file.getName())), lines, StandardOpenOption.CREATE);
            return new File(getNewName(file.getName()));
        }
        // throw file is empty
        return null;
    }

    private void readLine(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.endsWith("\\")) {
                builder.append(line);
                builder.deleteCharAt(builder.length()-1);
                builder.append(' ');
            } else {
                if (builder.length() > 0) {
                    builder.append(line);
                    line = builder.toString();
                    builder.delete(0, builder.length());
                }
                nextLine = line.trim();
                return;
            }
        }
        if (builder.length() > 0) {
            line = builder.toString();
            builder.deleteCharAt(builder.length()-1);
        }
        if (line != null){
            line = line.trim();
        }
        nextLine = line;
    }

    private List<String> readFor(BufferedReader reader) throws IOException {
        ArrayList<String> lines = new ArrayList<String>();
        while (!nextLine.startsWith("#endfor")) {
            readLine(reader);
            lines.add(getCorrectLine(nextLine));
        }
        readLine(reader);
        return lines;
    }

    private String getCorrectLine(String line) throws IOException {
        // TODO parsing for ... in
        String in = String.join(" ", Arrays.stream(line.split(";")[0].split(" "))
                .filter(l->!l.isEmpty()).collect(Collectors.toList()));
        String out = String.join(" ",Arrays.stream(line.split(";")[1].split(" "))
                .filter(l->!l.isEmpty()).collect(Collectors.toList()));

        List<String> com_and_labels = Arrays.asList(line.split(";")[2].split("#"));
        String com = com_and_labels.get(0).trim();
        String label = "#";
        if (com_and_labels.size() > 1) {
            label = String.join(" ", Arrays.asList(com_and_labels.get(1).split(" ")));
        }
        String rez = builder.append(in).append(';').append(out).append(';').append(com).append(label).toString();
        builder.delete(0, builder.length());
        return rez;
    }

}

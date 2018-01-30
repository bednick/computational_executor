package ru.preprocessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
abstract class ILinesReader {

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

    protected String patternReplace(String line, String name, int from) {
        // todo вынести в init

        String pattern = "%[\\s]*?\\{[\\s]*?\\[[\\s]*?"+name+"[\\s]*?\\][\\s\\S]*?\\}";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(line);

        do {
            if (!m.find()) {
                return line;
            }
        } while (from > m.start());

        String substring = line.substring(m.start(), m.end());
        return patternReplace(
                line.replace(substring, replace(parsing(substring))),
                name,
                m.start()+1
        );
    }

    protected String replace(List<String> args) {
        throw new RuntimeException("not implements replace");
    }

    protected List<String> parsing(String substring) {
        List<String> args = new ArrayList<>();
        int from = 0;
        while (substring.indexOf(']', from) != -1) {
            String str = substring.substring(
                    substring.indexOf('[', from)+1,
                    substring.indexOf(']', from)
            );
            args.add(str.trim());
            from = substring.indexOf(']', from)+1;
        }
        return args;
    }

//    public static void main(String[] args) {
//        String test = "{[test1][11][1e1]}";
//        for (String str: parsing(test)) {
//            System.out.println(str);
//        }
//    }
}

package ru.nikkollaii.bricks;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */

public abstract class Replace {

    protected String patternReplace(String line, String name, int from) {
        // todo вынести в init? ???
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

    protected abstract String replace(List<String> args);

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

}

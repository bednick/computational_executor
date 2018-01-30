package ru.preprocessor;

import ru.bricks.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 *
 */
class LinesFor extends ILinesReader {
    private List<Pair<String, List<String>>> queuesFor;
    private boolean is_exec = false;
    private Pair<String, String> replace;

    LinesFor() {
        queuesFor = new LinkedList<>();
    }

    @Override
    public boolean isRead(String line) {
        return line.startsWith("#for") || line.startsWith("#endfor");
    }

    @Override
    public List<String> read(BufferedReader reader, String thisLine) throws IOException {
        if (thisLine.startsWith("#for")) {
            String all = thisLine.substring(4, thisLine.lastIndexOf(':'));
            String[] spl = all.split("#in", 2);
            queuesFor.add(0, new Pair<>(spl[0].trim(), new LinkedList<>(Arrays.asList(spl[1].split(",")))));
        } else {
            queuesFor.remove(0);
        }
        return new ArrayList<>(0);
    }

    @Override
    List<String> modify(String line) {
        List<String> lines = new ArrayList<>();
        for (Pair<String, List<String>> pair: queuesFor) {
            for (String str: pair.getValue()) {
                replace = new Pair<>(pair.getKey(), str);
                String res = patternReplace(line, "for");
                if (is_exec) {
                    lines.add(res);
                }
                is_exec = false;
            }
        }
        if (lines.isEmpty()) {
            lines.add(line);
        }
        return lines;
    }

    @Override
    protected String replace(List<String> args) {
        if (replace.getKey().equals(args.get(1))) {
            is_exec = true;
            return replace.getValue();
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("%{");
            for (String arg: args) {
                builder.append(String.format("[%s]", arg));
            }
            builder.append("}");
            return builder.toString();
        }
    }
}

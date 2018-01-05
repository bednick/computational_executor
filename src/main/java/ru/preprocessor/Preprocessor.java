package ru.preprocessor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class Preprocessor implements IPreprocessor {
    private final String EXTENSION_IN = ".cm";
    private final String EXTENSION_OUT = ".cmc";

    private List<ILinesReader> modules;

    public Preprocessor() {
        modules = Arrays.asList(
                new LinesDefine(),
                new LinesFor(),
                new LinesInclude(),
                new LinesExecutor(),
                new LinesData()
        );
    }

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
        // todo check, подумать над считыванием
        String thisLine;
        while ((thisLine = modules.get(0).readLine(reader)) != null) {
            for (String line: getLinesReader(thisLine).read(reader, thisLine)) {
                lines.add(preparation(line));
            }
        }

        if (!lines.isEmpty()) {
            Files.write(Paths.get(getNewName(file.getName())), lines, StandardOpenOption.CREATE);
            return new File(getNewName(file.getName()));
        }
        // todo throw file is empty?
        return null;
    }

    private ILinesReader getLinesReader(String line) {
        for (ILinesReader module: modules) {
            if (module.isRead(line)) {
                return module;
            }
        }
        // todo exception Error in file
        throw new RuntimeException("Incorrect lines readers, or incorrect file");
    }

    private String preparation(String line) {
        for (ILinesReader module: modules) {
            if (module.isReplace(line)) {
                line = module.replace(line);
            }
        }
        return line;
    }
}

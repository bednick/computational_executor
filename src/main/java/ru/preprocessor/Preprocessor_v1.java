package ru.preprocessor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Preprocessor_v1 implements IPreprocessor {
    private final String EXTENSION_IN = ".cm";
    private final String EXTENSION_OUT = ".cmc";

    private List<ILinesReader> modules;

    public Preprocessor_v1() {
        modules = Arrays.asList(
                new LineVersion(),
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
        BufferedReader resource = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        ILinesReader reader = modules.get(0);

        List<String> lines = new ArrayList<String>();
        // todo check, подумать над считыванием
        String thisLine;

        while ((thisLine = reader.readLine(resource)) != null) {

            for (String line: getLinesReader(thisLine).read(resource, thisLine)) {
                lines.addAll(preparation(line));
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

    private List<String> preparation(String line) {
        List<String> all = new ArrayList<>();
        all.add(line);
        for (ILinesReader module: modules) {
            List<String> res = new ArrayList<>();
            for (String str: all) {
                res.addAll(module.modify(str));
            }
            all.addAll(res);
        }
        return all;
    }
}

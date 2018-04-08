package ru.nikkollaii.preprocessor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Preprocessor_v1 implements IPreprocessor {

    private List<ILinesReader> modules;

    public Preprocessor_v1() {
        modules = Arrays.asList(
                new LineVersion(),
                new LinesDefine(),
                new LinesInclude(),
                new LinesFor(),
                new LinesExecutor(),
                new LinesData()
        );
    }

    private String getNewName(String oldName) {
        String name = oldName;
        if(name.endsWith(".cm")) {
            name = name.substring(0, name.lastIndexOf(".cm"));
        }
        return String.format("%s%s", name, ".cmc");
    }

    @Override
    public File process(File file) throws IOException {
        BufferedReader resource = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        ILinesReader reader = modules.get(0);

        String version = "#version 1.0";
        LinkedList<String> includes = new LinkedList<>();
        LinkedList<String> lines = new LinkedList<>();
        // todo check, подумать над считыванием
        String thisLine;
        boolean comments = false;
        while ((thisLine = reader.readLine(resource)) != null) {
            if (thisLine.startsWith("###")) {
                comments = !comments;
            }
            if (comments || thisLine.startsWith("##") || thisLine.isEmpty()) {
                continue;
            }
            for (String line: getLinesReader(thisLine).read(resource, thisLine)) {
                Collection<String> res = preparation(line);
                for (String str: res) {
                    if (str.startsWith("#version")) {
                        version = str;
                        continue;
                    }
                    if (str.startsWith("#inc")) {
                        System.out.println("new include:" + str);
                        includes.add(str);
                        continue;
                    }
                    lines.add(str);
                }
                //lines.addAll(res);
            }
        }

        if (!lines.isEmpty()) {
            List<String> res = new LinkedList<>();
            res.add(version);
            res.add("");
            res.addAll(includes);
            res.add("");
            res.addAll(lines);

            Path p = Paths.get(getNewName(file.getName()));
            Files.write(p, res, StandardOpenOption.CREATE);
            return p.toFile();
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
//        while (!all.isEmpty()) {
//            String str = all.pop();
        for (ILinesReader module: modules) {
            List<String> res = new ArrayList<>();
            for (String str: all) {
                res.addAll(module.modify(str));
            }
            all = res;
        }
        //}
        return all;
    }

    public static void main(String[] args) throws IOException {
        IPreprocessor preprocessor = new Preprocessor_v1();
        File file = new File("test_preprocessor.cm");
        System.out.println(file.getAbsolutePath());
        System.out.println(file.exists());
        preprocessor.process(file);
    }
}

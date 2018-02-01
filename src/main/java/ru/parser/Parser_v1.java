package ru.parser;

import ru.bricks.graph.ConnectionsGraph;
import ru.bricks.Pair;
import ru.bricks.Replace;
import ru.bricks.command.Command;
import ru.bricks.command.ICommand;
import ru.bricks.state.State;
import ru.executors.ExecutorCommand;
import ru.executors.ExecutorCommandLocal;
import ru.preprocessor.IPreprocessor;
import ru.preprocessor.Preprocessor_v1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 *
 */
public class Parser_v1 extends Replace implements IParser {
    private Map<String, File> include_file;
    private Map<String, Pair<File, Map<String, String>>> include;
    private IPreprocessor preprocessor;
    private ExecutorCommand executor_line;
    private int gen = 0;

    Parser_v1() {
        include_file = new HashMap<>();
        include = new HashMap<>();
        preprocessor = new Preprocessor_v1();
        executor_line = new ExecutorCommandLocal();
    }

    @Override
    public ConnectionsGraph process(InputStream stream, List<String> outStates) throws IOException {
        BufferedReader resource = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        ConnectionsGraph graph = new ConnectionsGraph();

        String line;
        while ((line = resource.readLine()) != null) {
            if (line.isEmpty() || line.startsWith("#version")) {
                continue;
            }
            if (line.startsWith("#include")) {
                addInclude(line);
            } else {
                addData(line, graph);
            }
        }
        // TODO добавить фиктивную комманду, которая обединяет все входные состояния
        graph = graph.subgraphFromLeaf(new State(outStates.get(0)));

        return graph;
    }

    private void addInclude(String line) throws IOException {
        String include = getNameInclude(line);
        include_file.put(include, preprocessor.process(getFile(include)));
//        include_args.put(include, new HashMap<>());
    }

    private File getFile(String name) {
        if (!name.endsWith(".cm")) {
            name = String.format("%s.cm", name);
        }
        return new File(name);
    }

    private String getNameInclude(String line) {
        line = line.substring("#include".length());
        if (line.contains("\"")) {
            return line.substring(line.indexOf('\"')+1, line.lastIndexOf('\"')).trim();
        }
        if (line.contains("<") && line.contains(">")) {
            return line.substring(line.indexOf('<')+1, line.indexOf('>')).trim();
        }
        return line.trim();
    }

    private void addData(String line, ConnectionsGraph graph) {
        line = patternReplace(line, "include", 0);
        String[] sp = line.split("#", 2);
        String com = sp[0].split(";", 3)[2].trim();
//        Arrays.stream(in).forEach(System.out::println);
//        Arrays.stream(out).forEach(System.out::println);
//        System.out.println(command);
//        Arrays.stream(marks).forEach(System.out::println);
//        System.out.println();
        ICommand command = new Command(com, executor_line);

        Arrays.stream(sp[1].split(" "))
                .map(String::trim)
                .filter(l->!l.isEmpty())
                .forEach(l->{
                    String[] name_value = l.split("=", 2);
                    command.addMark(name_value[0], name_value[1]);
                });

        Set<State> states_in = new HashSet<>();
        Arrays.stream(sp[0].split(";", 3)[0].split(","))
                .map(String::trim)
                .filter(l->!l.isEmpty())
                .forEach(l->states_in.add(new State(l)));

        Set<State> states_out = new HashSet<>();
        Arrays.stream(sp[0].split(";", 3)[1].split(","))
                .map(String::trim)
                .filter(l->!l.isEmpty())
                .forEach(l->states_out.add(new State(l)));

        graph.addEdge(states_in, states_out, command);
    }

    private void addIncludeData(ConnectionsGraph graph) {
        // Map<String, File> include_file;
        // Map<String, Pair<File, Map<String, String>>> include;
        // TODO create CommandsGraph
    }

    @Override
    protected String replace(List<String> args) {
//        // Map<String, Map<String, List<Map<String, String>>>>
//        if (!include_args.containsKey(args.get(1))) {
//            include_args.put(args.get(1), new HashMap<>());
//        }
//        if (!include_args.get(args.get(1)).containsKey(args.get(2))) {
//            include_args.get(args.get(1)).put(args.get(2), new ArrayList<>());
//        }
        String new_name = String.format("%s::%d::%s", args.get(1), gen++, args.get(2));
        Map<String, String> my_args = new HashMap<>();
        args.stream().skip(3).forEach(l->{
            String[] name_value = l.split("=", 2);
            my_args.put(name_value[0], name_value[1]);
        });
        include.put(new_name, new Pair<>(include_file.get(args.get(1)), my_args));
//        include_args.get(args.get(1)).get(args.get(2)).add(my_args);

        return new_name;
    }

    public static void main(String[] args) throws IOException {
        Parser_v1 parser = new Parser_v1();
        ConnectionsGraph graph = parser.process(new FileInputStream(new File("test_preprocessor.cmc")),
                new LinkedList<>());
    }
}

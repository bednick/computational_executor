package ru.nikkollaii;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import ru.nikkollaii.bricks.Pair;
import ru.nikkollaii.bricks.command.CommandsGraph;
import ru.nikkollaii.bricks.command.ICommand;
import ru.nikkollaii.bricks.connectionsgraph.ConnectionsGraph;
import ru.nikkollaii.bricks.state.State;
import ru.nikkollaii.commandline.CommandRun;
import ru.nikkollaii.environment.Environment;
import ru.nikkollaii.executors.ExecutorGraph;
import ru.nikkollaii.libra.WeightTime;
import ru.nikkollaii.parser.IParser;
import ru.nikkollaii.parser.Selecting;
import ru.nikkollaii.preprocessor.Preprocessor_v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ComputationalExecutor {

    public static void main(String[] args) {
        CommandRun commandRun = new CommandRun();
        final JCommander jCommander = JCommander.newBuilder()
                //.acceptUnknownOptions(false)
                .addCommand(commandRun)
                .build();
        jCommander.setProgramName("cm_executor");
        jCommander.setCaseSensitiveOptions(false);
        jCommander.setAllowAbbreviatedOptions(true);

        try {
            jCommander.parse(args);
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            jCommander.usage();
            System.exit(1);
        }

        if (jCommander.getParsedCommand() == null) {
            jCommander.usage();
            System.exit(0);
        }
        try {
            switch (jCommander.getParsedCommand()) {
                case "run":
                    if (commandRun.isHelp()) {
                        jCommander.usage("run");
                        //jCommander.usage();
                        System.exit(0);
                    }
                    run(commandRun);
                    break;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    private static void run(CommandRun commandRun) throws IOException {
        Preprocessor_v1 preprocessor = new Preprocessor_v1();
        File correct = preprocessor.process(commandRun.getFiles().get(0));
        List<String> out = commandRun.getOutStates();
        List<State> outStates = new ArrayList<>();
        for (String str: out) {
            outStates.add(new State(str));
        }

        // Выбор парсера
        Selecting selecting = new Selecting();
        IParser parser = selecting.select(new FileInputStream(correct));

        // Разбор входного файла(ов)
        ConnectionsGraph connectionsGraph = parser.process(new FileInputStream(correct), outStates);

        Environment environment = new Environment(commandRun.getDirEnvironment());

        connectionsGraph.setAchievedStates(environment.getEnvironment());

        CommandsGraph commandsGraph = new CommandsGraph(connectionsGraph, outStates);
        commandsGraph.setExecutor(new ExecutorGraph());

        try {
            Pair<ICommand, Integer> res;
            do {
                commandsGraph.setWeight(new WeightTime());
                if (Float.isInfinite(commandsGraph.getWeight())) {
                    System.out.println("Невозможно получить выходные состояния");
                    System.exit(1);
                }
                BlockingQueue<Pair<ICommand, Integer>> blockingQueue = new LinkedBlockingQueue<>();
                commandsGraph.exec(blockingQueue);

                res = blockingQueue.take();
            } while (res.getValue() != 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(!commandRun.isNotRm() && correct.delete()) {
            System.out.println("delete *.cmc file");
        }
        System.out.println("Correct finish");
    }
}

package ru;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import ru.bricks.ConnectionsGraph;
import ru.commandline.CommandBuild;
import ru.commandline.CommandRun;
import ru.parser.IParser;
import ru.parser.Parser;
import ru.preprocessor.Preprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ComputationalExecutor {

    public static void main(String[] args) {
        CommandRun commandRun = new CommandRun();
        CommandBuild commandBuild = new CommandBuild();
        final JCommander jCommander = JCommander.newBuilder()
                .acceptUnknownOptions(false)
                .addObject(commandRun)
                .addObject(commandBuild)
                .build();
        jCommander.setProgramName("Computational Executor");
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
                    if (commandRun.getDelegate().isHelp()) {
                        jCommander.usage();
                        System.exit(0);
                    }
                    run(commandRun);
                    break;
                case "build":
                    if (commandBuild.getDelegate().isHelp()) {
                        jCommander.usage();
                        System.exit(0);
                    }
                    build(commandBuild);
                    break;
                default:
                    jCommander.usage();
                    System.exit(0);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    private static void run(CommandRun commandRun) throws IOException {
        File correct = build(new CommandBuild(commandRun.getDelegate()));
        List<String> outStates = commandRun.getOutStates();
        ConnectionsGraph connectionsGraph = new Parser().process(new FileInputStream(correct), outStates);

    }

    private static File build(CommandBuild commandBuild) throws IOException {
        // TODO add check(), (нужно ли собирать)
        Preprocessor preprocessor = new Preprocessor();
        return preprocessor.process(commandBuild.getDelegate().getFile());
    }
}

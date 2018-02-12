package ru;

import ru.bricks.command.CommandsGraph;
import ru.bricks.connectionsgraph.ConnectionsGraph;
import ru.bricks.state.State;
import ru.environment.Environment;
import ru.executors.ExecutorGraph;
import ru.parser.IParser;
import ru.parser.Selecting;
import ru.preprocessor.IPreprocessor;
import ru.preprocessor.Preprocessor_v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Test_2 {
    public static void main(String[] args) throws IOException {
        IPreprocessor preprocessor = new Preprocessor_v1();
        File file_cmc = preprocessor.process(new File("test_2.cm"));

        IParser parser = new Selecting().select(new FileInputStream(file_cmc));
        List<State> statesOut = Collections.singletonList(new State("hatch_d_e_f"));
        ConnectionsGraph graph = parser.process(new FileInputStream(file_cmc), statesOut);

        Environment environment = new Environment();
        environment.addEnvironment(new File("."));
        environment.setAchievedStates(graph);

        CommandsGraph commandsGraph = new CommandsGraph(graph, statesOut);
        commandsGraph.setExecutor(new ExecutorGraph());

        //commandsGraph.exec();
    }
}

package ru;

import ru.bricks.Pair;
import ru.bricks.command.CommandsGraph;
import ru.bricks.command.ICommand;
import ru.bricks.connectionsgraph.ConnectionsGraph;
import ru.bricks.state.State;
import ru.decision.DecisionFactory;
import ru.decision.DecisionMaker;
import ru.decision.IDecisionMaker;
import ru.environment.Environment;
import ru.executors.ExecutorGraph;
import ru.libra.WeightTime;
import ru.parser.IParser;
import ru.parser.Selecting;
import ru.preprocessor.IPreprocessor;
import ru.preprocessor.Preprocessor_v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 *
 */
public class TestDecision {
    public static void main(String[] args) throws IOException {
        IPreprocessor preprocessor = new Preprocessor_v1();
        File file_cmc = preprocessor.process(new File("big_test.cm"));

        IParser parser = new Selecting().select(new FileInputStream(file_cmc));
        List<State> statesOut = Collections.singletonList(new State("10"));
        ConnectionsGraph graph = parser.process(new FileInputStream(file_cmc), statesOut);
        System.out.println(graph);

        CommandsGraph commandsGraph = new CommandsGraph(graph, statesOut);
        commandsGraph.setExecutor(new ExecutorGraph());

        Environment environment = new Environment();
        environment.addEnvironment(new File("./test_states"));
        environment.setAchievedStates(graph);

        commandsGraph.setWeight(new WeightTime());

        BlockingQueue<Pair<ICommand, Integer>> blockingQueue = new LinkedBlockingQueue<>();
        commandsGraph.exec(blockingQueue);
        try {
            System.out.println("START wait");

            Pair<ICommand, Integer> res = blockingQueue.take();
            System.out.println("RESULT");
            System.out.println(res);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(commandsGraph.getWeight());
    }
}

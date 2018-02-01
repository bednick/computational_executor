package ru.bricks.command;

import ru.bricks.Pair;
import ru.bricks.graph.ConnectionsGraph;
import ru.decision.DecisionFactory;
import ru.decision.IDecisionMaker;
import ru.executors.ExecutorGraph;
import ru.libra.ILibra;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class CommandsGraph implements ICommand<ExecutorGraph> {
    private ConnectionsGraph graph;
    private ExecutorGraph executor;
    private Map<String, String> marks;

    public CommandsGraph(ConnectionsGraph graph) {
        this.graph = graph;
        this.marks = new HashMap<>();
    }

    @Override
    public void setExecutor(ExecutorGraph executor) {
        this.executor = executor;
    }

    @Override
    public void exec(BlockingQueue<Pair<Command, Integer>> queue) {
        executor.exec(graph, queue);
    }

    @Override
    public void addMark(String name, String value) {
        marks.put(name, value);
    }

    @Override
    public String getMark(String name) {
        return marks.get(name);
    }

    @Override
    public void setWeight(ILibra libra) {
        // TODO
        IDecisionMaker decision = DecisionFactory.getDecisionMaker(graph);

    }

    @Override
    public float getWeight() {
        return 100;
    }

    public ConnectionsGraph getGraph() {
        return graph;
    }
}

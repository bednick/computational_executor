package ru.bricks.command;

import ru.bricks.Pair;
import ru.bricks.graph.ConnectionsGraph;
import ru.bricks.state.State;
import ru.decision.DecisionFactory;
import ru.decision.IDecisionMaker;
import ru.executors.ExecutorGraph;
import ru.libra.ILibra;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class CommandsGraph implements ICommand<ExecutorGraph> {
    private ConnectionsGraph graph;
    private ExecutorGraph executor;
    private Map<String, String> marks;
    private List<State> out;
    private float weight;

    public CommandsGraph(ConnectionsGraph graph, List<State> out) {
        this.graph = graph;
        this.marks = new HashMap<>();
        this.out = out;
        weight = graph.getCommands().size() * 10;
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
        for (ICommand command: graph.getCommands()) {
            command.setWeight(libra);
        }
        // TODO установить веса для вершин графа и для самого графа (для этого необходимо строить алгоритмом сценарий)
        IDecisionMaker decision = DecisionFactory.getDecisionMaker(graph);
        weight = decision.decide(graph, out);
    }

    @Override
    public float getWeight() {
        return weight;
    }

    public ConnectionsGraph getGraph() {
        return graph;
    }
}

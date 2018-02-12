package ru.bricks.command;

import ru.bricks.Pair;
import ru.bricks.connectionsgraph.ConnectionsGraph;
import ru.bricks.state.State;
import ru.decision.DecisionFactory;
import ru.decision.IDecisionMaker;
import ru.executors.ExecutorGraph;
import ru.libra.ILibraCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class CommandsGraph implements ICommand<ExecutorGraph> {
    private ConnectionsGraph graph;
    private ExecutorGraph executor;
    private Map<String, String> marks;
    private List<State> out;
    private float weight;
    private Performance runtime;

    public CommandsGraph(ConnectionsGraph graph, List<State> out) {
        this.graph = graph;
        this.marks = new HashMap<>();
        this.out = out;
        this.weight = graph.getCommands().size() * 10;
        this.runtime = Performance.NOT_SPECIFIED;
    }

    @Override
    public void setExecutor(ExecutorGraph executor) {
        this.executor = executor;
    }

    @Override
    public void exec(BlockingQueue<Pair<ICommand, Integer>> queue) {
        executor.exec(this,  queue);
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
    public void setWeight(ILibraCommand libra) {
        for (ICommand command: graph.getCommands()) {
            command.setWeight(libra);
        }
        //установить веса для вершин графа и для самого графа (для этого необходимо строить алгоритмом сценарий)
        IDecisionMaker decision = DecisionFactory.getDecisionMaker(graph);
        weight = decision.decide(graph, out);
    }

    @Override
    public float getWeight() {
        return weight;
    }

    @Override
    public Performance getRuntime() {
        return runtime;
    }

    @Override
    public void setRuntime(Performance runtime) {
        this.runtime = runtime;
    }


    public ConnectionsGraph getGraph() {
        return graph;
    }
}

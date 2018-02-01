package ru.bricks.command;

import ru.bricks.graph.ConnectionsGraph;
import ru.executors.ExecutorGraph;
import ru.executors.IObserver;
import ru.libra.ILibra;

import java.util.HashMap;
import java.util.Map;

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
    public IObserver exec() {
        return executor.exec(graph);
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

    }

    @Override
    public float getWeight() {
        return 100;
    }

    public ConnectionsGraph getGraph() {
        return graph;
    }
}

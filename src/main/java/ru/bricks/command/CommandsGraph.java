package ru.bricks.command;

import ru.bricks.ConnectionsGraph;
import ru.executors.ExecutorGraph;

import java.util.HashMap;
import java.util.Map;

public class CommandsGraph implements ICommand<ExecutorGraph> {
    private ConnectionsGraph graph;
    private ExecutorGraph executor;
    private Map<String, String> marks;

    public CommandsGraph(ConnectionsGraph graph, ExecutorGraph executor) {
        this.graph = graph;
        this.executor = executor;
        this.marks = new HashMap<>();
    }

    @Override
    public ExecutorGraph getExecutor() {
        return executor;
    }

    @Override
    public void addMark(String name, String value) {
        marks.put(name, value);
    }

    @Override
    public String getMark(String name) {
        return marks.get(name);
    }

    public ConnectionsGraph getGraph() {
        return graph;
    }
}

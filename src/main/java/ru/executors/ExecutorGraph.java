package ru.executors;

import ru.bricks.command.CommandsGraph;
import ru.bricks.graph.ConnectionsGraph;
import ru.decision.DecisionFactory;
import ru.decision.IDecisionMaker;

import java.io.IOException;

public class ExecutorGraph implements IExecutor<ConnectionsGraph> { // CommandsGraph
    public ExecutorGraph() {}

    @Override
    public IObserver exec(ConnectionsGraph commands) {
        // запускает в отдельном потоке выполнение подзадачи (так же и основной)
        // TODO
        IDecisionMaker decision = DecisionFactory.getDecisionMaker(commands);
        // Создаётся поток в котором работает очередь задач
        return new ObserverThread();
    }

    @Override
    public boolean isAvailable() {

        return true;
    }

    @Override
    public float confidence() {
        return 1;
    }

    @Override
    public float overheads() {
        return 1;
    }
}

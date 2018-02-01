package ru.executors;

import ru.bricks.command.CommandsGraph;
import ru.decision.DecisionFactory;
import ru.decision.IDecisionMaker;

import java.io.IOException;

public class ExecutorGraph implements IExecutor<CommandsGraph> {
    public ExecutorGraph() {}

    @Override
    public IObserver exec(CommandsGraph commands) throws IOException {
        // запускает в отдельном потоке выполнение подзадачи (так же и основной)
        // TODO
        IDecisionMaker decision = DecisionFactory.getDecisionMaker(commands.getGraph());
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

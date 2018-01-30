package ru.executors;

import ru.bricks.command.CommandsGraph;

import java.io.IOException;

public class ExecutorGraph implements IExecutor<CommandsGraph> {
    public ExecutorGraph() {}

    @Override
    public IObserver exec(CommandsGraph commands) throws IOException {
        // запускает в отдельном потоке выполнение подзадачи (так же и основной)
        // TODO
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

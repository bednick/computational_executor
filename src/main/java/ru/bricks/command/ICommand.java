package ru.bricks.command;

import ru.bricks.Pair;
import ru.executors.IExecutor;
import ru.libra.ILibraCommand;

import java.util.concurrent.BlockingQueue;

public interface ICommand<T extends IExecutor> {
    void setExecutor(T executor);

    void exec(BlockingQueue<Pair<ICommand, Integer>> queue);

    void addMark(String name, String value);

    String getMark(String name);

    void setWeight(ILibraCommand libra);

    float getWeight();

    Performance getRuntime();

    void setRuntime(Performance runtime);
}

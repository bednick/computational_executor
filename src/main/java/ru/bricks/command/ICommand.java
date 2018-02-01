package ru.bricks.command;

import ru.bricks.Pair;
import ru.executors.IExecutor;
import ru.libra.ILibra;

import java.util.concurrent.BlockingQueue;

public interface ICommand<T extends IExecutor> {
    void setExecutor(T executor);

    void exec(BlockingQueue<Pair<Command, Integer>> queue);

    void addMark(String name, String value);

    String getMark(String name);

    void setWeight(ILibra libra);

    float getWeight();
}

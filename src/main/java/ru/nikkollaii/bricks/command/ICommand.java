package ru.nikkollaii.bricks.command;

import ru.nikkollaii.bricks.Pair;
import ru.nikkollaii.executors.IExecutor;
import ru.nikkollaii.libra.ILibraCommand;

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

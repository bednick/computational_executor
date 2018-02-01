package ru.bricks.command;

import ru.executors.IExecutor;
import ru.executors.IObserver;
import ru.libra.ILibra;

public interface ICommand<T extends IExecutor> {
    void setExecutor(T executor);

    IObserver exec();

    void addMark(String name, String value);

    String getMark(String name);

    void setWeight(ILibra libra);

    float getWeight();
}

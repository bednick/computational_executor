package ru.bricks.command;

import ru.executors.IExecutor;
import ru.libra.ILibra;

public interface ICommand<T extends IExecutor> {
    T getExecutor();

    void addMark(String name, String value);

    String getMark(String name);

    void setWeight(ILibra libra);

    float getWeight();
}

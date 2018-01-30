package ru.bricks.command;

import ru.executors.IExecutor;

public interface ICommand<T extends IExecutor> {
    T getExecutor();

    void addMark(String name, String value);

    String getMark(String name);
}

package ru.bricks;

import ru.executors.IExecutor;

import java.util.HashMap;

public interface ICommand<T extends IExecutor> {
    T getExecutor();

    void addMark(String name, String value);

    String getMark(String name);
}

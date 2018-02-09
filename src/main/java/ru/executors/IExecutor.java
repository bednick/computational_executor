package ru.executors;

import ru.bricks.Pair;

import ru.bricks.command.ICommand;

import java.util.concurrent.BlockingQueue;

public interface IExecutor<T extends ICommand> { //  extends ICommand

    void exec(T command, BlockingQueue<Pair<ICommand, Integer>> queue);

    // Доступен ли исполнитель на данный момент
    boolean isAvailable();

    // С какой вероятностью корректная задача будет выполнена при её запуске
    float confidence();

    // Слагаемое дополнительной нагрузки (накладные расходы на выполнение)
    float overheads(T command);
}

package ru.executors;

import ru.bricks.Pair;
import ru.bricks.command.Command;

import java.util.concurrent.BlockingQueue;

public interface IExecutor<T> { //  extends ICommand

    void exec(T command, BlockingQueue<Pair<Command, Integer>> queue);

    // Доступен ли исполнитель на данный момент
    boolean isAvailable();

    // С какой вероятностью корректная задача будет выполнена при её запуске
    float confidence();

    // Коофициент дополнительной нагрузки (накладные расходы на выполнение)
    float overheads();
}

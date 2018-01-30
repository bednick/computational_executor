package ru.executors;

import ru.bricks.command.ICommand;

import java.io.IOException;

public interface IExecutor<T extends ICommand> {

    IObserver exec(T command) throws IOException;

    // Доступен ли исполнитель на данный момент
    boolean isAvailable();

    // С какой вероятностью корректная задача будет выполнена при её запуске
    float confidence();

    // Коофициент дополнительной нагрузки (накладные расходы на выполнение)
    float overheads();
}

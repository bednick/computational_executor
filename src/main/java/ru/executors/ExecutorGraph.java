package ru.executors;

import ru.bricks.Pair;
import ru.bricks.command.Command;
import ru.bricks.graph.ConnectionsGraph;
import ru.decision.DecisionFactory;
import ru.decision.IDecisionMaker;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;


public class ExecutorGraph implements IExecutor<ConnectionsGraph> { // CommandsGraph
    public ExecutorGraph() {}

    @Override
    public void exec(ConnectionsGraph commands, BlockingQueue<Pair<Command, Integer>> queue) {
        // использовать  BlockingQueue queue для запуска 1ой! таски, которая отвечает за поток
        // запускает в отдельном потоке выполнение подзадачи (так же и основной)
        // todo
        /*
        * Ставит задачу на выполнение
        * Слушает очередь(BlockingQueue<Pair<Command(or String), Integer(result_work)>>), в которую ЗАПИСИВАЮ НАБЛЮДАТЕЛИ
        * Вносит изменения в граф, на основе результата поток прининимает одно из решений:
        * 1) ждать следующую задачу
        * 2) запуск доступных задач на выполнение
        * 3) перестроить граф (нужно решить, что делать с запушенными процессами)
        * */
        // Создаётся поток в котором работает очередь з

        Callable<Void> callable = () -> {
            return null;
        };
        // TODO использовать пул потоков! (общий для всех потоков)
        FutureTask<Void> task = new FutureTask<Void>(callable);
        Thread t = new Thread(task);
        t.start();
    }

    @Override
    public boolean isAvailable() {

        return true;
    }

    @Override
    public float confidence() {
        return 1;
    }

    @Override
    public float overheads() {
        return 1;
    }
}

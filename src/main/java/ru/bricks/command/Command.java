package ru.bricks.command;

import ru.bricks.Pair;
import ru.executors.ExecutorCommand;
import ru.libra.ILibraCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 *
 */

public class Command implements ICommand<ExecutorCommand> {
    private ExecutorCommand executor;
    private String command;
    private Map<String, String> marks = null;
    private float weight = 0;
    private Performance runtime;

    public Command(final String command) {
        this.command = command;
        this.runtime = Performance.NOT_SPECIFIED;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public void setExecutor(ExecutorCommand executor) {
        this.executor = executor;
    }

    @Override
    public void exec(BlockingQueue<Pair<ICommand, Integer>> queue) {
        executor.exec(command, queue);
    }

    public void addMark(String name, String value) {
        if (marks == null) {
            this.marks = new HashMap<>();
        }
        this.marks.put(name, value);
    }

    public String getMark(String name) {
        if (marks == null) {
            return null;
        }
        return marks.get(name);
    }

    @Override
    public void setWeight(ILibraCommand libra) {
        weight = libra.getWeight(this);
    }

    @Override
    public float getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Command command = (Command) obj;
        return this.command.equals(command.command);
    }

    @Override
    public int hashCode() {
        return command.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Command: %s ", command);
    }

    @Override
    public Performance getRuntime() {
        return runtime;
    }

    @Override
    public void setRuntime(Performance runtime) {
        this.runtime = runtime;
    }
}

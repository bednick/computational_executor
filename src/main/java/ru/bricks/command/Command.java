package ru.bricks.command;

import ru.executors.ExecutorCommand;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */

public class Command implements ICommand<ExecutorCommand> {
    private ExecutorCommand executor;
    private String command;
    private Map<String, String> marks = null;

    public Command(final String command, final ExecutorCommand executor) {
        this.command = command;
        this.executor = executor;
    }

    public String getCommand() {
        return command;
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
    public ExecutorCommand getExecutor() {
        return executor;
    }

    @Override
    public String toString() {
        return String.format("Command: %s ", command);
    }

}

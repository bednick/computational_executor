package ru.bricks;

import ru.executors.Executor;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Command {
    private Executor executor;
    private String command;
    private Map<String, String> marks = null;

    public Command(final String command, final Executor executor) {
        this.command = command;
        this.executor = executor;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
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

    public int getMarkOrDefault(String name, int def) {
        if (marks == null) {
            return def;
        }
        String rez = marks.get(name);
        if (rez == null) {
            return def;
        }
        return Integer.parseInt(rez);
    }

    public Executor getExecutor() {
        return executor;
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
}

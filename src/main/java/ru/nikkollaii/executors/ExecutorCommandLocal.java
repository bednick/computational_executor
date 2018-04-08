package ru.nikkollaii.executors;

import ru.nikkollaii.bricks.command.Command;

import java.io.IOException;

/**
 *
 */
public class ExecutorCommandLocal extends ExecutorCommand {
    @Override
    protected Process sh(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", command);
        return processBuilder.start();
    }

    @Override
    protected Process win(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", command);
        return processBuilder.start();
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
    public float overheads(Command command) {
        return 1;
    }
}

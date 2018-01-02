package ru.executors;

import java.io.IOException;

/**
 *
 */
public class Local extends Executor {
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
}

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
    protected boolean available() {
        return false;
    }

    @Override
    protected float confidence() {
        return 1;
    }

    @Override
    protected float overheads() {
        return 1;
    }
}

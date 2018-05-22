package ru.nikkollaii.executors;

import ru.nikkollaii.bricks.Pair;
import ru.nikkollaii.bricks.command.Command;
import ru.nikkollaii.bricks.command.ICommand;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 *
 */
public class ExecutorCommandLocal implements IExecutor<Command>  {
    private static boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("win");

    ExecutorCommandLocal() {}

    @Override
    public void exec(Command command, BlockingQueue<Pair<ICommand, Integer>> queue) {
        Callable<Void> callable = () -> {
            Process process = null;
            try {
                if (isWindows) {
                    process = win(command.getCommand());
                } else {
                    process = sh(command.getCommand());
                }
                String name = command.getCommand().split(" ")[0];
                setUpStreamGobbler(process.getInputStream(), System.out, name);
                setUpStreamGobbler(process.getErrorStream(), System.err, name);
            } catch (IOException e) {
                process = null;
            }
            if (process == null) {
                queue.add(new Pair<ICommand, Integer>(new Command(command.getCommand()), -1));
            } else {
                queue.add(new Pair<ICommand, Integer>(new Command(command.getCommand()), process.waitFor()));
            }
            return null;
        };
        // TODO использовать пул потоков! (общий для всех потоков)
        FutureTask<Void> task = new FutureTask<Void>(callable);
        Thread t = new Thread(task);
        t.start();
    }

    @Override
    public String name(Command command) {
        return null;
    }

    @Override
    public float confidence(Command command) {
        return 0;
    }

    @Override
    public float overheads(Command command) {
        return 0;
    }

    @Override
    public float performance(Command command) {
        return 0;
    }

    private void setUpStreamGobbler(final InputStream is, final PrintStream ps, String name) {
        final InputStreamReader streamReader = new InputStreamReader(is);
        new Thread(new Runnable() {
            public void run() {
                BufferedReader br = new BufferedReader(streamReader);
                String line = null;
                try {
                    while ((line = br.readLine()) != null) {
                        ps.println(String.format("%s >>> %s", name, line));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    protected Process sh(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", command);
        return processBuilder.start();
    }

    protected Process win(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", command);
        return processBuilder.start();
    }
}

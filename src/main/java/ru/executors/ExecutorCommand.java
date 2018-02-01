package ru.executors;

import ru.bricks.Pair;
import ru.bricks.command.Command;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 *
 */
public abstract class ExecutorCommand implements IExecutor<String>  {
    private static boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("win");

    ExecutorCommand() {}

    @Override
    public void exec(String command, BlockingQueue<Pair<Command, Integer>> queue) {
        Callable<Void> callable = () -> {
            Process process = null;
            try {
                if (isWindows) {
                    process = win(command);
                } else {
                    process = sh(command);
                }
                String name = command.split(" ")[0];
                setUpStreamGobbler(process.getInputStream(), System.out, name);
                setUpStreamGobbler(process.getErrorStream(), System.err, name);
                // создать поток, который будет ждать и после завершения поместит в очередь
                // TODO
                //return new ObserverProcess(process);
            } catch (IOException e) {
                //
            }
            if (process == null) {
                queue.add(new Pair<Command, Integer>(new Command(command), -1));
            } else {
                queue.add(new Pair<Command, Integer>(new Command(command), process.waitFor()));
            }
            return null;
        };
        // TODO использовать пул потоков! (общий для всех потоков)
        FutureTask<Void> task = new FutureTask<Void>(callable);
        Thread t = new Thread(task);
        t.start();
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

    protected abstract Process sh(String command) throws IOException;

    protected abstract Process win(String command) throws IOException;
}

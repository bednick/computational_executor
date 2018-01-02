package ru.executors;

import ru.bricks.Command;

import java.io.*;

/**
 *
 */
public abstract class Executor {
    private static boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("win");

    public Executor() {}

    public Process run(String command) throws IOException {
        Process process;
        if (isWindows) {
            process = win(command);
        } else {
            process = sh(command);
        }
        String name = command.split(" ")[0];
        setUpStreamGobbler(process.getInputStream(), System.out, name);
        setUpStreamGobbler(process.getErrorStream(), System.err, name);
        return process;
    }

    private static void setUpStreamGobbler(final InputStream is, final PrintStream ps, String name) {
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

    public abstract boolean isAvailable();
}

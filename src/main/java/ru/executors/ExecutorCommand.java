package ru.executors;


import ru.bricks.command.Command;

import java.io.*;

/**
 *
 */
public abstract class ExecutorCommand implements IExecutor<String> {
    private static boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("win");

    ExecutorCommand() {}

    @Override
    public IObserver exec(String command) {
        try {

            Process process;
            if (isWindows) {
                process = win(command);
            } else {
                process = sh(command);
            }
            String name = command.split(" ")[0];
            setUpStreamGobbler(process.getInputStream(), System.out, name);
            setUpStreamGobbler(process.getErrorStream(), System.err, name);
            return new ObserverProcess(process);
        } catch (IOException e) {
            return null;
        }
        //return process;

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

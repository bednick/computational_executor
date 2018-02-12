package ru.environment;

import ru.bricks.connectionsgraph.ConnectionsGraph;
import ru.bricks.state.Attainability;
import ru.bricks.state.State;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class Environment {
    private  Set<State> environment;

    public Environment() {
        environment = new HashSet<>();
    }

    public void addEnvironment(File path) {
        if (!path.exists()) {
            addEnvironment(new State(path.getName()));
        } else if (path.isDirectory()) {
            addDirectory(path);
        } else if (path.isFile()) {
            addFile(path);
        }
    }

    private void addDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file: files) {
            if (file.isFile()) {
                environment.add(new State(file.getName()));
            }
        }
    }

    private void addFile(File file) {
        environment.add(new State(file.getName()));
    }

    public void addEnvironment(State state) {
        environment.add(state);
    }

    public  void setAchievedStates(ConnectionsGraph graph) {
        for (State state: graph.getStates()) {
            if (environment.contains(state)) {
                state.setAttainability(Attainability.ACHIEVED);
            }
        }
    }
}

package ru.nikkollaii.environment;

import ru.nikkollaii.bricks.state.State;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class Environment {
    private  Set<State> environment;

    public Environment(List<File> directories) {
        environment = new HashSet<>();
        for(File dir: directories) {
            addDirectory(dir);
        }
    }

    public void addDirectory(File dir) {
        if (dir.exists() && dir.isDirectory()) {
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
    }

    public void addEnvironments(Collection<String> states) {
        for (String state: states) {
            environment.add(new State(state));
        }
    }

    public Set<State> getEnvironment() {
        return environment;
    }
}

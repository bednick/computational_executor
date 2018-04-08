package ru.nikkollaii.parser;

import ru.nikkollaii.bricks.connectionsgraph.ConnectionsGraph;
import ru.nikkollaii.bricks.state.State;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *
 */
public interface IParser {
    ConnectionsGraph process(InputStream stream, List<State> outStates) throws IOException;
}

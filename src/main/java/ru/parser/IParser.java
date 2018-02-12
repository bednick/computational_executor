package ru.parser;

import ru.bricks.connectionsgraph.ConnectionsGraph;
import ru.bricks.state.State;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *
 */
public interface IParser {
    ConnectionsGraph process(InputStream stream, List<State> outStates) throws IOException;
}

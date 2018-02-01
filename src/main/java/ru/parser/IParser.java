package ru.parser;

import ru.bricks.graph.ConnectionsGraph;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *
 */
public interface IParser {
    ConnectionsGraph process(InputStream stream, List<String> outStates) throws IOException;
}

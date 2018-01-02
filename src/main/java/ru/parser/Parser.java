package ru.parser;

import ru.bricks.ConnectionsGraph;

import java.io.InputStream;
import java.util.List;

/**
 *
 */
public class Parser implements IParser {

    @Override
    public ConnectionsGraph process(InputStream stream, List<String> outStates) {
        return new ConnectionsGraph();
    }
}

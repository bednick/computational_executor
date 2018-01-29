package ru.parser;

import ru.bricks.ConnectionsGraph;

import java.io.InputStream;
import java.util.List;

/**
 *
 */
public class Parser_v1 implements IParser {

    Parser_v1() {}

    @Override
    public ConnectionsGraph process(InputStream stream, List<String> outStates) {
        return new ConnectionsGraph();
    }
}

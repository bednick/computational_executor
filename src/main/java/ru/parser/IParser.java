package ru.parser;

import ru.bricks.ConnectionsGraph;

import java.io.InputStream;
import java.util.List;

/**
 *
 */
public interface IParser {
    ConnectionsGraph process(InputStream stream, List<String> outStates);
}

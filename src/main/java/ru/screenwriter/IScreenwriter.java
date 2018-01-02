package ru.screenwriter;

import ru.bricks.ConnectionsGraph;

import java.util.List;

/**
 *
 */
interface IScreenwriter {
    void exec(ConnectionsGraph connectionsGraph, List<String> outStates, String core);
}

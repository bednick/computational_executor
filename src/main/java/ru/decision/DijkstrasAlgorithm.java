package ru.decision;

import ru.bricks.connectionsgraph.ConnectionsGraph;
import ru.bricks.state.State;

import java.util.List;

/**
 *
 */
public class DijkstrasAlgorithm extends DecisionMaker {
    @Override
    public float decide(ConnectionsGraph graph, List<State> outStates) {
        updateAchievable(graph);
        return 100;
    }
}

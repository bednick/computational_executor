package ru.decision;

import ru.bricks.graph.ConnectionsGraph;
import ru.bricks.state.Attainability;
import ru.bricks.state.State;

import java.util.List;

/**
 *
 */
public class DijkstrasAlgorithm extends DecisionMaker {
    @Override
    public float decide(ConnectionsGraph graph, List<State> outStates) {
        for (State state: graph.getStates()) {
            if (state.getAttainability() == Attainability.ACHIEVED) {
                //
            }
        }
        return 100;
    }
}

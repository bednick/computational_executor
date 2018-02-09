package ru.decision;

import ru.bricks.graph.ConnectionsGraph;
import ru.bricks.graph.Vertex;
import ru.bricks.state.Attainability;
import ru.bricks.state.State;

/**
 *
 */
public abstract class DecisionMaker implements IDecisionMaker {
    protected void setNotAchievable(ConnectionsGraph connectionsGraph) {
        for (Vertex<State> in: connectionsGraph.getLeafStates()) {
            if(in.getObject().getAttainability() == Attainability.NOT_SPECIFIED) {
                in.getObject().setAttainability(Attainability.CAN_NOT_ACHIEVED);
            }
        }
    }
}

package ru.decision;

import ru.bricks.command.Command;
import ru.bricks.connectionsgraph.ConnectionsGraph;
import ru.bricks.connectionsgraph.VertexCG;
import ru.bricks.state.State;

import java.util.*;

/**
 *
 */
public class DijkstrasAlgorithm extends DecisionMaker {
    @Override
    public float decide(ConnectionsGraph graph, List<State> outStates) {
        updateAchievable(graph);
        Queue<VertexCG<Command>> queueCommands = new LinkedList<>();
        Stack<VertexCG<Command>> stackCommands = new Stack<>();

        Set<VertexCG<Command>> start = getInCommands(graph, outStates);
        queueCommands.addAll(start);
        // stackCommands.addAll(start);

        while (!queueCommands.isEmpty()) {
            VertexCG<Command> com = queueCommands.poll();
            if (!stackCommands.contains(com)) {
                stackCommands.add(com);
            }
        }

        return 100;
    }

    private Set<VertexCG<Command>> getInCommands(ConnectionsGraph graph, State outStates) {
        Set<VertexCG<Command>> allCommands = new HashSet<>();
        for (VertexCG vertexCG: graph.getVertexState(outStates).getIn()) {
            allCommands.add(vertexCG);
        }
        return allCommands;
    }

    private Set<VertexCG<Command>> getInCommands(ConnectionsGraph graph, Collection<State> states) {
        Set<VertexCG<Command>> allStates = new HashSet<>();
        for (State state: states) {
            allStates.addAll(getInCommands(graph, state));
        }
        return allStates;
    }
}

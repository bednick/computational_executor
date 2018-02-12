package ru.decision;

import ru.bricks.command.Command;
import ru.bricks.command.Performance;
import ru.bricks.connectionsgraph.ConnectionsGraph;
import ru.bricks.connectionsgraph.VertexCG;
import ru.bricks.state.Attainability;
import ru.bricks.state.State;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 *
 */
public abstract class DecisionMaker implements IDecisionMaker {

    public void updateAchievable(ConnectionsGraph connectionsGraph) {
        connectionsGraph.getLeafStates().stream()
                .filter(in -> in.getObject().getAttainability().equals(Attainability.NOT_SPECIFIED))
                .forEach(in -> in.getObject().setAttainability(Attainability.CAN_NOT_ACHIEVED));

        connectionsGraph.getCommands().stream()
                .filter(command -> command.getRuntime().equals(Performance.NOT_SPECIFIED))
                .forEach(command -> command.setRuntime(Performance.CAN_PERFORMED));

        Queue<VertexCG<State>> canNotAchieved = connectionsGraph.getVertexStates().stream()
                .filter(vs -> vs.getObject().getAttainability().equals(Attainability.CAN_NOT_ACHIEVED))
                .collect(Collectors.toCollection(LinkedList::new));

        connectionsGraph.getVertexCommands().stream()
                .filter(command -> command.getObject().getRuntime().equals(Performance.PERFORMED_INCORRECT))
                .forEach(command -> {
                    for (VertexCG<State> outState: command.getOut()) {
                        if (canAchieved(outState)) {
                            continue;
                        }
                        outState.getObject().setAttainability(Attainability.CAN_NOT_ACHIEVED);
                        canNotAchieved.add(outState);
                    }
                    command.getObject().setRuntime(Performance.CAN_NOT_PERFORMED);
                });

        while (!canNotAchieved.isEmpty()) {
            VertexCG<State> stateVertex = canNotAchieved.poll();
            for (VertexCG<Command> commandVertex: stateVertex.getOut()) {
                if (canPerformed(commandVertex)) {
                    continue;
                }
                commandVertex.getObject().setRuntime(Performance.CAN_NOT_PERFORMED);
                for (VertexCG<State> outState: commandVertex.getOut()) {
                    if (canAchieved(outState)) {
                        continue;
                    }
                    outState.getObject().setAttainability(Attainability.CAN_NOT_ACHIEVED);
                    canNotAchieved.add(outState);
                }
            }
        }

    }

    private boolean canPerformed(VertexCG<Command> commandVertex) {
        if (commandVertex.getObject().getRuntime().equals(Performance.CAN_NOT_PERFORMED)) {
            return false;
        }
        for (VertexCG<State> inState: commandVertex.getIn()) {
            if (inState.getObject().getAttainability().equals(Attainability.CAN_NOT_ACHIEVED)) {
                return false;
            }
        }
        return true;
    }

    private boolean canAchieved(VertexCG<State> stateVertex) {
        if (stateVertex.getObject().getAttainability().equals(Attainability.CAN_NOT_ACHIEVED)) {
            return false;
        }
        for (VertexCG<Command> inCommand: stateVertex.getIn()) {
            if (inCommand.getObject().getRuntime().equals(Performance.CAN_PERFORMED)) {
                return true;
            }
        }
        return false;
    }
}

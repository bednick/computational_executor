package ru.decision;

import ru.bricks.command.ICommand;
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

    public void updateAchievable(ConnectionsGraph graph) {
        for (ICommand command: graph.getCommands()) {
            switch (command.getRuntime()) {
                case TRAJECTORY:
                case CAN_PERFORMED:
                    command.setRuntime(Performance.NOT_SPECIFIED);
            }
        }

        for (State state: graph.getStates()) {
            if (state.getAttainability().equals(Attainability.CAN_ACHIEVED)) {
                state.setAttainability(Attainability.NOT_SPECIFIED);
            }
        }

        graph.getRootStates().stream()
                .filter(in -> in.getObject().getAttainability().equals(Attainability.NOT_SPECIFIED))
                .forEach(in -> in.getObject().setAttainability(Attainability.CAN_NOT_ACHIEVED));

        graph.getVertexCommands().stream()
                .filter(command -> command.getObject().getRuntime().equals(Performance.PERFORMED_INCORRECT))
                .forEach(command -> {
                    for (VertexCG<State, ICommand> outState: command.getOut()) {
                        if (!canBeAchieved(outState)) {
                            outState.getObject().setAttainability(Attainability.CAN_NOT_ACHIEVED);
                        }
                    }
                    command.getObject().setRuntime(Performance.CAN_NOT_PERFORMED);
                });

        Queue<VertexCG<State, ICommand>> canNotAchieved = graph.getVertexStates().stream()
                .filter(vs -> vs.getObject().getAttainability().equals(Attainability.CAN_NOT_ACHIEVED))
                .collect(Collectors.toCollection(LinkedList::new));

        while (!canNotAchieved.isEmpty()) {
            VertexCG<State, ICommand> stateVertex = canNotAchieved.remove();
            for (VertexCG<ICommand, State> commandVertex: stateVertex.getOut()) {
                if (canPerformed(commandVertex)) {
                    continue;
                }
                commandVertex.getObject().setRuntime(Performance.CAN_NOT_PERFORMED);

                for (VertexCG<State, ICommand> outState: commandVertex.getOut()) {
                    if (!canBeAchieved(outState)) {
                        outState.getObject().setAttainability(Attainability.CAN_NOT_ACHIEVED);
                        canNotAchieved.add(outState);
                    }
                }
            }
        }

        graph.getStates().stream()
                .filter(in -> in.getAttainability().equals(Attainability.NOT_SPECIFIED))
                .forEach(in -> in.setAttainability(Attainability.CAN_ACHIEVED));

        graph.getCommands().stream()
                .filter(com -> com.getRuntime().equals(Performance.NOT_SPECIFIED))
                .forEach(com -> com.setRuntime(Performance.CAN_PERFORMED));

    }

    private boolean canPerformed(VertexCG<ICommand, State> commandVertex) {
        if (commandVertex.getObject().getRuntime().equals(Performance.CAN_NOT_PERFORMED)) {
            return false;
        }
        for (VertexCG<State, ICommand> inState: commandVertex.getIn()) {
            if (inState.getObject().getAttainability().equals(Attainability.CAN_NOT_ACHIEVED)) {
                return false;
            }
        }
        return true;
    }

    private boolean canBeAchieved(VertexCG<State, ICommand> stateVertex) {
        if (stateVertex.getObject().getAttainability().equals(Attainability.CAN_NOT_ACHIEVED)) {
            return false;
        }
        for (VertexCG<ICommand, State> inCommand: stateVertex.getIn()) {
            switch (inCommand.getObject().getRuntime()) {
                case PERFORMED_CORRECT:
                case CAN_PERFORMED:
                case RUNNING:
                case NOT_SPECIFIED:
                    return true;
            }
        }
        return false;
    }
}

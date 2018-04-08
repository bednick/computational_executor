package ru.nikkollaii.decision;

import ru.nikkollaii.bricks.Pair;
import ru.nikkollaii.bricks.command.ICommand;
import ru.nikkollaii.bricks.command.Performance;
import ru.nikkollaii.bricks.connectionsgraph.ConnectionsGraph;
import ru.nikkollaii.bricks.connectionsgraph.VertexCG;
import ru.nikkollaii.bricks.state.Attainability;
import ru.nikkollaii.bricks.state.State;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class DijkstrasAlgorithm extends DecisionMaker {
    // достигнутые/провереные состояния
    private HashMap<VertexCG<State, ICommand>, Pair<Float, VertexCG<ICommand, State>>> checkStates;
    // взвещенные комманды(достигнуты/проверены все входные состояния)
    private HashMap<VertexCG<ICommand, State>, Float> checkCommands;
    // очередь на рассмотрение, (входные состояния рассмотрены, а сама ещё нет)
    private Queue<VertexCG<ICommand, State>> vertexCGQueue;

    @Override
    public float decide(ConnectionsGraph graph, List<State> outStates) {
        updateAchievable(graph);
        for (State outState: outStates) {
            if(graph.getVertexState(outState).getObject().getAttainability().equals(Attainability.CAN_NOT_ACHIEVED)) {
                return Float.POSITIVE_INFINITY;
            }
        }

        checkStates = new HashMap<>();
        checkCommands = new HashMap<>();
        addInCheckStates(graph.getVertexStates().stream()
                .filter(v -> v.getObject().getAttainability().equals(Attainability.ACHIEVED))
                .collect(Collectors.toList()));


        Set<VertexCG<ICommand, State>> roots = graph.getRootCommands();
        addInCheckCommands(roots);


        vertexCGQueue = new LinkedList<>();
        addInVertexCGQueue(checkStates.keySet());

        while (!vertexCGQueue.isEmpty()) {
            VertexCG<ICommand, State> commandVertexCG = vertexCGQueue.remove();

            checkCommands.put(commandVertexCG, getWeightCommand(commandVertexCG));
            addInVertexCGQueue(commandVertexCG.getOut());
        }

        return setPaths(graph, outStates);
    }

    private void addInCheckStates(Collection<VertexCG<State, ICommand>> collectionCheck) {
        for (VertexCG<State, ICommand> state: collectionCheck) {
            switch (state.getObject().getAttainability()) {
                case CAN_NOT_ACHIEVED:
                    continue;
                case ACHIEVED:
                    checkStates.put(state, new Pair<>(0f, null));
                case CAN_ACHIEVED:
                    Pair<Float, VertexCG<ICommand, State>> pair = getWeightState(state);
                    if (!Float.isNaN(pair.getKey())) {
                        checkStates.put(state, pair);
                    }
            }
        }
    }

    private void addInCheckCommands(Collection<VertexCG<ICommand, State>> collectionCheck) {
        for (VertexCG<ICommand, State> commandVertexCG: collectionCheck) {
            float weight = getWeightCommand(commandVertexCG);
            if (!Float.isNaN(weight)) {
                checkCommands.put(commandVertexCG, weight);
                addInCheckStates(commandVertexCG.getOut());
            }
        }
    }

    private float getWeightCommand(VertexCG<ICommand, State> commandVertexCG) {
        ICommand command = commandVertexCG.getObject();
        float weight = command.getWeight();

        switch (command.getRuntime()) {
            case CAN_NOT_PERFORMED:
            case PERFORMED_INCORRECT:
                return Float.NaN;
            case CAN_PERFORMED:
                break;
            case RUNNING:
                weight /= 2;
                break;
            case PERFORMED_CORRECT:
                weight = 0;
                break;
            case NOT_SPECIFIED:
            default:
                throw new RuntimeException("NOT_SPECIFIED");
        }

        for (VertexCG<State, ICommand> stateIn: commandVertexCG.getIn()) {
            Pair<Float, VertexCG<ICommand, State>> pair = checkStates.get(stateIn);
            if (pair == null) {
                return Float.NaN;
            }
            weight += pair.getKey();
        }

        return weight;
    }

    private Pair<Float, VertexCG<ICommand, State>> getWeightState(VertexCG<State, ICommand> stateVertexCG) {
        float weight = Float.POSITIVE_INFINITY;
        VertexCG<ICommand, State> minimum = null;

        for (VertexCG<ICommand, State> in: stateVertexCG.getIn()) {
            Float inWeight;
            switch (in.getObject().getRuntime()) {
                case CAN_NOT_PERFORMED:
                case PERFORMED_INCORRECT:
                    break;
                case PERFORMED_CORRECT:
                    weight = 0;
                    minimum = in;
                    break;
                case CAN_PERFORMED:
                    inWeight = checkCommands.get(in);
                    if (inWeight == null) {
                        return new Pair<>(Float.NaN, null);
                    }
                    if (inWeight < weight) {
                        minimum = in;
                        weight = inWeight;
                    }
                    break;
                case RUNNING:
                    inWeight = in.getObject().getWeight();
                    if (inWeight/2 < weight) {
                        minimum = in;
                        weight = inWeight/2;
                    }
                    break;
                case NOT_SPECIFIED:
                    throw new RuntimeException("NOT_SPECIFIED");
            }
        }
        if (minimum == null) {
            weight = 0;
        }
        return new Pair<>(weight, minimum);
    }

    private void addInVertexCGQueue(Collection<VertexCG<State, ICommand>> collectionCheck) {
        for (VertexCG<State, ICommand> state : collectionCheck) {
            for (VertexCG<ICommand, State> com: state.getOut()) {
                if (!checkCommands.containsKey(com) && isCorrect(com)) {
                    vertexCGQueue.add(com);
                    addInCheckStates(com.getIn());
                    addInCheckStates(com.getOut());
                }
            }
        }
    }

    private boolean isCorrect(VertexCG<ICommand, State> com) {
        Performance perf = com.getObject().getRuntime();
        return perf.equals(Performance.RUNNING) || (perf.equals(Performance.CAN_PERFORMED) && canPerformed(com));
    }

    private boolean canPerformed(VertexCG<ICommand, State> com) {
        for (VertexCG<State, ICommand> in: com.getIn()) {
            if (in.getObject().getAttainability().equals(Attainability.CAN_NOT_ACHIEVED)) {
                return false;
            }
        }
        return true;
    }

    private float setPaths(ConnectionsGraph graph, List<State> outStates) {
        float allWeight = 0;

        for (State outState: outStates) {
            VertexCG<ICommand, State> minVertex = null;
            float minWeight = Float.POSITIVE_INFINITY;
            VertexCG<State, ICommand> vertexOutState = graph.getVertexState(outState);
            for (VertexCG<ICommand, State> in: vertexOutState.getIn()) {
                Float weight = checkCommands.get(in);
                if (weight == null) {
                    continue;
                }
                if (weight < minWeight) {
                    minVertex = in;
                    minWeight = weight;
                }
            }
            setPath(minVertex);
            allWeight += minWeight;
        }

        return allWeight;
    }

    private void setPath(VertexCG<ICommand, State> command) {
        command.getObject().setRuntime(Performance.TRAJECTORY);

        for (VertexCG<State, ICommand> in: command.getIn()) {
            Pair<Float, VertexCG<ICommand, State>> pair = checkStates.get(in);
            if (pair == null || pair.getValue() == null) {
                continue;
            }
            setPath(pair.getValue());
        }
    }

}

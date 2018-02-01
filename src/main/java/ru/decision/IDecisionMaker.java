package ru.decision;

import ru.bricks.graph.ConnectionsGraph;
import ru.bricks.state.State;

import java.util.List;

/**
 * Отвечает за выбор пути выполнения
 *   Выставляем Значения в states and commands
 */
public interface IDecisionMaker {

    ConnectionsGraph decide(ConnectionsGraph connectionsGraph, List<State> outStates);
}

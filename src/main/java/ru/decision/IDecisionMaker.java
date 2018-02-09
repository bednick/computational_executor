package ru.decision;

import ru.bricks.graph.ConnectionsGraph;
import ru.bricks.state.State;
import ru.libra.ILibra;

import java.util.List;

/**
 * Отвечает за выбор пути выполнения
 *   Выставляем Значения в states and commands
 */
public interface IDecisionMaker {

    float decide(ConnectionsGraph connectionsGraph, List<State> outStates);
}

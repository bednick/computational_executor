package ru.nikkollaii.decision;

import ru.nikkollaii.bricks.connectionsgraph.ConnectionsGraph;
import ru.nikkollaii.bricks.state.State;

import java.util.List;

/**
 * Отвечает за выбор пути выполнения
 *   Выставляем Значения в states and commands
 */
public interface IDecisionMaker {

    float decide(ConnectionsGraph connectionsGraph, List<State> outStates);
}

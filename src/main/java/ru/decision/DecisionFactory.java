package ru.decision;

import ru.bricks.graph.ConnectionsGraph;

/**
 *
 */
public class DecisionFactory {
    public static IDecisionMaker getDecisionMaker(ConnectionsGraph graph) {
        // Анализ графа и входных аргументов и последующий выбор алгоритма работы
        return new DijkstrasAlgorithm();
    }
}

package ru;

import ru.bricks.connectionsgraph.ConnectionsGraph;
import ru.bricks.state.State;
import ru.decision.DecisionFactory;
import ru.decision.DecisionMaker;
import ru.decision.IDecisionMaker;
import ru.parser.IParser;
import ru.parser.Selecting;
import ru.preprocessor.IPreprocessor;
import ru.preprocessor.Preprocessor_v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class TestDecision {
    public static void main(String[] args) throws IOException {
        IPreprocessor preprocessor = new Preprocessor_v1();
        File file_cmc = preprocessor.process(new File("test_0.cm"));

        IParser parser = new Selecting().select(new FileInputStream(file_cmc));
        List<State> statesOut = Collections.singletonList(new State("c"));
        ConnectionsGraph graph = parser.process(new FileInputStream(file_cmc), statesOut);

        System.out.println(graph);

        IDecisionMaker maker = DecisionFactory.getDecisionMaker(graph);
        float weight = maker.decide(graph, statesOut);
        System.out.println(weight);
    }
}

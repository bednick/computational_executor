package ru.bricks.connectionsgraph;

import ru.bricks.command.ICommand;
import ru.bricks.state.State;

import java.util.*;

/**
 *
 */
public class ConnectionsGraph {
    private Map<State, VertexCG<State, ICommand>> states;
    private Set<VertexCG<State, ICommand>> rootStates;
    private Set<VertexCG<State, ICommand>> leafStates;

    private Map<ICommand, VertexCG<ICommand, State>> commands;
    private Set<VertexCG<ICommand, State>> rootCommands;
    private Set<VertexCG<ICommand, State>> leafCommands;

    public ConnectionsGraph() {
        this.states = new HashMap<>();
        this.rootStates = new HashSet<>();
        this.leafStates = new HashSet<>();

        this.commands = new HashMap<>();
        this.rootCommands = new HashSet<>();
        this.leafCommands = new HashSet<>();
    }

    public void addState(State state) {
        if (states.containsKey(state)) {
            return;
        }
        VertexCG<State, ICommand> vertex = new VertexCG<>(state);
        states.put(state, vertex);
        rootStates.add(vertex);
        leafStates.add(vertex);
    }

    public void addCommand(ICommand command) {
        if (commands.containsKey(command)) {
            return;
        }
        VertexCG<ICommand, State> vertex = new VertexCG<>(command);
        commands.put(command, vertex);
        rootCommands.add(vertex);
        leafCommands.add(vertex);
    }

    public void addEdge(Set<State> in, Set<State> out, ICommand command) {
        addEdges(in, command);
        addEdges(command, out);
    }

    public void addEdges(Set<State> in, ICommand command) {
        addCommand(command);
        VertexCG<ICommand, State> com = commands.get(command);

        for (State state: in) {
            addState(state);
            VertexCG<State, ICommand> v_in = states.get(state);
            v_in.addOut(com);
            com.addIn(v_in);
            rootCommands.remove(com);
            leafStates.remove(v_in);
        }
    }

    public void addEdges(ICommand command, Set<State> out) {
        addCommand(command);
        VertexCG<ICommand, State> com = commands.get(command);

        for (State state: out) {
            addState(state);
            VertexCG<State, ICommand> v_out = states.get(state);
            com.addOut(v_out);
            v_out.addIn(com);
            leafCommands.remove(com);
            rootStates.remove(v_out);
        }

    }

    public void addEdge(ICommand command, State state) {
        VertexCG<ICommand, State> vertex_command;
        if (commands.containsKey(command)) {
            vertex_command = commands.get(command);
            leafCommands.remove(vertex_command);

        } else {
            vertex_command = new VertexCG<>(command);
            commands.put(command, vertex_command);
            rootCommands.add(vertex_command);
        }

        VertexCG<State, ICommand> vertex_state;
        if (states.containsKey(state)) {
            vertex_state = states.get(state);
            rootStates.remove(vertex_state);
        } else {
            vertex_state = new VertexCG<>(state);
            states.put(state, vertex_state);
            leafStates.add(vertex_state);
        }

        vertex_command.addOut(vertex_state);
        vertex_state.addIn(vertex_command);
    }

    public VertexCG<State, ICommand> getVertexState(State state) {
        return states.get(state);
    }

    public VertexCG<ICommand, State> getVertexCommand(ICommand command) {
        return commands.get(command);
    }

    public Set<VertexCG<State, ICommand>> getRootStates() {
        return rootStates;
    }

    public Set<VertexCG<State, ICommand>> getLeafStates() {
        return leafStates;
    }

    public Set<VertexCG<ICommand, State>> getRootCommands() {
        return rootCommands;
    }

    public Set<VertexCG<ICommand, State>> getLeafCommands() {
        return leafCommands;
    }

    public ConnectionsGraph subgraphFromLeaf(Collection<State> statesLeaf) {
        Set<VertexCG<ICommand, State>> allCommands = new HashSet<>();
        Queue<VertexCG<State, ICommand>> queue = new LinkedList<>();
        for (State stateLeaf: statesLeaf) {
            VertexCG<State, ICommand> leaf = getVertexState(stateLeaf);
            queue.add(leaf);
        }

        ConnectionsGraph subgraph = new ConnectionsGraph();
        while (!queue.isEmpty()) {
            VertexCG<State, ICommand> state = queue.poll();
            subgraph.addState(state.getObject());
            for (VertexCG<ICommand, State> inCom: state.getIn()) {
                if (allCommands.contains(inCom)) {
                    continue;
                }
                allCommands.add(inCom);
                subgraph.addEdge(inCom.getObject(), state.getObject());
                Set<State> in = new HashSet<>();
                for (VertexCG<State, ICommand> inState: inCom.getIn()) {
                    in.add(inState.getObject());
                    queue.add(inState);
                }
                subgraph.addEdges(in, inCom.getObject());
            }
        }


        return subgraph;
    }

    public Set<VertexCG<ICommand, State>> getVertexCommands() {
        return new HashSet<>(commands.values());
    }

    public Set<VertexCG<State, ICommand>> getVertexStates() {
        return new HashSet<>(states.values());
    }

    public Set<ICommand> getCommands() {
        return commands.keySet();
    }

    public Set<State> getStates() {
        return states.keySet();
    }
}

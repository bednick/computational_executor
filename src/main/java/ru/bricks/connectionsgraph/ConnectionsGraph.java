package ru.bricks.connectionsgraph;

import ru.bricks.command.ICommand;
import ru.bricks.state.State;

import java.util.*;

/**
 *
 */
public class ConnectionsGraph {
    private Map<State, VertexCG<State>> states;
    private Set<VertexCG<State>> rootStates;
    private Set<VertexCG<State>> leafStates;

    private Map<ICommand, VertexCG<ICommand>> commands;
    private Set<VertexCG<ICommand>> rootCommands;
    private Set<VertexCG<ICommand>> leafCommands;

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
        VertexCG<State> vertex = new VertexCG<>(state);
        states.put(state, vertex);
        rootStates.add(vertex);
        leafStates.add(vertex);
    }

    public void addCommand(ICommand command) {
        if (commands.containsKey(command)) {
            return;
        }
        VertexCG<ICommand> vertex = new VertexCG<>(command);
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
        VertexCG<ICommand> com = commands.get(command);

        for (State state: in) {
            addState(state);
            VertexCG<State> v_in = states.get(state);
            v_in.addOut(com);
            com.addIn(v_in);
            rootCommands.remove(com);
            leafStates.remove(v_in);
        }
    }

    public void addEdges(ICommand command, Set<State> out) {
        addCommand(command);
        VertexCG<ICommand> com = commands.get(command);

        for (State state: out) {
            addState(state);
            VertexCG<State> v_out = states.get(state);
            com.addOut(v_out);
            v_out.addIn(com);
            leafCommands.remove(com);
            rootStates.remove(v_out);
        }

    }

    public void addEdge(ICommand command, State state) {
        VertexCG<ICommand> vertex_command;
        if (commands.containsKey(command)) {
            vertex_command = commands.get(command);
            leafCommands.remove(vertex_command);

        } else {
            vertex_command = new VertexCG<>(command);
            commands.put(command, vertex_command);
            rootCommands.add(vertex_command);
        }

        VertexCG<State> vertex_state;
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

    public VertexCG<State> getVertexState(State state) {
        return states.get(state);
    }

    public VertexCG<ICommand> getVertexCommand(ICommand command) {
        return commands.get(command);
    }

    public Set<VertexCG<State>> getRootStates() {
        return rootStates;
    }

    public Set<VertexCG<State>> getLeafStates() {
        return leafStates;
    }

    public Set<VertexCG<ICommand>> getRootCommands() {
        return rootCommands;
    }

    public Set<VertexCG<ICommand>> getLeafCommands() {
        return leafCommands;
    }

    public ConnectionsGraph subgraphFromLeaf(Collection<State> statesLeaf) {
        Set<VertexCG<ICommand>> allCommands = new HashSet<>();
        Queue<VertexCG<State>> queue = new LinkedList<>();
        for (State stateLeaf: statesLeaf) {
            VertexCG<State> leaf = getVertexState(stateLeaf);
            queue.add(leaf);
        }

        ConnectionsGraph subgraph = new ConnectionsGraph();
        while (!queue.isEmpty()) {
            VertexCG<State> state = queue.poll();
            subgraph.addState(state.getObject());
            for (VertexCG inCom: state.getIn()) {
                if (allCommands.contains(inCom)) {
                    continue;
                }
                allCommands.add(inCom);
                subgraph.addEdge((ICommand) inCom.getObject(), state.getObject());
                Set<State> in = new HashSet<>();
                for (Object inState: inCom.getIn()) {
                    VertexCG<State> _in = (VertexCG<State>) inState;
                    in.add(_in.getObject());
                    queue.add(_in);
                }
                subgraph.addEdges(in, (ICommand) inCom.getObject());
            }
        }


        return subgraph;
    }

    public Set<VertexCG<ICommand>> getVertexCommands() {
        return new HashSet<>(commands.values());
    }

    public Set<VertexCG<State>> getVertexStates() {
        return new HashSet<>(states.values());
    }

    public Set<ICommand> getCommands() {
        return commands.keySet();
    }

    public Set<State> getStates() {
        return states.keySet();
    }
}

package ru.bricks.graph;

import ru.bricks.command.ICommand;
import ru.bricks.state.State;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
public class ConnectionsGraph {
    private Map<State, Vertex<State>> states;
    private Set<Vertex<State>> rootStates;
    private Set<Vertex<State>> leafStates;

    private Map<ICommand, Vertex<ICommand>> commands;
    private Set<Vertex<ICommand>> rootCommands;
    private Set<Vertex<ICommand>> leafCommands;

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
        Vertex<State> vertex = new Vertex<>(state);
        states.put(state, vertex);
        rootStates.add(vertex);
        leafStates.add(vertex);
    }

    public void addCommand(ICommand command) {
        if (commands.containsKey(command)) {
            return;
        }
        Vertex<ICommand> vertex = new Vertex<>(command);
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
        Vertex<ICommand> com = commands.get(command);

        for (State state: in) {
            addState(state);
            Vertex<State> v_in = states.get(state);
            v_in.addOut(com);
            com.addIn(v_in);
            rootCommands.remove(com);
            leafStates.remove(v_in);
        }
    }

    public void addEdges(ICommand command, Set<State> out) {
        addCommand(command);
        Vertex<ICommand> com = commands.get(command);

        for (State state: out) {
            addState(state);
            Vertex<State> v_out = states.get(state);
            com.addOut(v_out);
            v_out.addIn(com);
            leafCommands.remove(com);
            rootStates.remove(v_out);
        }

    }

    public void addEdge(ICommand command, State state) {
        Vertex<ICommand> vertex_command;
        if (commands.containsKey(command)) {
            vertex_command = commands.get(command);
            leafCommands.remove(vertex_command);

        } else {
            vertex_command = new Vertex<>(command);
            commands.put(command, vertex_command);
            rootCommands.add(vertex_command);
        }

        Vertex<State> vertex_state;
        if (states.containsKey(state)) {
            vertex_state = states.get(state);
            rootStates.remove(vertex_state);
        } else {
            vertex_state = new Vertex<>(state);
            states.put(state, vertex_state);
            leafStates.add(vertex_state);
        }

        vertex_command.addOut(vertex_state);
        vertex_state.addIn(vertex_command);
    }

    public Vertex<State> getVertexState(State state) {
        return states.get(state);
    }

    public Vertex<ICommand> getVertexCommand(ICommand command) {
        return commands.get(command);
    }

    public Set<Vertex<State>> getRootStates() {
        return rootStates;
    }

    public Set<Vertex<State>> getLeafStates() {
        return leafStates;
    }

    public Set<Vertex<ICommand>> getRootCommands() {
        return rootCommands;
    }

    public Set<Vertex<ICommand>> getLeafCommands() {
        return leafCommands;
    }

    public ConnectionsGraph subgraphFromLeaf(Collection<State> statesLeaf) {
        Set<Vertex<ICommand>> allCommands = new HashSet<>();
        Queue<Vertex<State>> queue = new LinkedList<>();
        for (State stateLeaf: statesLeaf) {
            Vertex<State> leaf = getVertexState(stateLeaf);
            queue.add(leaf);
        }

        ConnectionsGraph subgraph = new ConnectionsGraph();
        while (!queue.isEmpty()) {
            Vertex<State> state = queue.poll();
            subgraph.addState(state.getObject());
            for (Vertex inCom: state.getIn()) {
                if (allCommands.contains(inCom)) {
                    continue;
                }
                allCommands.add(inCom);
                subgraph.addEdge((ICommand) inCom.getObject(), state.getObject());
                Set<State> in = new HashSet<>();
                for (Object inState: inCom.getIn()) {
                    Vertex<State> _in = (Vertex<State>) inState;
                    in.add(_in.getObject());
                    queue.add(_in);
                }
                subgraph.addEdges(in, (ICommand) inCom.getObject());
            }
        }


        return subgraph;
    }

    public Set<Vertex<ICommand>> getVertexCommands() {
        return new HashSet<>(commands.values());
    }

    public Set<Vertex<State>> getVertexStates() {
        return new HashSet<>(states.values());
    }

    public Set<ICommand> getCommands() {
        return commands.keySet();
    }

    public Set<State> getStates() {
        return states.keySet();
    }
}

package ru.bricks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
}

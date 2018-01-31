package ru.bricks;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class Vertex<T> {
    private Set<Vertex> in;
    private Set<Vertex> out;
    private T object;
    private int weight;

    public Vertex(T object) {
        this.object = object;
        this.in = new HashSet<Vertex>();
        this.out = new HashSet<Vertex>();
    }

    public T getObject() {
        return object;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void addIn(Vertex vertex) {
        in.add(vertex);
    }

    public void addIn(Collection<Vertex> vertices) {
        in.addAll(vertices);
    }

    public void addOut(Vertex vertex) {
        out.add(vertex);
    }

    public void addOut(Collection<Vertex<T>> vertices) {
        out.addAll(vertices);
    }

    public Set<Vertex> getIn() {
        return in;
    }

    public Set<Vertex> getOut() {
        return out;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Vertex vertex = (Vertex)obj;
        return this.object.equals(vertex.object);
    }

    @Override
    public int hashCode() {
        return object.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Vertex: ").append(object.toString()).append(System.lineSeparator());
        builder.append("    in: ");
        for (Vertex v: in) {
            builder.append(v.object.toString()).append(',').append(' ');
        }
        builder.append(System.lineSeparator());
        builder.append("    out: ");
        for (Vertex v: out) {
            builder.append(v.object.toString()).append(',').append(' ');
        }
        builder.append(System.lineSeparator());
        return builder.toString();
    }
}

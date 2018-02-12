package ru.bricks.connectionsgraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class VertexCG<T> {
    private Set<VertexCG> in;
    private Set<VertexCG> out;
    private T object;

    public VertexCG(T object) {
        this.object = object;
        this.in = new HashSet<VertexCG>();
        this.out = new HashSet<VertexCG>();
    }

    public T getObject() {
        return object;
    }

    public void addIn(VertexCG vertex) {
        in.add(vertex);
    }

    public void addIn(Collection<VertexCG> vertices) {
        in.addAll(vertices);
    }

    public void addOut(VertexCG vertex) {
        out.add(vertex);
    }

    public void addOut(Collection<VertexCG<T>> vertices) {
        out.addAll(vertices);
    }

    public Set<VertexCG> getIn() {
        return in;
    }

    public Set<VertexCG> getOut() {
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
        VertexCG vertex = (VertexCG)obj;
        return this.object.equals(vertex.object);
    }

    @Override
    public int hashCode() {
        return object.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("VertexCG: ").append(object.toString()).append(System.lineSeparator());
        builder.append("    in: ");
        for (VertexCG v: in) {
            builder.append(v.object.toString()).append(',').append(' ');
        }
        builder.append(System.lineSeparator());
        builder.append("    out: ");
        for (VertexCG v: out) {
            builder.append(v.object.toString()).append(',').append(' ');
        }
        builder.append(System.lineSeparator());
        return builder.toString();
    }
}

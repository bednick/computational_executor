package ru.bricks;

/**
 *
 */
public class State {
    private Attainability attainability;
    private String name;

    public State(String name) {
        this.name = name;
        this.attainability = Attainability.NOT_SPECIFIED;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Attainability getAttainability() {
        return attainability;
    }

    public void setAttainability(Attainability attainability) {
        this.attainability = attainability;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        State state = (State) obj;
        return this.name.equals(state.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return String.format("State %s %s", name, attainability);
    }

}

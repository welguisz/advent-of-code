package com.dwelguisz.utilities.graph.graph;

import java.util.Objects;

public class DirectedEdge<T> {
    public T source;
    public T destination;
    public long cost;
    private int hashCode;
    public DirectedEdge(T source, T destination, long cost) {
        this.source = source;
        this.destination = destination;
        this.cost = cost;
        this.hashCode = Objects.hash(this.source, this.destination, this.cost);
    }

    @Override
    public String toString() {
        return "" + source + " -> " + destination + "; cost: " + cost;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null | getClass() != o.getClass()) return false;
        DirectedEdge<T> other = (DirectedEdge<T>) o;
        return source.equals(other.source) && destination.equals(other.destination) && (cost == other.cost);
    }
}

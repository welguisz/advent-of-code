package com.dwelguisz.utilities.graph.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vertex<T> {
    public T id;
    public List<Edge> neighbors;
    private int hashCode;

    public Vertex(T id) {
        this.id = id;
        this.neighbors = new ArrayList<>();
        hashCode = Objects.hash(id, neighbors);
    }

    public void addEdge(Edge<T> e) {
        this.neighbors.add(e);
    }

    public List<Edge> getNeighbors() {
        return this.neighbors;
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
        Vertex oV = (Vertex) o;
        return (id.equals(oV.id) && this.neighbors.equals(neighbors));
    }
}

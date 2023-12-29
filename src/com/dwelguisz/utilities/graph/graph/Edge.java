package com.dwelguisz.utilities.graph.graph;

import java.util.Objects;

public class Edge<T> {
    public Vertex<T> u;
    public Vertex<T> v;
    private int hashCode;
    public Edge(Vertex<T> u, Vertex<T> v) {
        this.u = u;
        this.v = v;
        this.hashCode = Objects.hash(this.u, this.v);
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
        Edge oE = (Edge) o;
        return (oE.u.equals(u) && oE.v.equals(v));
    }

    public Vertex<T> getAnother(Vertex u) {
        if (u == this.u) {
            return v;
        }
        return this.u;
    }
}

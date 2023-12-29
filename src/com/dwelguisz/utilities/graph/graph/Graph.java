package com.dwelguisz.utilities.graph.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph<T> {
    public Map<T, Vertex<T>> vertices;
    public List<Edge<T>> edges;

    public Graph() {
        this.vertices = new HashMap<>();
        this.edges = new ArrayList<>();
    }

    public Graph(Graph o) {
        this.vertices = new HashMap<>();
        this.edges = new ArrayList<>();
        for (Object vertexObject : o.vertices.entrySet()) {
            Map.Entry<T, Vertex<T>> vertex = (Map.Entry<T, Vertex<T>>) vertexObject;
            this.vertices.put(vertex.getKey(), vertex.getValue());
        }
        for (Object edgeObject: o.edges) {
            Edge<T> edge = (Edge<T>) edgeObject;
            this.edges.add(edge);
        }
        this.edges = new ArrayList<>(o.edges);
    }

    public int size() {
        return this.vertices.size();
    }
    public void addVertex(T id) {
        Vertex<T> newVertex = new Vertex<>(id);
        vertices.put(id, newVertex);
    }

    public void addEdges(T u, T v, int cost) {
        Vertex<T> uV = vertices.get(u);
        Vertex<T> vV = vertices.get(v);
        Edge<T> edge = new Edge<>(uV, vV);
        uV.addEdge(edge);
        vV.addEdge(edge);
        edges.add(edge);
    }

    public List<Graph<T>> minCut(int number) {
        MinCut mincut = new MinCut(new Graph(this));
        int value = 0;
        while (value != number) {
            mincut = new MinCut(new Graph(this));
            mincut.reduceGraphToTwoVertices();
            value = mincut.countCuts();
            List<Graph<T>> tmpValues = mincut.groupVertices();
        }
        return mincut.groupVertices();
    }


}

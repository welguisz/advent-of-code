package com.dwelguisz.utilities.graph.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class MinCut<T> {
    Graph graph;
    Map<T, T> parent;
    Map<T, Integer> rank;

    public MinCut(Graph<T> graph) {
        this.graph = new Graph<>(graph);
        parent = new HashMap<>();
        rank = new HashMap<>();
        graph.vertices.keySet().stream().forEach(v -> parent.put(v,v));
        graph.vertices.keySet().stream().forEach(v -> rank.put(v,0));
    }

    public void reduceGraphToTwoVertices() {
        int v = graph.vertices.size();
        while (v > 2) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(graph.edges.size());
            T u = ((Edge<T>) graph.edges.get(randomIndex)).u.id;
            T w = ((Edge<T>) graph.edges.get(randomIndex)).v.id;
            T setU = find(u);
            T setW = find(w);
            if (!setU.equals(setW)) {
                v--;
                union(setU, setW);
            }
            graph.edges.remove(randomIndex);
        }
    }

    public Integer countCuts() {
        Integer counts = 0;
        for (Object eO : graph.edges) {
            Edge e = (Edge) eO;
            T setU = find((T) e.u.id);
            T setV = find((T) e.v.id);
            if (!setU.equals(setV)) {
                counts++;
            }
        }
        return counts;
    }

    public List<Graph<T>> groupVertices() {
        Map<T, List<T>> groups = (Map<T, List<T>>) graph.vertices.keySet().stream()
                .collect(Collectors.groupingBy(t -> find((T) t)));
        List<Graph<T>> finalGroups = new ArrayList<>();
        for (Map.Entry<T, List<T>> entry : groups.entrySet()) {
            Graph newGraph = new Graph<T>();
            for (T t : entry.getValue()) {
                newGraph.addVertex(t);
            }
            finalGroups.add(newGraph);
        }
        return finalGroups;
    }

    public T find(T i) {
        if (parent.get(i).equals(i)) {
            return i;
        }
        return find(parent.get(i));
    }

    public void union (T x, T y) {
        T xroot = find(x);
        T yroot = find(y);
        if (!xroot.equals(yroot)) {
            if (rank.get(xroot) < rank.get(yroot)) {
                T temp = xroot;
                xroot = yroot;
                yroot = temp;
            }
            parent.put(yroot, xroot);
            if (rank.get(xroot).equals(rank.get(yroot))) {
                Integer value = rank.get(xroot);
                value++;
                rank.put(xroot, value);
            }
        }
    }

}

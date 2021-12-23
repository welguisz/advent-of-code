package com.dwelguisz.year2021.helper.day15;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode
@Getter
@Setter
public class Graph {
    private Set<Node> nodes = new HashSet<>();
    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }
}

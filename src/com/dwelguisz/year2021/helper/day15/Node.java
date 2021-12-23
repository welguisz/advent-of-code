package com.dwelguisz.year2021.helper.day15;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@Getter
@Setter
public class Node {
    private String name;
    private List<Node> shortestPath = new LinkedList<>();
    private Integer distance = Integer.MAX_VALUE;
    private Integer right = Integer.MAX_VALUE;
    private Integer left = Integer.MAX_VALUE;
    private Integer up = Integer.MAX_VALUE;
    private Integer down = Integer.MAX_VALUE;
    Map<Node, Integer> adjacentNodes = new HashMap<>();

    public Node(String name) {
        this.name = name;
    }

    public void addDestination(Node nodeA, Integer distance) {
        adjacentNodes.put(nodeA, distance);
    }

}

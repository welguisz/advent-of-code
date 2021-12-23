package com.dwelguisz.year2021;

import com.dwelguisz.year2021.helper.Tuple;
import com.dwelguisz.year2021.helper.day15.Graph;
import com.dwelguisz.year2021.helper.day15.Node;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static com.dwelguisz.year2021.helper.day15.Dijkstra.calculateShortestPath;
import static java.lang.Integer.parseInt;

public class AdventDay15 {
    public static List<Node> riskNodes = new ArrayList<>();
    public static Integer MAP_SIZE = 10;
    public static Integer[][] riskMap = new Integer[MAP_SIZE][MAP_SIZE];

    public static void main(String[] args) {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2021/day15/testcase.txt");
//        createRiskMap(lines);
//        Graph graph = createGraph(1,1);
//        Graph finalGraph = calculateShortestPath(graph, riskNodes.get(new Tuple(0,0)));
    }

//    public static Graph createGraph(Integer rows, Integer cols) {
//        for(Pair<Integer,Integer> loc : riskNodes.keySet()) {
//            System.out.println(String.format("Linking nodes at location: %d,%d",loc.getLeft(),loc.getRight()));
//            Node currentNode = riskNodes.get(loc);
//            List<Pair<Integer, Integer>> neighborNodes = new ArrayList<>();
//            neighborNodes.add(new ImmutablePair<>(loc.getLeft()-1,loc.getRight()));
//            neighborNodes.add(new ImmutablePair<>(loc.getLeft()+1,loc.getRight()));
//            neighborNodes.add(new ImmutablePair<>(loc.getLeft(),loc.getRight()-1));
//            neighborNodes.add(new ImmutablePair<>(loc.getLeft(),loc.getRight()+1));
//            for(Pair<Integer, Integer> neighbor : neighborNodes) {
//                Node tmpNode = riskNodes.get(neighbor);
//                if (tmpNode != null) {
//                    System.out.println(String.format("--- Linking to %d,%d",neighbor.getLeft(),neighbor.getRight()));
//                    currentNode.addDestination(tmpNode, riskMap[neighbor.getLeft()][neighbor.getRight()]);
//                }
//            }
//        }
//        Graph finalVersion = new Graph();
//        for(Pair<Integer, Integer> node : riskNodes.keySet()) {
//            finalVersion.addNode(riskNodes.get(node));
//        }
//        return finalVersion;
//    }
//
//    public static void createRiskMap(List<String> lines) {
//        Integer y = 0;
//        for (String line : lines) {
//            for (Integer x=0; x < line.length(); x++) {
//                riskMap[y][x] = parseInt(Character.toString(line.charAt(x)));
//                Pair<Integer, Integer> loc = new ImmutablePair(y,x);
//                Node currentNode = new Node(String.format("Node %d,%d", loc.getLeft(), loc.getRight()));
//                riskNodes.put(loc, currentNode);
//            }
//            y++;
//        }
//    }
}

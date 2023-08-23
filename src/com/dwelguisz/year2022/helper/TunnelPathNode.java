package com.dwelguisz.year2022.helper;

import com.dwelguisz.base.SearchNode;
import com.dwelguisz.year2022.AoC2022Day16;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TunnelPathNode extends SearchNode<String>  {
    public AoC2022Day16.Valve currentValve;
    Map<Pair<String,String>,Integer> pathCosts;
    Map<String, AoC2022Day16.Valve> valveMap;
    public List<String> valvesWithFlows;
    Integer timeLeft;
    Integer pressure;

    public TunnelPathNode (
            AoC2022Day16.Valve currentValve,
            Map<Pair<String,String>,Integer> pathCosts,
            List<SearchNode> visitedNodes,
            Map<String, AoC2022Day16.Valve> valveMap,
            Integer timeLeft,
            Integer pressure,
            List<String> valvesWithFlows) {
        this.currentValve = currentValve;
        this.pathCosts = pathCosts;
        setVisitedNodes(visitedNodes);
        this.valveMap = valveMap;
        this.timeLeft = timeLeft;
        this.pressure = pressure;
        this.valvesWithFlows = valvesWithFlows;
    }

    @Override
    public Object getCompareObject1() {
        return timeLeft;
    }

    public Object getCompareObject2() {
        return pressure;
    }


    public Integer compareFlows(String a, String b) {
        return valveMap.get(b).flowRate - valveMap.get(a).flowRate;
    }
    public List<SearchNode> getNextNodes(String[][] map) {
        List<String> nextNodes = new ArrayList<>(valvesWithFlows);
        nextNodes.remove(currentValve.name);
        List<String> alreadyVisited = visitedNodes.stream().map(v -> v.getName()).collect(Collectors.toList());
        nextNodes.removeAll(alreadyVisited);
        nextNodes = nextNodes.stream().sorted((a,b) -> compareFlows(a,b)).collect(Collectors.toList());
        List<SearchNode> nodes = new ArrayList<>();
        Integer newPressure = currentValve.flowRate + pressure;
        for (String node : nextNodes) {
            Integer newTimeLeft = timeLeft - (pathCosts.get(Pair.of(currentValve.name, node)) + 1);
            List<SearchNode> newSearchNodes = new ArrayList<>(visitedNodes);
            newSearchNodes.add(this);
            nodes.add(new TunnelPathNode(valveMap.get(node), pathCosts, newSearchNodes, valveMap, newTimeLeft, newPressure, valvesWithFlows));
        }
        return nodes;
    }

    @Override
    public Boolean onTarget() {
        return timeLeft < 0;
    }

    public String getName() {
        String tmp = visitedNodes.stream().map(vn -> ((AoC2022Day16.Valve)vn.getObj()).name).collect(Collectors.joining(" -> "));
        if (tmp.length() > 0) {
            tmp += " -> ";
        }
        tmp += currentValve.name;
        return tmp;
    }

    @Override
    public Object getObj() {
        return currentValve;
    }

    @Override
    public Integer getSteps() {
        Integer pressure = 0;
        Integer time = timeLeft;
        Integer index = 0;
        while (index + 1 < visitedNodes.size()) {
            time -= (pathCosts.get(Pair.of(visitedNodes.get(index).getName(),visitedNodes.get(index+1).getName())) + 1);
            index++;
            if (time >= 0) {
                pressure += valveMap.get(visitedNodes.get(index).getName()).flowRate * (time);
            }
        }
        return pressure;
    }

    public static int compare(Object o, Object o1) {
        SearchNode a = (SearchNode) o;
        SearchNode b = (SearchNode) o1;
        Integer aObj1 = (Integer)a.getCompareObject1();
        Integer bObj1 = (Integer)b.getCompareObject1();
        Integer diff = aObj1 - bObj1;
        if (diff == 0) {
            Integer aObj2 = (Integer)a.getCompareObject2();
            Integer bObj2 = (Integer)b.getCompareObject2();
            return bObj2 - aObj2;
        }
        return diff;
    }
}

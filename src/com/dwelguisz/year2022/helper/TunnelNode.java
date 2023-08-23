package com.dwelguisz.year2022.helper;

import com.dwelguisz.base.SearchNode;
import com.dwelguisz.year2022.ProboscideaVolcanium;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TunnelNode extends SearchNode<String> {
    public ProboscideaVolcanium.Valve currentValve;
    public ProboscideaVolcanium.Valve targetValve;
    public Set<ProboscideaVolcanium.Valve> visitedValves;
    public Map<String, ProboscideaVolcanium.Valve> valveMap;

    public TunnelNode(
            ProboscideaVolcanium.Valve currentValve,
            ProboscideaVolcanium.Valve targetNode,
            List<SearchNode> visitedNodes,
            Map<String, ProboscideaVolcanium.Valve> valveMap
    ) {
        this.currentValve = currentValve;
        this.targetValve = targetNode;
        this.visitedNodes = visitedNodes;
        this.valveMap = valveMap;
        setVisitedNodes(visitedNodes);
    }

    @Override
    public Integer getSteps() {
        return visitedNodes.size();
    }

    @Override
    public String getName() {
        return currentValve.name;
    }
    public List<SearchNode> getNextNodes(String[][] map) {
        List<String> nextTunnels = currentValve.tunnelsDestinations;
        List<String> alreadyVisited = visitedNodes.stream().map(v -> v.getName()).collect(Collectors.toList());
        List<String> notVisitedYet = nextTunnels.stream().filter(s -> !alreadyVisited.contains(s)).collect(Collectors.toList());
        List<ProboscideaVolcanium.Valve> nextValves = notVisitedYet.stream().map(s -> valveMap.get(s)).collect(Collectors.toList());
        List<SearchNode> nextNodes = new ArrayList<>();
        for (ProboscideaVolcanium.Valve valve : nextValves) {
            List<SearchNode> newCurrentPath = new ArrayList<>(this.visitedNodes);
            newCurrentPath.add(this);
            TunnelNode nextTunnel = new TunnelNode(valve, targetValve, newCurrentPath, valveMap);
            nextNodes.add(nextTunnel);
        }
        return nextNodes;
    }

    public Boolean onTarget() {
        return this.targetValve.name.equals(currentValve.name);
    }

}

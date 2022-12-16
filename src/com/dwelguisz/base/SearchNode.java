package com.dwelguisz.base;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Set;


public abstract class SearchNode<T> {
    public Pair<Integer, Integer> location;
    public Pair<Integer, Integer> targetLocation;
    public List<SearchNode> visitedNodes;
    public Set<Pair<Integer,Integer>> visitedLocations;

    public String name;


    public void setLocation(Pair<Integer, Integer> loc) {
        this.location = loc;
    }
    public void setTargetLocation(Pair<Integer, Integer> targetLocation) {
        this.targetLocation = targetLocation;
    }
    public void setVisitedNodes(List<SearchNode> visitedNodes) {
        this.visitedNodes = visitedNodes;
    }

    public void setVisitedLocations(Set<Pair<Integer, Integer>> visitedLocations) {
        this.visitedLocations = visitedLocations;
    }

    public Pair<Integer, Integer> getLocation() {
        return location;
    }
    public Pair<Integer, Integer> getTargetLocation() {
        return targetLocation;
    }
    public List<SearchNode> getVisitedNodes() {
        return visitedNodes;
    }
    public Set<Pair<Integer,Integer>> getVisitedLocations() {
        return visitedLocations;
    }
    public Pair<Integer, Integer> getLocationPair() {
        return location;
    }

    public Integer distanceToTarget() {
        Integer horizontal = (targetLocation.getRight() - location.getRight());
        Integer vertical = (targetLocation.getLeft() - location.getLeft());
        return (horizontal * horizontal) + (vertical * vertical);
    }

    public Integer manhattanDistance() {
        Integer horizontal = (targetLocation.getRight() - location.getRight());
        Integer vertical = (targetLocation.getLeft() - location.getLeft());
        return Math.abs(horizontal) + Math.abs(vertical);
    }

    public Integer getSteps() {
        return visitedNodes.size();
    }

    Boolean haveWeVisitedThisLocation(Pair<Integer,Integer> nextLocation) {
        return visitedLocations.contains(nextLocation);
    }

    public Boolean onTarget() {
        return distanceToTarget() == 0;
    }

    public abstract String getName();

    public Object getObj() {
        return "To do here";
    }

    public abstract List<SearchNode> getNextNodes(T map[][]);
}

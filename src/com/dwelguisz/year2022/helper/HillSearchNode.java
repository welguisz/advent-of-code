package com.dwelguisz.year2022.helper;

import com.dwelguisz.base.SearchNode;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HillSearchNode extends SearchNode<String> {
    public HillSearchNode(
            Pair<Integer, Integer> currentLocation,
            Pair<Integer, Integer> targetLocation,
            List<SearchNode> visited,
            Set<Pair<Integer, Integer>> visitedLocations
    ) {
        setLocation(currentLocation);
        setTargetLocation(targetLocation);
        setVisitedNodes(visited);
        setVisitedLocations(visitedLocations);
    }

    public String getName() {
        return location.toString();
    }
    public List<SearchNode> getNextNodes(String[][] map) {
        Boolean[] validNextSteps = validMoves(map);
        List<SearchNode> newNodes = new ArrayList<>();
        List<SearchNode> newVisited = new ArrayList<>(getVisitedNodes());
        Set<Pair<Integer, Integer>> newLocs = new HashSet<>(getVisitedLocations());
        newVisited.add(this);
        if (validNextSteps[0]) {
            newNodes.add(
                    new HillSearchNode(
                            Pair.of(getLocation().getLeft() -1,getLocation().getRight()),
                            getTargetLocation(),
                            newVisited,
                            newLocs));
        }
        if (validNextSteps[1]) {
            newNodes.add(
                    new HillSearchNode(
                            Pair.of(getLocation().getLeft()+1,getLocation().getRight()),
                            getTargetLocation(),
                            newVisited,
                            newLocs));
        }
        if (validNextSteps[2]) {
            newNodes.add(
                    new HillSearchNode(
                            Pair.of(getLocation().getLeft(),getLocation().getRight()-1),
                            getTargetLocation(),
                            newVisited,
                            newLocs));
        }
        if (validNextSteps[3]) {
            newNodes.add(
                    new HillSearchNode(
                            Pair.of(getLocation().getLeft(),getLocation().getRight()+1),
                            getTargetLocation(),
                            newVisited,
                            newLocs));
        }
        return newNodes;
    }

    public Boolean[] validMoves(String[][] map) {
        Integer x = getLocation().getLeft();
        Integer y = getLocation().getRight();
        Boolean[] stepResult = allowedMovement(map);
        return new Boolean[]{
                stepResult[0] && !getVisitedLocations().contains(Pair.of(x-1,y)),
                stepResult[1] && !getVisitedLocations().contains(Pair.of(x+1,y)),
                stepResult[2] && !getVisitedLocations().contains(Pair.of(x,y-1)),
                stepResult[3] && !getVisitedLocations().contains(Pair.of(x,y+1))
        };
    }

    public Boolean[] allowedMovement(String[][] map) {
        Integer x = getLocation().getLeft();
        Integer y = getLocation().getRight();
        Integer currentLoc = getHeight(map,x,y);

        Integer up = getHeight(map,x-1,y);
        Integer down = getHeight(map,x+1,y);
        Integer left = getHeight(map,x,y-1);
        Integer right = getHeight(map,x,y+1);
        return new Boolean[]{currentLoc +2 > up, currentLoc + 2 > down, currentLoc + 2 > left, currentLoc + 2 > right};
    }

    public Integer getHeight(String[][] map, Integer x, Integer y) {
        if (x < 0 || x >= map.length || y < 0 || y >= map[x].length) {
            return 37;
        }
        String height = map[x][y];
        if (height.equals("S")) {
            return 1;
        } else if (height.equals("E")) {
            return 26;
        } else {
            return Integer.valueOf(height.charAt(0)) - 96;
        }
    }


}

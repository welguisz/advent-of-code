package com.dwelguisz.year2020.helper;

import java.util.*;
import java.util.stream.Collectors;

public class ConwayCubeBoard {
    Set<List<Integer>> cells;

    public ConwayCubeBoard() {
        cells = new HashSet<>();
    }

    public void add(List<Integer> point) {
        cells.add(point);
    }

    public boolean contains(List<Integer> point) {
        return cells.contains(point);
    }

    public Integer size() {
        return cells.size();
    }


    public ConwayCubeBoard step() {
        Map<List<Integer>, Integer> neighborhoods = new HashMap<>();
        for(List<Integer> point: cells) {
            int x = point.get(0);
            int y = point.get(1);
            int z = point.get(2);
            for (int i = x - 1; i < x + 2; i++) {
                for (int j = y - 1; j < y + 2; j++) {
                    for (int k = z - 1; k < z + 2; k++) {
                        Integer ints[] = {i, j, k};
                        List<Integer> neighbor = Arrays.stream(ints).collect(Collectors.toList());
                        if (neighborhoods.containsKey(neighbor)) {
                            Integer val = neighborhoods.get(neighbor);
                            neighborhoods.put(neighbor, val + 1);
                        } else {
                            neighborhoods.put(neighbor, 1);
                        }
                    }
                }
            }
        }
        ConwayCubeBoard newBoard = new ConwayCubeBoard();
        for(Map.Entry<List<Integer>, Integer> point : neighborhoods.entrySet()) {
            if ((point.getValue() == 3) ||
                    ((point.getValue() == 4) && cells.contains(point.getKey()))) {
                newBoard.add(point.getKey());
            }
        }
        return newBoard;
    }

    public ConwayCubeBoard stepPart2() {
        Map<List<Integer>, Integer> neighborhoods = new HashMap<>();
        for(List<Integer> point: cells) {
            int x = point.get(0);
            int y = point.get(1);
            int z = point.get(2);
            int w = point.get(3);
            for (int i = x - 1; i < x + 2; i++) {
                for (int j = y - 1; j < y + 2; j++) {
                    for (int k = z - 1; k < z + 2; k++) {
                        for(int l = w - 1; l < w + 2; l++) {
                            Integer ints[] = {i, j, k, l};
                            List<Integer> neighbor = Arrays.stream(ints).collect(Collectors.toList());
                            if (neighborhoods.containsKey(neighbor)) {
                                Integer val = neighborhoods.get(neighbor);
                                neighborhoods.put(neighbor, val + 1);
                            } else {
                                neighborhoods.put(neighbor, 1);
                            }
                        }
                    }
                }
            }
        }
        ConwayCubeBoard newBoard = new ConwayCubeBoard();
        for(Map.Entry<List<Integer>, Integer> point : neighborhoods.entrySet()) {
            if ((point.getValue() == 3) ||
                    ((point.getValue() == 4) && cells.contains(point.getKey()))) {
                newBoard.add(point.getKey());
            }
        }
        return newBoard;
    }


}

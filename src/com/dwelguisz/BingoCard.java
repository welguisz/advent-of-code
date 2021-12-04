package com.dwelguisz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BingoCard {
    private List<List<Integer>> card;
    private List<Map<Integer, Boolean>> rows;
    private List<Map<Integer, Boolean>> cols;

    public BingoCard(final List<List<Integer>> card) {
        this.card = card;
        createRows();
        createCols();
    }

    public boolean insertNewNumber(final int value) {
        return doesCardHaveNumber(value);
    }

    public int sumOfNonMarkedValues() {
        int sum = 0;
        List<Integer> nonMarkedValues = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            nonMarkedValues.addAll(rows.get(i).entrySet().stream()
                    .filter(entry -> !entry.getValue())
                    .map(entry -> entry.getKey())
                    .collect(Collectors.toList()));
        }
        for (int i = 0; i < nonMarkedValues.size(); i++) {
            sum += nonMarkedValues.get(i);
        }
        return sum;
    }

    public boolean doesCardHaveNumber(final int value) {
        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (card.get(i).get(j) == value) {
                    rows.get(i).put(value, true);
                    cols.get(j).put(value, true);
                    return (rows.get(i).entrySet().stream().allMatch(entry -> entry.getValue()))
                            || (cols.get(j).entrySet().stream().allMatch(entry -> entry.getValue()));
                }
            }
        }
        return false;
    }

    private void createRows() {
        this.rows = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            Map state = new HashMap<>();
            for (int j = 0; j < 5; j++) {
                state.put(card.get(i).get(j), false);
            }
            rows.add(state);
        }
    }

    private void createCols() {
        this.cols = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            Map state = new HashMap<>();
            for (int j = 0; j < 5; j++) {
                state.put(card.get(j).get(i), false);
            }
            cols.add(state);
        }
    }
}

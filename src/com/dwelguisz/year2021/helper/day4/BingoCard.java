package com.dwelguisz.year2021.helper.day4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BingoCard {
    private List<List<Integer>> card;
    private List<Map<Integer, Boolean>> rows;
    private List<Map<Integer, Boolean>> cols;
    private int winner = 0;

    public BingoCard(final List<List<Integer>> card) {
        this.card = card;
        createRows();
        createCols();
        winner = 0;
    }

    public int getWinner() {
        return this.winner;
    }

    public boolean insertNewNumber(final int value) {
        if (this.winner != 0) {
            if (doesCardHaveNumber(value)) {
                this.winner = value * sumOfNonMarkedValues();
                return true;
            }
        }
        return false;
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

    public boolean winningCard() {
        for (int i = 0; i < 5; i++) {
            if (rows.get(i).entrySet().stream().allMatch(entry -> entry.getValue())
                    || (cols.get(i).entrySet().stream().allMatch(entry -> entry.getValue()))) {
                return true;
            }
        }
        return false;
    }

    public boolean doesCardHaveNumber(final int value) {
        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (card.get(i).get(j) == value) {
                    rows.get(i).put(value, true);
                    cols.get(j).put(value, true);
                    return winningCard();
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

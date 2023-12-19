package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2021.helper.day4.BingoCard;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class GiantSquid extends AoCDay {

    private List<Integer> numbersCalled;
    private List<BingoCard> bingoCards;
    private List<BingoCard> orderedWinners;
    private List<Integer> orderedWinnersValue;

    public GiantSquid() {
        super();
        this.numbersCalled = new ArrayList<>();
        this.bingoCards = new ArrayList<>();
        this.orderedWinners = new ArrayList<>();
        this.orderedWinnersValue = new ArrayList<>();
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2021,4,false,0);
        createGame(instructions);
        playThroughAllWinners();
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = orderedWinnersValue.get(0);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = orderedWinnersValue.get(orderedWinnersValue.size()-1);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private void createGame(List<String> instructions) {
        List<List<Integer>> card = new ArrayList<>();
        bingoCards = new ArrayList<>();
        for (int i = 0; i < instructions.size(); i++) {
            if (i == 0) {
                numbersCalled = Arrays.stream(instructions.get(0).split(",")).map(str -> parseInt(str)).collect(Collectors.toList());
            } else if (instructions.get(i).length() < 2) {
                if (card.size() == 5) {
                    bingoCards.add(new BingoCard(card));
                }
                card = new ArrayList<>();
            } else {
                List<Integer> row = Arrays.stream(instructions.get(i).split("\\s+"))
                        .filter(str -> str.length() > 0)
                        .map(str -> parseInt(str))
                        .collect(Collectors.toList());
                card.add(row);
            }
        }
        if (card.size() == 5) {
            bingoCards.add(new BingoCard(card));
        }
    }

    private void playThroughAllWinners() {
        for (int pos = 0; pos < numbersCalled.size(); pos++) {
            List<Boolean> status = new ArrayList<>();
            for (int i = 0; i < bingoCards.size(); i++) {
                status.add(false);
            }
            for (int i = 0; i < bingoCards.size(); i++) {
                status.set(i, bingoCards.get(i).doesCardHaveNumber(numbersCalled.get(pos)));
            }
            if (pos >= 5) {
                List<Integer> removeCards = new ArrayList<>();
                for (int i = 0; i < bingoCards.size(); i++) {
                    if (status.get(i)) {
                        orderedWinnersValue.add(bingoCards.get(i).sumOfNonMarkedValues() * numbersCalled.get(pos));
                        orderedWinners.add(bingoCards.get(i));
                        removeCards.add(i);
                    }
                }
                if (!removeCards.isEmpty()) {
                    for (int j = removeCards.size() - 1; j >= 0; j--) {
                        bingoCards.remove(bingoCards.get(removeCards.get(j)));
                    }
                }
            }
        }
    }

}

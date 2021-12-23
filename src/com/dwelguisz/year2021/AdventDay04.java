package com.dwelguisz.year2021;

import com.dwelguisz.year2021.helper.day4.BingoCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Integer.parseInt;

public class AdventDay04 {

    static private List<Integer> numbersCalled;
    static private List<BingoCard> bingoCards;
    static private List<BingoCard> orderedWinners;
    static private List<Integer> orderedWinnersValue;

    public static void main(String[] args) {
        List<String> instructions = readFile("/home/dwelguisz/advent_of_code/src/resources/year2021/day4/input.txt");
        createGame(instructions);
        playThroughAllWinners();
        int part1 = orderedWinnersValue.get(0);
        int part2 = orderedWinnersValue.get(orderedWinnersValue.size()-1);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    private static void createGame(List<String> instructions) {
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

    private static void playThroughAllWinners() {
        orderedWinners = new ArrayList<>();
        orderedWinnersValue = new ArrayList<>();
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

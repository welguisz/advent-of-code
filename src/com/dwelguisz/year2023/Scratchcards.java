package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Scratchcards extends AoCDay {

    List<Integer> winningSizePerCard;
    public void solve() {
        this.winningSizePerCard = new ArrayList<>();
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 4, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Long solutionPart1(List<String> lines) {
        Long total = 0L;
        for (String line : lines) {
            String [] parts = line.split(" \\| ");
            List<Integer> winningNumbers = Arrays.stream(parts[0].split(": ")[1].split("\\s+"))
            .filter(s -> s.length() > 0)
                    .map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> myNumbers = Arrays.stream(parts[1].split("\\s+"))
                    .filter(s -> s.length() > 0)
                    .map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> goodNumbers = myNumbers.stream().filter(num -> winningNumbers.contains(num)).collect(Collectors.toList());
            winningSizePerCard.add(goodNumbers.size());
            Long val = (long) Math.pow(2,goodNumbers.size()-1);
            total += val;
        }
        return total;
    }

    private Long solutionPart2(List<String> lines) {
        Map<Integer, Integer> winners = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            winners.put(i, 1);
        }
        int cardNumber = 0;
        for (Integer winningSize : winningSizePerCard) {
            Integer currentNumber = winners.get(cardNumber);
            for (int i = 1; i <= winningSize; i++) {
                Integer val = winners.get(cardNumber + i);
                val += currentNumber;
                winners.put(cardNumber+i,val);
            }
            cardNumber++;
        }
        return winners.values().stream().mapToLong(i -> i).sum();
    }
}

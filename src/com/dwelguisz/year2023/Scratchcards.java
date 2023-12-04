package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Scratchcards extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 4, false, 0);
        List<Integer> winningNumsPerCard = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(winningNumsPerCard);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(winningNumsPerCard);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Set<Integer> createSet(String val) {
        return Arrays.stream(val.split(("\\s+"))).filter(s -> s.length() > 0).map(Integer::parseInt).collect(Collectors.toSet());
    }

    private List<Integer> parseLines(List<String> lines) {
        return lines.stream()
                .map(l -> l.split(":\\s+")[1])
                .map(l -> {
                    String [] parts = l.split(" \\| ");
                    Set<Integer> winningNumbers = createSet(parts[0]);
                    Set<Integer> myNumbers = createSet(parts[1]);
                    myNumbers.retainAll(winningNumbers);
                    return myNumbers.size();
                })
                .collect(Collectors.toList());
    }
    private Long solutionPart1(List<Integer> winningSizePerCard) {
        return winningSizePerCard.stream()
                .filter(i -> i > 0)
                .mapToLong(i -> (long) Math.pow(2,i-1))
                .sum();
    }

    private Long solutionPart2(List<Integer> winningSizePerCard) {
        Map<Integer, Integer> winners = new HashMap<>();
        for (int i = 0; i < winningSizePerCard.size(); i++) {
            winners.put(i, 1);
        }
        int cardNumber = 0;
        for (Integer winningSize : winningSizePerCard) {
            final Integer tmpNum = winners.get(cardNumber);
            for (int i = 1; i <= winningSize; i++) {
                winners.merge(cardNumber+i,tmpNum,(v1,v2) -> v1+v2);
            }
            cardNumber++;
        }
        return winners.values().stream().mapToLong(i -> i).sum();
    }
}

package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class CamelCards extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 7, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines, false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart1(lines, true);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Long solutionPart1(List<String> lines, boolean part2) {
        PriorityQueue<Pair<Integer, Integer>> camelCards = new PriorityQueue<>(1000, Comparator.comparingInt((Pair<Integer, Integer> a) -> a.getLeft()));
        for(String l : lines) {
            String s[] = l.split(" ");
            camelCards.add(Pair.of(parseCards(s[0], part2), Integer.parseInt(s[1])));
        }
        Long total = 0L;
        int i = 1;
        while (!camelCards.isEmpty()) {
            total += i * camelCards.poll().getRight();
            i++;
        }
        return total;
    }
    Integer parseCards(final String a, boolean part2) {
        String newA = a;
        if (!part2) {
            newA = newA.replaceAll("J","X");
        }
        Integer maxHand = 0;
        for (String c : "23456789TJQKA".split("")) {
            String tmp = new String(newA);
            tmp = tmp.replaceAll("J",c);
            maxHand = Integer.max(maxHand, scoringHands(tmp));
        }
        String rankOrder = part2 ? "J23456789TQKA" : "23456789TJQKA";
        List<Character> rank = Arrays.stream(rankOrder.split("")).map(s -> s.charAt(0)).collect(Collectors.toList());
        Integer score = maxHand;
        for(char b : a.toCharArray()) {
            score *= rank.size();
            score += rank.indexOf(b);
        }
        return score;
    }

    Integer scoringHands(String a) {
        Map<Character, Integer> values = new HashMap<>();
        for (char b : a.toCharArray()) {
            Integer value = values.getOrDefault(b, 0);
            value ++;
            values.put(b,value);
        }
        List<List<Integer>> handOrder = List.of(List.of(1,1,1,1,1), List.of(2,1,1,1), List.of(2,2,1), List.of(3,1,1),List.of(3,2),List.of(4,1),List.of(5));
        List<Integer> valueSizes = values.values().stream().sorted().collect(Collectors.toList());
        Collections.reverse(valueSizes);
        return handOrder.indexOf(valueSizes);
    }
}

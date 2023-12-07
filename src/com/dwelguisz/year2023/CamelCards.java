package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CamelCards extends AoCDay {
    List<List<Long>> HAND_ORDER = List.of(
            List.of(1L,1L,1L,1L,1L),  //High card
            List.of(2L,1L,1L,1L),  //Pair
            List.of(2L,2L,1L), //Two Pair
            List.of(3L,1L,1L), //Three of a Kind
            List.of(3L,2L), //Full House
            List.of(4L,1L), // Four of a Kind
            List.of(5L) //Five of a kind
    );
    
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
        lines.stream()
                .forEach(l -> {
                    String s[] = l.split(" ");
                    camelCards.add(Pair.of(parseCards(s[0], part2), Integer.parseInt(s[1])));
                });
        return IntStream.range(1,camelCards.size()+1).mapToLong(i -> i * camelCards.poll().getRight()).sum();
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
        Map<Character, Long> values = a.chars().mapToObj(c -> (char)c)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        List<Long> valueSizes = values.values().stream().sorted().collect(Collectors.toList());
        Collections.reverse(valueSizes);
        return HAND_ORDER.indexOf(valueSizes);
    }
}

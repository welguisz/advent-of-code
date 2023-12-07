package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
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
        PriorityQueue<Pair<String, Integer>> camelCards = new PriorityQueue<>(1000, (a,b) -> compareCards(a.getLeft(),b.getLeft(), part2));
        for(String l : lines) {
            String s[] = l.split(" ");
            camelCards.add(Pair.of(s[0], Integer.parseInt(s[1])));
        }
        Long total = 0L;
        int i = 1;
        while (!camelCards.isEmpty()) {
            total += i * camelCards.poll().getRight();
            i++;
        }
        return total;
    }

    public int compareCards(String a, String b, boolean part2) {
        List<Integer> aValue = parseCards(a, part2);
        List<Integer> bValue = parseCards(b, part2);
        for (int i = 0; i < 6; i++) {
            if (aValue.get(i) > bValue.get(i)) {
                return 1;
            }
            if (aValue.get(i) < bValue.get(i)) {
                return -1;
            }
        }
        return 0;
    }

    List<Integer> parseCards(final String a, boolean part2) {
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
        List<Integer> retValues = new ArrayList<>();
        retValues.add(maxHand);
        for(char b : a.toCharArray()) {
            retValues.add(rank.indexOf(b));
        }
        return retValues;
    }

    Integer scoringHands(String a) {
        Map<Character, Integer> values = new HashMap<>();
        for (char b : a.toCharArray()) {
            Integer value = values.getOrDefault(b, 0);
            value ++;
            values.put(b,value);
        }
        switch (values.size()) {
            case 1: //5 of a kind
                return 100;
            case 2: //4 of a kind or full house
                if (values.values().contains(4)) {
                    return 90;
                } else {
                    return 80;
                }
            case 3: //Could be 3 of a kind or two pair
                if (values.values().contains(3)) {
                    return 70;
                } else {
                    return 60;
                }
            case 4: // Can only be a pair
                return 50;
            default:
                return 40;
        }
    }
}

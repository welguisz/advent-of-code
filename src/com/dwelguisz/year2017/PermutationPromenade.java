package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Array;
import java.util.*;

public class PermutationPromenade extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2017/day16/input.txt");
        String[] commands = lines.get(0).split(",");
        String part1 = solutionPart1(16,commands);
        String part2 = solutionPart2(16,commands,1000000000L);
        System.out.println(String.format("Part 1 Answer: %s",part1));
        System.out.println(String.format("Part 2 Answer: %s",part2));

    }

    public String solutionPart1(int length, String[] commands) {
        Map<Integer, Character> intCharMap = new HashMap<>();
        Map<Character, Integer> charIntMap = new HashMap<>();
        for (int i = 0; i < length; i++) {
            intCharMap.put(i, (char) ((char) i + 97));
            charIntMap.put((char) ((char) i + 97), i);
        }
        for (String command : commands) {
            Pair<Map<Integer, Character>, Map<Character, Integer>> pair = updateMap(command, intCharMap, charIntMap);
            intCharMap = pair.getLeft();
            charIntMap = pair.getRight();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(intCharMap.get(i));
        }
        return sb.toString();
    }

    public String solutionPart2(int length, String[] commands, Long danceCount) {
        Map<Integer, Character> intCharMap = new HashMap<>();
        Map<Character, Integer> charIntMap = new HashMap<>();
        for (int i = 0; i < length; i++) {
            intCharMap.put(i, (char) ((char) i + 97));
            charIntMap.put((char) ((char) i + 97), i);
        }
        List<String> previousValues = new ArrayList();
        boolean repeatNotFound = true;
        String repeatFound = "";
        while (repeatNotFound) {
            for (String command : commands) {
                Pair<Map<Integer, Character>, Map<Character, Integer>> pair = updateMap(command, intCharMap, charIntMap);
                intCharMap = pair.getLeft();
                charIntMap = pair.getRight();
            }
            if (danceCount % 10000 == 0) {
                System.out.println(String.format("Dance moves left: %d", danceCount));
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(intCharMap.get(i));
            }
            if (previousValues.contains(sb.toString())) {
                repeatNotFound = false;
                repeatFound = sb.toString();
            } else {
                previousValues.add(sb.toString());
            }
            danceCount--;
        }
        Integer startIndex = previousValues.indexOf(repeatFound);
        Integer loopValue = previousValues.size() - startIndex;
        Long beforeLoop = danceCount - startIndex;
        Long remainder = beforeLoop % loopValue;
        return previousValues.get(remainder.intValue());

    }

    public Pair<Map<Integer,Character>, Map<Character, Integer>> updateMap(String command, Map<Integer, Character> map1, Map<Character,Integer> map2) {
        String tmp = command.substring(0,1);
        if (tmp.equals("s")) {
            int length = map1.size();
            int rotate = Integer.parseInt(command.substring(1));
            rotate %= length;
            Map<Integer, Character> newMap1 = new HashMap<>();
            Map<Character, Integer> newMap2 = new HashMap<>();
            for (int i = 0; i < length; i++) {
                Character val = map1.get(i);
                int newLocation = i + rotate;
                newLocation %= length;
                newMap1.put(newLocation, val);
                newMap2.put(val, newLocation);
            }
            return Pair.of(newMap1, newMap2);
        } else if (tmp.equals("x")) {
            Integer slashPos = command.indexOf("/");
            String m1 = command.substring(1,slashPos);
            Integer posA = Integer.parseInt(m1);
            Integer posB = Integer.parseInt(command.substring(slashPos+1));
            Character valA = map1.get(posA);
            Character valB = map1.get(posB);
            map1.put(posA, valB);
            map1.put(posB, valA);
            map2.put(valB, posA);
            map2.put(valA, posB);
            return Pair.of(map1, map2);
        } else {
            //Integer slashPos = command.indexOf("/");
            Character valA = command.substring(1,2).toCharArray()[0];
            Character valB = command.substring(3,4).toCharArray()[0];
            Integer posA = map2.get(valA);
            Integer posB = map2.get(valB);
            map1.put(posA, valB);
            map1.put(posB, valA);
            map2.put(valB, posA);
            map2.put(valA, posB);
            return Pair.of(map1, map2);
        }
    }
}

package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class KnotHash extends AoCDay {
    public static String input = "106,118,236,1,130,0,235,254,59,205,2,87,129,25,255,118";
    public static String testcase = "3,4,1,5";
    public static String testcase2 = "1,2,4";
    public static Integer HASH_LENGTH = 256;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2017,10,false,0);
        Map<Integer, Integer> maps = new HashMap<>();
        for (int i = 0; i < HASH_LENGTH; i++) {
            maps.put(i,i);
        }
        List<Integer> commands = Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(maps, commands);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines.get(0));
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(Map<Integer, Integer> map, List<Integer> commands) {
        int currentPosition = 0;
        int skipSize = 0;
        Map<Integer, Integer> tempMap = map;
        for (Integer command : commands) {
            List<Integer> values = getValues(tempMap, currentPosition, command);
            tempMap = updateMap(tempMap, currentPosition, command, values);
            currentPosition += command + skipSize;
            while (currentPosition >= map.size()) {
                currentPosition -= map.size();
            }
            skipSize++;
        }
        return tempMap.get(0) * tempMap.get(1);
    }

    public String solutionPart2(String inputStr) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < HASH_LENGTH; i++) {
            map.put(i,i);
        }
        StringBuffer sb = new StringBuffer();
        char[] values = inputStr.toCharArray();
        List<Integer> inputList = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            inputList.add((int)values[i]);
        }
        inputList.addAll(List.of(17,31,73,47,23));

        int currentPosition = 0;
        int skipSize = 0;
        Map<Integer, Integer> tempMap = map;
        for (int i = 0; i < 64; i++) {
            for (Integer command : inputList) {
                List<Integer> valuesList = getValues(tempMap, currentPosition, command);
                tempMap = updateMap(tempMap, currentPosition, command, valuesList);
                currentPosition += command + skipSize;
                while (currentPosition >= map.size()) {
                    currentPosition -= map.size();
                }
                skipSize++;
            }
        }
        for(int i = 0; i < 16; i++) {
            List<Integer> sixteenDigits = new ArrayList<>();
            for (int j = 0; j < 16; j++) {
                sixteenDigits.add(tempMap.get(i*16+j));
            }
            sb.append(convertSparseToDense(sixteenDigits));
        }

        return sb.toString().toLowerCase();
    }

    public String convertSparseToDense(List<Integer> sixteenDigits) {
        Integer value = 0;
        for (Integer val : sixteenDigits) {
            value ^= val;
        }
        return String.format("%02X",value);
    }

    private List<Integer> getValues(Map<Integer, Integer> map, Integer start, Integer length) {
        List<Integer> values = new ArrayList<>();
        Integer pos = start;
        for (int i = 0; i < length; i++) {
            values.add(map.get(pos));
            pos++;
            if (pos == map.size()) {
                pos -= map.size();
            }
        }
        Collections.reverse(values);
        return values;
    }

    private Map<Integer, Integer> updateMap(Map<Integer, Integer> map, Integer start, Integer length, List<Integer> values) {
        Map<Integer, Integer> newMap = map;
        Integer pos = start;
        for (int i = 0; i < length; i++) {
            map.put(pos,values.get(i));
            pos++;
            if (pos == map.size()) {
                pos -= map.size();
            }
        }
        return map;
    }

}

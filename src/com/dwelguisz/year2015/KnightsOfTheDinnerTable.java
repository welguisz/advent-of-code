package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import com.google.common.collect.Collections2;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class KnightsOfTheDinnerTable extends AoCDay {


    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2015,13,false,0);
        Map<Pair<String,String>,Integer> dinnerTable = createTable(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(dinnerTable);
        timeMarkers[2] = Instant.now().toEpochMilli();
        dinnerTable = addMe(dinnerTable);
        part2Answer = solutionPart1(dinnerTable);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Map<Pair<String, String>, Integer> addMe(Map<Pair<String, String>, Integer> dinnerTable) {
        String myName = "Steven";
        Set<String> people = dinnerTable.keySet().stream().map(key -> key.getLeft()).collect(Collectors.toSet());
        for (String neighbor : people) {
            dinnerTable.put(Pair.of(neighbor,myName),0);
            dinnerTable.put(Pair.of(myName,neighbor),0);
        }
        people.add(myName);
        return dinnerTable;
    }
    public Map<Pair<String,String>, Integer> createTable(List<String> lines) {
        Map<Pair<String, String>, Integer> dinnerTable = new HashMap<>();
        for (String fullline:lines) {
            String line = fullline.substring(0,fullline.length()-1);
            String[] points = line.split(" ");
            Integer multiplier = points[2].equals("gain") ? 1 : -1;
            Integer happiness = parseInt(points[3]) * multiplier;
            dinnerTable.put(Pair.of(points[0],points[10]), happiness);
        }
        return dinnerTable;
    }

    public Integer solutionPart1(Map<Pair<String,String>, Integer> dinnerTable) {
        int maxHappiness = Integer.MIN_VALUE;
        Set<String> people = dinnerTable.keySet().stream().map(key -> key.getLeft()).collect(Collectors.toSet());
        for (List<String> perm : Collections2.permutations(people)) {
            int happiness = 0;
            for (int i = 0; i < perm.size(); i++) {
                int nextNum = (i + 1 == perm.size()) ? 0 : i + 1;
                happiness += dinnerTable.get(Pair.of(perm.get(i),perm.get(nextNum)));
                happiness += dinnerTable.get(Pair.of(perm.get(nextNum),perm.get(i)));
            }
            if (happiness > maxHappiness) {
                maxHappiness = happiness;
            }
        }
        return maxHappiness;
    }
}

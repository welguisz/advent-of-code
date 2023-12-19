package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APlenty extends AoCDay {


    Map<String, String> RULES;
    List<List<Integer>> parts;

    public class AcceptedRules {
        final List<Coord2D> values;

        public AcceptedRules(final List<Coord2D> values) {
            this.values = values;
        }

        public boolean allowedPart(List<Integer> part) {
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i).x > part.get(i) || values.get(i).y < part.get(i)) {
                    return false;
                }
            }
            return true;
        }

        public Long score() {
            return values.stream().mapToLong(c -> c.y-c.x+1).reduce(1L, (a,b) -> a*b);
        }
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 19, false, 0);
        parseLines(lines);
        List<AcceptedRules> acceptedRules = createBins();
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(acceptedRules);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(acceptedRules);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    void parseLines(List<String> lines) {
        int part = 0;
        parts = new ArrayList<>();
        RULES = new HashMap<>();
        for (String line : lines) {
            if (line.length() == 0) {
                part++;
                continue;
            }
            if (part == 0) {
                Integer indexOpen = line.indexOf('{');
                String key = line.substring(0,indexOpen);
                String restOfLine = line.substring(indexOpen+1,line.length()-1);
                RULES.put(key, restOfLine);
            } else {
                String newLine = line.substring(1,line.length()-1);
                String[] split = newLine.split(",");
                parts.add(List.of(
                        Integer.parseInt(split[0].split("=")[1]),
                        Integer.parseInt(split[1].split("=")[1]),
                        Integer.parseInt(split[2].split("=")[1]),
                        Integer.parseInt(split[3].split("=")[1])
                        ));
            }
        }
    }

    List<AcceptedRules> createBins() {
        Pair<String,List<Coord2D>> value = Pair.of("in", new ArrayList<>(
                List.of(new Coord2D(1,4000), new Coord2D(1, 4000), new Coord2D(1, 4000), new Coord2D(1, 4000)))
        );
        ArrayDeque<Pair<String,List<Coord2D>>> queue = new ArrayDeque<>(4000);
        queue.add(value);
        List<AcceptedRules> acceptedRules = new ArrayList<>();
        while (!queue.isEmpty()) {
            Pair<String,List<Coord2D>> v = queue.pop();
            List<Coord2D> vL = v.getRight();
            if (vL.stream().anyMatch(c -> c.x > c.y)) {
                continue;
            }
            if (v.getLeft().equals("A")) {
                acceptedRules.add(new AcceptedRules(vL));
                //total += vL.stream().mapToLong(c -> c.y-c.x+1).reduce(1L, (a,b) -> a*b);
            } else if (v.getLeft().equals("R")) {
                //do nothing
            } else {
                String rule = RULES.get(v.getLeft());
                for (String cmd : rule.split(",")) {
                    String res = new String(cmd.toCharArray());
                    if (cmd.contains(":")) {
                        String[] mI = cmd.split(":");
                        String var = mI[0].substring(0,1);
                        String op = mI[0].substring(1,2);
                        res = mI[1];
                        Integer n = Integer.parseInt(mI[0].substring(2));
                        queue.push(Pair.of(res, new ArrayList<>(newRanges(var, op, n, new ArrayList<>(vL), false))));
                        vL = new ArrayList<>(newRanges(var, op, n, new ArrayList<>(vL), true));
                    } else {
                        queue.push(Pair.of(res, new ArrayList<>(vL)));
                        break;
                    }
                }
            }
        }
        return acceptedRules;
    }
    Long solutionPart1(List<AcceptedRules> acceptedRules) {
        return parts.stream()
                .filter(p -> acceptedRules.stream().anyMatch(a -> a.allowedPart(p)))
                .mapToLong(p -> p.stream().reduce(0, (a, b) -> a+b)).sum();
    }

    Long solutionPart2(List<AcceptedRules> acceptedRules) {
        return acceptedRules.stream().mapToLong(a -> a.score()).sum();
    }

    Coord2D newRange(String op, Integer value, Integer low, Integer high, boolean negativeLogic) {
        if (op.equals(">") && !negativeLogic) {
            low = Integer.max(low,value+1);
        } else if (op.equals("<") && !negativeLogic) {
            high = Integer.min(high, value-1);
        } else if (op.equals("<") && negativeLogic) {
            low = Integer.max(low, value);
        } else if (op.equals(">") && negativeLogic) {
            high = Integer.min(high, value);
        }
        return new Coord2D(low, high);
    }

    List<Coord2D> newRanges(String var, String op, Integer value, List<Coord2D> ranges, boolean negativeLogic) {
        List<String> options = List.of("x","m","a","s");
        Integer index = options.indexOf(var);
        ranges.set(index, newRange(op, value, ranges.get(index).x, ranges.get(index).y, negativeLogic));
        return ranges;
    }

}

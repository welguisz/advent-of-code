package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.google.common.collect.Collections2;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class KeypadConundrum extends AoCDay {

    static Map<Character, Coord2D> numericKeypad = new HashMap<>();
    static {
        numericKeypad.put('7', new Coord2D(0, 0));
        numericKeypad.put('8', new Coord2D(0, 1));
        numericKeypad.put('9', new Coord2D(0, 2));
        numericKeypad.put('4', new Coord2D(1, 0));
        numericKeypad.put('5', new Coord2D(1, 1));
        numericKeypad.put('6', new Coord2D(1, 2));
        numericKeypad.put('1', new Coord2D(2, 0));
        numericKeypad.put('2', new Coord2D(2, 1));
        numericKeypad.put('3', new Coord2D(2, 2));
        numericKeypad.put('0', new Coord2D(3, 1));
        numericKeypad.put('A', new Coord2D(3, 2));
    }

    static Map<Character, Coord2D> directionalKeypad = new HashMap<>();
    static {
        directionalKeypad.put('^', new Coord2D(0, 1));
        directionalKeypad.put('A', new Coord2D(0, 2));
        directionalKeypad.put('<', new Coord2D(1, 0));
        directionalKeypad.put('v', new Coord2D(1, 1));
        directionalKeypad.put('>', new Coord2D(1, 2));
    }

    Map<Character, Coord2D> DIRECTIONS = Map.of(
            '^', new Coord2D(-1,0),
            'v', new Coord2D(1, 0),
            '<', new Coord2D(0, -1),
            '>', new Coord2D(0, 1)
    );

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 21, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines, 2);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart1(lines, 25);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Collection<List<Character>> findPermutationOfMoves(Coord2D startingPoint, Coord2D endingPoint) {
        Coord2D diff = endingPoint.add(startingPoint.multiply(-1));
        char horizontalPress = diff.y < 0 ? '<' : '>';
        char verticalPress = diff.x < 0 ? '^' : 'v';
        int length = Math.abs(diff.y);
        List<Character> path = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            path.add(horizontalPress);
        }
        length = Math.abs(diff.x);
        for (int i = 0; i < length; i++) {
            path.add(verticalPress);
        }
        return Collections2.permutations(path);
    }

    List<List<Character>> path(Coord2D startingPoint, Coord2D endingPoint, Coord2D avoid){
        Collection<List<Character>> permutations = findPermutationOfMoves(startingPoint, endingPoint);
        Set<List<Character>> items = new HashSet<>(permutations);
        List<List<Character>> validPath = new ArrayList<>();
        for(List<Character> item : items) {
            if (item.isEmpty()) {
                continue;
            }
            List<Coord2D> checkPath = new ArrayList<>();
            checkPath.add(startingPoint);
            boolean valid = true;
            for (Character c : item) {
                Coord2D nextPos = checkPath.get(checkPath.size() - 1).add(DIRECTIONS.get(c));
                if(nextPos.equals(avoid)) {
                    valid = false;
                    break;
                }
                checkPath.add(nextPos);
            }
            if(valid) {
                List<Character> buffer = new ArrayList<>(item);
                buffer.add('A');
                validPath.add(buffer);
            }
        }
        if (validPath.isEmpty()) {
            return List.of(List.of('A'));
        }
        return validPath;
    }

    @Value
    class MemoKey {
        List<Character> sequence;
        Integer depth;
        Integer limit;
    }

    Map<MemoKey, Long> robotLevelDP = new HashMap<>();

    Long calculateLength(int robotLevel, int maxRobotLevel, List<Character> input) {
        MemoKey key = new MemoKey(input, robotLevel, maxRobotLevel);
        if (robotLevelDP.containsKey(key)) {
            return robotLevelDP.get(key);
        }
        Coord2D current = (robotLevel == 0) ? numericKeypad.get('A') : directionalKeypad.get('A');
        Coord2D avoid = (robotLevel == 0) ? new Coord2D(3, 0) : new Coord2D(0,0);
        long totalLength = 0;

        for (char c : input) {
            Coord2D nextPos = (robotLevel == 0) ? numericKeypad.get(c) : directionalKeypad.get(c);
            List<List<Character>> moves = path(current, nextPos, avoid);
            if (robotLevel >= maxRobotLevel) {
                totalLength += moves.stream().mapToLong(List::size).min().getAsLong();
            } else {
                Long minMoves = Long.MAX_VALUE;
                for (List<Character> m : moves) {
                    Long len = calculateLength(robotLevel + 1, maxRobotLevel, m);
                    if (len < minMoves) {
                        minMoves = len;
                    }
                }
                totalLength += (minMoves == Long.MAX_VALUE) ? moves.stream().mapToLong(List::size).min().getAsLong() : minMoves;
            }
            current = nextPos;
        }
        robotLevelDP.put(key, totalLength);
        return totalLength;
    }

    long solutionPart1(List<String> lines, int numberOfDirectionalRobots) {
        return lines.stream()
                .map(line -> Long.parseLong(line.split("A")[0]) * calculateLength(0, numberOfDirectionalRobots, Arrays.stream(line.split("")).map(s -> s.charAt(0)).toList()))
                .reduce(0L, Long::sum);
    }
}

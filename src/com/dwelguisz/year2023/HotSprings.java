package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord3D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HotSprings extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 12, false, 0);
        List<Pair<String,List<Integer>>> springs = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(springs);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(springs);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<Pair<String,List<Integer>>> parseLines(List<String> lines) {
        return lines.stream()
                .map(l -> {String split[] = l.split(" ");
                        return Pair.of(split[0], Arrays.stream(split[1].split(",")).map(Integer::parseInt).collect(Collectors.toList()));
                })
                .collect(Collectors.toList());
    }

    Map<Coord3D, Long> map;
    Long calculatePossibilities(String spring, List<Integer> brokenGroups) {
        map = new HashMap<>();
        return score(spring, brokenGroups, 0,0,0);
    }

    Long score(String springs, List<Integer> brokenSpringSizes, Integer currentSpringNumber, Integer brokenGroupNumber, Integer currentSizeOfBrokenGroup) {
        Coord3D key = new Coord3D(currentSpringNumber, brokenGroupNumber, currentSizeOfBrokenGroup);
        //We have already calculated this, so just get the value.
        if (map.containsKey(key)) {
            return map.get(key);
        }
        //Reached the end of the springs, make final decisions
        if (currentSpringNumber == springs.length()) {
            //Already reached the total number of brokenSpring groups and current length is 0 (good condition)
            if (brokenGroupNumber == brokenSpringSizes.size() && currentSizeOfBrokenGroup == 0) {return 1L;}
            //In the middle of brokenSprings and have reached the end of the group
            else if (brokenGroupNumber == brokenSpringSizes.size()-1 && brokenSpringSizes.get(brokenGroupNumber)==currentSizeOfBrokenGroup) {return 1L;}
            //Not possible to be part of the group, so return 0
            else {
                return 0L;
            }
        }
        char[] chrs = new char[]{'.','#'};
        Long currentValue = 0L;
        //Need to check both possibilties for a broken spring or a good spring
        for (char chr : chrs) {
            //The character we are currently at is ? or the char we are interested in
            if ((springs.charAt(currentSpringNumber) == chr )|| (springs.charAt(currentSpringNumber) == '?')) {
                //We know we are in good spring and the previous spring was good too, so just inc our current location
                if (chr == '.' && currentSizeOfBrokenGroup == 0) { currentValue += score(springs, brokenSpringSizes, currentSpringNumber + 1, brokenGroupNumber, 0); }
                //We just end a string of either ? or # and we have not reached the end of the brokenGroup and the current
                //size of broken/unknown has reached the current timit
                else if (chr == '.' && currentSizeOfBrokenGroup > 0 && brokenGroupNumber < brokenSpringSizes.size() && brokenSpringSizes.get(brokenGroupNumber) == currentSizeOfBrokenGroup) {
                    currentValue += score(springs, brokenSpringSizes, currentSpringNumber + 1, brokenGroupNumber + 1, 0);
                }
                //Definitely broken, inc our current location, keep the same brokenGroup set, and inc the currentSize group
                else if (chr == '#') {
                    currentValue += score(springs, brokenSpringSizes, currentSpringNumber + 1, brokenGroupNumber, currentSizeOfBrokenGroup + 1);
                }
            }
        }
        //Update the map and return the value so that previous steps can update their currentValue
        map.put(key, currentValue);
        return currentValue;
    }

    Long solutionPart1(List<Pair<String,List<Integer>>> springs) {
        return springs.stream().mapToLong(l -> calculatePossibilities(l.getLeft(), l.getRight())).sum();
    }

    Long solutionPart2(List<Pair<String,List<Integer>>> springs) {
        List<Pair<String,List<Integer>>> newSprings =
                springs.stream().map(p -> Pair.of(
                        IntStream.range(0,5).boxed().map(i -> p.getLeft()).collect(Collectors.joining("?")),
                        IntStream.range(0,5).boxed().flatMap(i -> p.getRight().stream()).collect(Collectors.toList())))
                .collect(Collectors.toList());
        return newSprings.stream().mapToLong(l -> calculatePossibilities(l.getLeft(), l.getRight())).sum();
    }

}

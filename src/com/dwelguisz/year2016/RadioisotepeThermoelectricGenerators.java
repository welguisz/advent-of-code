package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.utilities.Coord3D;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RadioisotepeThermoelectricGenerators extends AoCDay {

    @Value
    public class Floor {
        Set<String> microchips;
        Set<String> generators;

        public int size() {
            return microchips.size() + generators.size();
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public void addMicrochips(Collection<String> microchips) {
            this.microchips.addAll(microchips);
        }

        public void removeMicrochips(Collection<String> microchips) {
            this.microchips.removeAll(microchips);
        }

        public void removeGenerators(Collection<String> generators) {
            this.generators.removeAll(generators);
        }

        public void addGenerators(Collection<String> generators) {
            this.generators.addAll(generators);
        }

        public boolean isValid() {
            if (generators.isEmpty() || microchips.isEmpty()) {
                return true;
            }
            for (String microchip : microchips) {
               if (!generators.contains(microchip)) {
                   return false;
               }
            }
            return true;
        }
    }

    @Value
    public class State {
        List<Floor> floors;
        Integer current_floor;
        Integer max_floor;
        @EqualsAndHashCode.Exclude int stepNumber;

        List<Integer> nextFloors() {
            List<Integer> nextFloors = new ArrayList<>();
            int lower_floor = current_floor-1;
            if (lower_floor >= 0) {
                nextFloors.add(lower_floor);
            }
            int higher_floor = current_floor+1;
            if (higher_floor < max_floor) {
                nextFloors.add(higher_floor);
            }
            return nextFloors;
        }


        public boolean isValid() {
            return floors.stream().allMatch(Floor::isValid);
        }

        public boolean isDone() {
            for (int i = 0; i < floors.size()-1; i++) {
                if (!floors.get(i).isEmpty()) {
                    return false;
                }
            }
            return current_floor == max_floor-1;
        }

        private List<Pair<List<String>, List<String>>> createCombinations(List<String> microchips, List<String> generators) {
            List<Pair<List<String>, List<String>>> combinations = new ArrayList<>();
            //One microchip
            for (String microchip : microchips) {
                combinations.add(Pair.of(List.of(microchip), new ArrayList<>()));
            }
            //One generator
            for (String generator : generators) {
                combinations.add(Pair.of(new ArrayList<>(), List.of(generator)));
            }
            //Two microchips
            List<List<String>> twoMicrochips = combinations(microchips, 2);
            for(List<String> twoMicrochip : twoMicrochips) {
                combinations.add(Pair.of(twoMicrochip, new ArrayList<>()));
            }
            //Two generators
            List<List<String>> twoGenerators = combinations(generators, 2);
            for (List<String> twoGenerator : twoGenerators) {
                combinations.add(Pair.of(new ArrayList<>(), twoGenerator));
            }
            //One microchip and one generator
            for (String microchip : microchips) {
                if (generators.contains(microchip)) {
                    combinations.add(Pair.of(List.of(microchip), List.of(microchip)));
                }
            }
            return combinations;
        }

        public List<List<String>> combinations(List<String> inputSet, int k) {
            List<List<String>> results = new ArrayList<>();
            combinationsInternal(inputSet, k, results, new ArrayList<>(), 0);
            return results;
        }

        public void combinationsInternal(List<String> inputSet, int k, List<List<String>> results, ArrayList<String> accumulator, int index) {
            int needToAccumulate = k - accumulator.size();
            int canAccumulate = inputSet.size() - index;
            if (accumulator.size() == k) {
                results.add(new ArrayList<>(accumulator));
            } else if (needToAccumulate <= canAccumulate) {
                combinationsInternal(inputSet, k, results, accumulator, index + 1);
                accumulator.add(inputSet.get(index));
                combinationsInternal(inputSet,k,results,accumulator,index+1);
                accumulator.remove(accumulator.size()-1);
            }
        }

        public Stream<State> nextState(Set<Pair<List<Coord2D>, Integer>> seenShapes) {
            List<State> nextStates = new ArrayList<>();
            List<Integer> nextFloors = nextFloors();
            Floor currentFloor = floors.get(current_floor);
            List<String> currentMicrochips = new ArrayList<>(currentFloor.microchips);
            List<String> currentGenerators = new ArrayList<>(currentFloor.generators);
            //getLeft -> microchips, getRight -> generators
            List<Pair<List<String>, List<String>>> possibleCombinations = createCombinations(currentMicrochips, currentGenerators);
            for (Integer nextFloor : nextFloors) {
                for (Pair<List<String>, List<String>> combination : possibleCombinations) {
                    List<Floor> newFloors = new ArrayList<>();
                    for (int i = 0; i < max_floor; i++) {
                        if (i == current_floor) {
                            Floor tempFloor = new Floor(new HashSet<>(currentMicrochips), new HashSet<>(currentGenerators));
                            tempFloor.removeMicrochips(combination.getLeft());
                            tempFloor.removeGenerators(combination.getRight());
                            newFloors.add(tempFloor);
                        } else if (i == nextFloor) {
                            Floor tempFloor = new Floor(new HashSet<>(floors.get(i).getMicrochips()), new HashSet<>(floors.get(i).getGenerators()));
                            tempFloor.addMicrochips(combination.getLeft());
                            tempFloor.addGenerators(combination.getRight());
                            newFloors.add(tempFloor);
                        } else {
                            newFloors.add(new Floor(new HashSet<>(floors.get(i).getMicrochips()), new HashSet<>(floors.get(i).getGenerators())));
                        }
                    }
                    State tmp = new State(newFloors, nextFloor, max_floor, stepNumber + 1);
                    if (tmp.isValid() && !seenShapes.contains(tmp.getShape())) {
                        nextStates.add(tmp);
                    }
                }
            }
            return nextStates.stream();
        }

        public Pair<List<Coord2D>, Integer> getShape() {
            List<Coord2D> shape = new ArrayList<>();
            for (Floor floor : floors) {
                shape.add(new Coord2D(floor.getMicrochips().size(), floor.getGenerators().size()));
            }
            return Pair.of(shape, current_floor);
        }
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,11,false,0);
        State initialState = parseLines(lines, new ArrayList<>());
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(initialState);
        timeMarkers[2] = Instant.now().toEpochMilli();
        initialState = parseLines(lines, List.of("elerium", "dilithium"));
        part2Answer = solutionPart1(initialState);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    State parseLines(List<String> lines, List<String> items) {
        List<Floor> floors = new ArrayList<>();
        int floorNumber = 0;
        for (String line : lines) {
            Set<String> generators = (floorNumber == 0) ? new HashSet<>(items) : new HashSet<>();
            Set<String> microchips = (floorNumber == 0) ? new HashSet<>(items) : new HashSet<>();
            if (line.contains("generator")) {
                Pattern pattern = Pattern.compile("a (?<element>\\w+) generator");
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    generators.add(matcher.group("element"));
                }
            }
            if (line.contains("microchip")) {
                Pattern pattern = Pattern.compile("a (?<element>\\w+)-compatible microchip");
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    microchips.add(matcher.group("element"));
                }
            }
            floors.add(new Floor(microchips, generators));
            floorNumber++;
        }
        return new State(floors, 0, 4, 0);
    }

    public Integer solutionPart1(State initialState) {
        PriorityQueue<State> queue = new PriorityQueue<>(5000, Comparator.comparingInt(State::getStepNumber));
        queue.add(initialState);
        Set<Pair<List<Coord2D>, Integer>> seenShapes = new HashSet<>();
        int counter = 0;
        while (counter < 1000000) {
            State state = queue.poll();
            if (seenShapes.contains(state.getShape())) {
                continue;
            }
            if (state.isDone()) {
                return state.getStepNumber();
            }
            seenShapes.add(state.getShape());
            queue.addAll(state.nextState(seenShapes).filter(s -> !queue.contains(s)).collect(Collectors.toSet()));
            counter++;
        }
        return -1;
    }
}

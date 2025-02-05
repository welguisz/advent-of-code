package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OxygenSystem extends AoCDay {

    public static Coord2D NORTH = new Coord2D(-1,0);
    public static Coord2D SOUTH = new Coord2D(1,0);
    public static Coord2D WEST = new Coord2D(0,-1);
    public static Coord2D EAST = new Coord2D(0, 1);

    public static Map<Coord2D, Long> MOVEMENTS_INSTR = Map.of(NORTH,1L,SOUTH,2L,WEST,3L,EAST,4L);
    public static Map<Coord2D, Coord2D> REVERSE = Map.of(NORTH,SOUTH,SOUTH,NORTH,WEST,EAST,EAST,WEST);

    public static class RepairDroid extends IntCodeComputer {


        Coord2D currentLocation;
        Map<Coord2D, String> map;
        boolean stop;
        Stack<List<Coord2D>> previousSteps;
        Stack<Coord2D> currentSteps;
        Integer stepsToOxygen;
        Coord2D oxygenLocation;
        public RepairDroid() {
            super();
            currentLocation = new Coord2D(0,0);
            map = new HashMap<>();
            map.put(new Coord2D(0,0),".");
            oxygenLocation = new Coord2D(0,0);
            stop = false;
            previousSteps = new Stack<>();
            currentSteps = new Stack<>();
            List<Coord2D> todo = new ArrayList<>();
            todo.addAll(List.of(NORTH,SOUTH,WEST,EAST));
            previousSteps.push(todo);
            stepsToOxygen = 0;
        }


        @Override
        public Pair<Boolean, Long> getInputValue() {
            List<Coord2D> stepsToCheck = previousSteps.pop();
            if (stepsToCheck.isEmpty()) {
                if (currentSteps.isEmpty()) {
                    stop = true;
                    return Pair.of(false, 0L);
                }
                Coord2D stepToLook = currentSteps.pop();
                Coord2D reverseStep = REVERSE.get(stepToLook);
                currentLocation = currentLocation.add(reverseStep);
                return Pair.of(true, MOVEMENTS_INSTR.get(reverseStep));
            }
            Coord2D step = stepsToCheck.remove(0);
            Coord2D proposedLocation = currentLocation.add(step);
            while (map.containsKey(proposedLocation)) {
                if (stepsToCheck.isEmpty()) { //Loop, move back one step
                    if (currentSteps.isEmpty()) {
                        stop = true;
                        return Pair.of(false, 0L);
                    }
                    Coord2D stepToLook = currentSteps.pop();
                    Coord2D reverseStep = REVERSE.get(stepToLook);
                    currentLocation = currentLocation.add(reverseStep);
                    return Pair.of(true,MOVEMENTS_INSTR.get(reverseStep));
                }
                step = stepsToCheck.remove(0);
                proposedLocation = currentLocation.add(step);
            }
            currentLocation = proposedLocation;
            currentSteps.push(step);
            List<Coord2D> todo = new ArrayList<>();
            todo.addAll(List.of(NORTH,SOUTH,WEST,EAST));
            previousSteps.push(stepsToCheck);
            previousSteps.push(todo);
            return Pair.of(true, MOVEMENTS_INSTR.get(step));
        }

        @Override
        public void addOutputValue(Long outputValue) {
            if (outputValue == 0L) {
                map.put(currentLocation, "#");
                currentLocation = currentLocation.add(REVERSE.get(currentSteps.pop()));
                previousSteps.pop();
            } else if (outputValue == 1L) {
                map.put(currentLocation, ".");
            } else if (outputValue == 2L) {
                map.put(currentLocation, "O");
                stepsToOxygen = currentSteps.size();
                oxygenLocation = new Coord2D(currentLocation.x, currentLocation.y);
            }
        }

        @Override
        public void run() {
            Long currentInstructionWithMode = intCode.getOrDefault(instructionPointer,0L);
            Long currentInstruction = currentInstructionWithMode % 100;
            done = false;
            while (!stop && (currentInstruction != 99) && (opCodes.containsKey(currentInstruction))) {
                List<ParameterModes> modes = findModes(currentInstructionWithMode);
                doCalculation(currentInstruction, modes);
                currentInstructionWithMode = intCode.getOrDefault(instructionPointer, 0L);
                currentInstruction = currentInstructionWithMode % 100;
            }
            done = true;
        }

        public void printMap() {
            Integer minY = map.keySet().stream().mapToInt(p -> p.x).min().getAsInt();
            Integer maxY = map.keySet().stream().mapToInt(p -> p.x).max().getAsInt();
            Integer minX = map.keySet().stream().mapToInt(p -> p.y).min().getAsInt();
            Integer maxX = map.keySet().stream().mapToInt(p -> p.y).max().getAsInt();
            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    System.out.print(map.getOrDefault(new Coord2D(y,x)," "));
                }
                System.out.println();
            }
        }

        Stream<Coord2D> nextSpots(Coord2D loc, Set<Coord2D> alreadyFilled) {
            List<Coord2D> possibleLocs = List.of(
                    loc.add(NORTH),
                    loc.add(SOUTH),
                    loc.add(WEST),
                    loc.add(EAST));
            return possibleLocs.stream()
                    .filter(n -> map.getOrDefault(n,"#").equals("."))
                    .filter(n -> !alreadyFilled.contains(n));

        }

        public Integer stepsToFill() {
            Integer steps = 0;
            Set<Coord2D> states = new HashSet<>();
            Set<Coord2D> alreadyFilled = new HashSet<>();
            states.add(oxygenLocation);
            while (!states.isEmpty()) {
                alreadyFilled.addAll(states);
                states = states.stream().flatMap(s -> nextSpots(s,alreadyFilled)).collect(Collectors.toSet());
                steps++;
            }
            return steps-1;
        }

    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,15,false,0);
        RepairDroid droid = new RepairDroid();
        droid.setId(1L);
        droid.initializeIntCode(lines);
        droid.run();
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(droid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(droid);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(RepairDroid droid) {
        return droid.stepsToOxygen;
    }

    public Integer solutionPart2(RepairDroid droid) {
        return droid.stepsToFill();
    }

}

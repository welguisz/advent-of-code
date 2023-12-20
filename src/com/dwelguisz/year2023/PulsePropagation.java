package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.SpecialMath;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PulsePropagation extends AoCDay {

    Map<String, Module> modules;
    List<String> flipFlops;
    List<String> conjunctions;
    List<String> WATCH = new ArrayList<>();
    Map<String, Long> COUNT = new HashMap<>();
    Map<String, Long> PREVIOUS = new HashMap<>();
    List<Long> lcmCatch = new ArrayList<>();

    public class Module {
        Integer type; //0 -> flip-flop; 1 -> conjunction; 2 -> broadcaster
        String name;
        List<String> receiver;
        Map<String, Boolean> currentInputs;
        Long lowPulses;
        Long highPulses;
        Boolean currentValue;


        public Module(String name, List<String> receiver, String choice) {
            this.name = name;
            this.receiver = receiver;
            this.currentValue = false;
            this.currentInputs = new HashMap<>();
            this.lowPulses = 0L;
            this.highPulses = 0L;
            if (name.equals("broadcaster")) {
                type = 2;
            } else if (choice.equals("%")) {
                type = 0;
            } else if (choice.equals("&")) {
                type = 1;
            } else {
                type = 3;
            }
        }

        public void addInput(String name) {
            currentInputs.put(name, false);
        }

        public List<ButtonState> process(String previous, Boolean value, Long count) {
            if (!value) {
                if (WATCH.contains(name) && COUNT.getOrDefault(name, 0L) == 2L) {
                    lcmCatch.add(count - PREVIOUS.get(name));
                }
                PREVIOUS.put(name, count);
                Long va = COUNT.getOrDefault(name, 0L);
                va++;
                COUNT.put(name, va);
            }
            if (value) {
                highPulses++;
            } else {
                lowPulses++;
            }
            if (type == 1) {
                currentInputs.put(previous,value);
                Boolean pulse = !currentInputs.values().stream().allMatch(b->b);
                return receiver.stream().map(s->new ButtonState(s,name,pulse)).collect(Collectors.toList());
            } else if (type == 0){
                if (value) {
                    return new ArrayList<>();
                }
                this.currentValue ^= true;
                return receiver.stream().map(s->new ButtonState(s,name,currentValue)).collect(Collectors.toList());
            } else if (type == 2) {
                return receiver.stream().map(s -> new ButtonState(s,name,false)).collect(Collectors.toList());
            }
            return new ArrayList<>();
        }
    }

    public class ButtonState {
        String currentNode;
        String previousNode;
        Boolean value;

        public ButtonState(String currentNode, String previousNode, Boolean value) {
            this.currentNode = currentNode;
            this.previousNode = previousNode;
            this.value = value;
        }
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 20, false, 0);
        parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        parseLines(lines);
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public void parseLines(List<String> lines) {
        modules = new HashMap<>();
        flipFlops = new ArrayList<>();
        conjunctions = new ArrayList<>();
        WATCH = new ArrayList<>();
        for (String line : lines) {
            String split[] = line.split(" -> ");
            List<String> outputs = Arrays.stream(split[1].split(", ")).collect(Collectors.toList());
            String name = split[0];
            if (name.contains("%") || name.contains("&")) {
                if (name.contains("%")) {
                    flipFlops.add(name.substring(1));
                } else {
                    conjunctions.add(name.substring(1));
                }
                name = name.substring(1);
            }
            modules.put(name, new Module(name, outputs, split[0].substring(0,1)));
        }
        for (String line : lines) {
            String split[] = line.split(" -> ");
            List<String> outputs = Arrays.stream(split[1].split(", ")).collect(Collectors.toList());
            String name = split[0];
            if (name.contains("%") || name.contains("&")) {
                name = name.substring(1);
            }
            final String n = name;
            outputs.stream().filter(s -> !modules.containsKey(s)).forEach(s -> modules.put(s, new Module(s, new ArrayList<>(),"-")));
            outputs.stream().forEach(s -> modules.get(s).addInput(n));
        }
        String importantModule = "rx";
        while (!conjunctions.contains(importantModule)) {
            importantModule = modules.get(importantModule).currentInputs.keySet().stream().collect(Collectors.toList()).get(0);
        }
        WATCH = modules.get(importantModule).currentInputs.keySet().stream().collect(Collectors.toList());
    }

    void buttonPressOnce(Long count) {
        ArrayDeque<ButtonState> queue = new ArrayDeque<>(500);
        queue.add(new ButtonState("broadcaster", "button", false));
        while (!queue.isEmpty()) {
            ButtonState current = queue.pollFirst();
            queue.addAll(modules.get(current.currentNode).process(current.previousNode, current.value, count));
        }
    }

    Long solutionPart1() {
        for (int i = 0; i < 1000; i++) {
            buttonPressOnce((long) i);
        }
        Long highPulses = modules.values().stream().mapToLong(m -> m.highPulses).sum();
        Long lowPulses = modules.values().stream().mapToLong(m -> m.lowPulses).sum();
        System.out.println("lowPulses: " + lowPulses + "; highPulses: " + highPulses);
        return modules.values().stream().mapToLong(m -> m.highPulses).sum() * modules.values().stream().mapToLong(m->m.lowPulses).sum();
    }

    Long solutionPart2() {
        Long count = 1L;
        boolean done = false;
        while (!done) {
            buttonPressOnce(count);
            count++;
            if (lcmCatch.size() == WATCH.size()) {
                done = true;
            }
        }
        return lcmCatch.stream().mapToLong(a -> a).reduce(1L, (a,b) -> SpecialMath.lcm(a,b));
    }
}

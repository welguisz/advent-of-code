package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SpaceStoichiometry extends AoCDay {

    public Map<String, Long> productionMultiplicty = new HashMap<>();
    public static class Reactions {

        final List<Pair<String, Long>> inputs;
        final String name;
        final Long amount;
        private int hashCode;
        Long used;
        Long created;

        public Reactions(List<Pair<String, Long>> inputs, String name, Long amount) {
            this.inputs = inputs;
            this.name = name;
            this.amount = amount;
            this.hashCode = Objects.hash(inputs, name, amount);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,14,false,0);
        Map<String, Reactions> reactions = parsedLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(reactions, 1L);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(reactions, (long) part1Answer);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Map<String, Reactions> parsedLines(List<String> lines) {
        Map<String, Reactions> reactions = new HashMap<>();
        for (String l: lines) {
            String[] elements = l.split(" => ");
            List<Pair<String, Long>> inputs = Arrays.stream(elements[0].split(", "))
                    .map(e -> parseElement(e)).collect(Collectors.toList());
            String output[] = elements[1].split(" ");
            String name = output[1];
            Long amount = Long.parseLong(output[0]);
            reactions.put(name, new Reactions(inputs, name, amount));
            productionMultiplicty.put(name,amount);
        }
        return reactions;
    }

    public Pair<String, Long> parseElement(String e) {
        String[] makeUp = e.split(" ");
        Long number = Long.parseLong(makeUp[0]);
        return Pair.of(makeUp[1],number);
    }

    public Long getOreNeeded(Map<String, Reactions> reactions, Map<String, Long> leftovers, Long produceCount, String resource) {
        List<Pair<String, Long>> inputs = reactions.get(resource).inputs;
        Long value = 0L;
        for(Pair<String, Long> input : inputs) {
            if (input.getLeft().equals("ORE")) {
                value += produceCount * input.getRight();
            } else {
                Long quantityNeeded = produceCount * input.getRight() - leftovers.getOrDefault(input.getLeft(),0L);
                Long productMultiplicity = productionMultiplicty.get(input.getLeft());
                Long create = (long) Math.ceil(quantityNeeded / (double) productMultiplicity);
                Long leftover = productMultiplicity * create - quantityNeeded;
                leftovers.compute(input.getLeft(),(key, oldValue) -> leftover == 0 ? null : leftover);
                value += getOreNeeded(reactions, leftovers, create, input.getLeft());
            }
        }
        return value;
    }

    public Long solutionPart1(Map<String, Reactions> reactions, Long amount) {
        return getOreNeeded(reactions, new HashMap<>(), amount, "FUEL");
    }

    public Long solutionPart2(Map<String, Reactions> reactions, Long oreNeededFor1) {
        Long oreAvailable = 1_000_000_000_000L;
        Long fuelCreated = 0L;
        Map<String,Long> leftOvers = new HashMap<>();
        while (oreAvailable >= oreNeededFor1) {
            Long fuelCreatedThisRound = (oreAvailable / (oreNeededFor1 * 2)) + 1;
            Long oreNeededForBigBatch = getOreNeeded(reactions, leftOvers, fuelCreatedThisRound, "FUEL");
            oreAvailable -= oreNeededForBigBatch;
            fuelCreated += fuelCreatedThisRound;
        }
        return fuelCreated;
    }
}

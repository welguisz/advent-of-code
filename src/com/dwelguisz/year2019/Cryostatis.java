package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2019.IntCodeComputer.IntCodeComputer;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Cryostatis extends AoCDay {

    public static class ReindeerDroid extends IntCodeComputer {

        Long finalAnswer;
        Scanner in;
        byte[] program;
        Integer programPointer;
        String santaVoice;

        public ReindeerDroid() {
            super();
            finalAnswer = 0L;
            in = new Scanner(System.in);
            programPointer = 0;
            santaVoice = "";
        }

        public void setFunctions(List<String> instructions) {
            String total = instructions.stream().collect(Collectors.joining("\n")) + "\n";
            program = total.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public Pair<Boolean, Long> getInputValue() {
            if (programPointer < program.length) {
                programPointer++;
                return Pair.of(true, Long.valueOf(program[programPointer-1]));
            }
            return Pair.of(false, 0L);
        }

        @Override
        public void addOutputValue(Long outputValue) {
            if (outputValue > 255L) {
                finalAnswer = outputValue;
            } else {
                Character tmp = (char) outputValue.intValue();
                //System.out.print(tmp);
                santaVoice += tmp;
            }


        }
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,25,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = "Click on link to complete";
        timeMarkers[3] = Instant.now().toEpochMilli();
        Long parseTime = Instant.now().toEpochMilli();
    }

    public Long solutionPart1(List<String> lines) {
        List<String> items = List.of("festive hat", "wreath", "space heater", "coin", "pointer", "dehydrated water", "astrolabe", "prime number");
        List<List<String>> combos = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            combos.addAll(combinations(items,i));
        }
        ReindeerDroid droid = new ReindeerDroid();
        droid.setId(0L);
        droid.initializeIntCode(lines);
        List<String> functions = new ArrayList<>();
        functions.addAll(List.of("south","take festive hat","north","west","south","take pointer","south",
                "take prime number","west","take coin","east","north","north","east","east","south","south",
                "take space heater", "south", "take astrolabe","north","north","north","north","take wreath","north",
                "west","take dehydrated water","north","east","drop festive hat","drop pointer","drop prime number",
                "drop space heater","drop astrolabe", "drop wreath","drop dehydrated water"));
        for(List<String> combo : combos) {
            for(String i : combo) {
                functions.add("take " + i);
            }
            functions.add("inv");
            functions.add("south");
            for (String i : combo) {
                functions.add("drop " + i);
            }
        }
        droid.setFunctions(functions);
        droid.run();
        // Inventory needed to be wreath, space heater, pointer, dehydrated water
        String tmp = droid.santaVoice;
        Pattern pattern = Pattern.compile("You should be able to get in by typing (?<code>\\d+) on the keypad at the main airlock");
        Matcher matcher = pattern.matcher(tmp);
        if (matcher.find()) {
            return Long.parseLong(matcher.group("code"));
        }
        return droid.finalAnswer;
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

}

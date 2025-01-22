package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BalanceBots extends AoCDay {

    public Map<String, Pair<String, String>> map;
    public Map<Integer, String> inputValues;
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,10,false,0);
        createBotNet(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(61,17);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(Integer value1, Integer value2) {
        Map<String, List<Integer>> bots = new HashMap<>();
        for (Map.Entry<Integer, String> inputValue : inputValues.entrySet()) {
            List<Integer> vals = bots.getOrDefault(inputValue.getValue(), new ArrayList<>());
            vals.add(inputValue.getKey());
            bots.put(inputValue.getValue(), vals);
        }
        while (true) {
            Map<String, List<Integer>> tmp = new HashMap<>();
            for (Map.Entry<String, List<Integer>> bot : bots.entrySet()) {
                if (bot.getValue().size() > 1) {
                    if (bot.getValue().contains(value1) && bot.getValue().contains(value2)) {
                        String botSplit[] = bot.getKey().split(" ");
                        return Integer.parseInt(botSplit[1]);
                    }
                    Integer lowValue = bot.getValue().stream().min(Integer::compareTo).get();
                    Integer highValue = bot.getValue().stream().max(Integer::compareTo).get();
                    String lowBot = map.get(bot.getKey()).getLeft();
                    String highBot = map.get(bot.getKey()).getRight();
                    List<Integer> vals = tmp.getOrDefault(lowBot, new ArrayList<>());
                    vals.add(lowValue);
                    tmp.put(lowBot,vals);
                    vals = tmp.getOrDefault(highBot, new ArrayList<>());
                    vals.add(highValue);
                    tmp.put(highBot,vals);
                } else {
                    List<Integer> vals = tmp.getOrDefault(bot.getKey(), new ArrayList<>());
                    vals.add(bot.getValue().get(0));
                    tmp.put(bot.getKey(), vals);
                }
            }
            bots = tmp;
        }

    }

    public Integer solutionPart2() {
        Map<String, Set<Integer>> bots = new HashMap<>();
        Map<Integer, Set<Integer>> outputBins = new HashMap<>();
        for (Map.Entry<Integer, String> inputValue : inputValues.entrySet()) {
            Set<Integer> vals = bots.getOrDefault(inputValue.getValue(), new HashSet<>());
            vals.add(inputValue.getKey());
            bots.put(inputValue.getValue(), vals);
        }
        while (true) {
            Map<String, Set<Integer>> tmp = new HashMap<>();
            for (Map.Entry<String, Set<Integer>> bot : bots.entrySet()) {
                if (bot.getValue().size() > 1) {
                    Integer lowValue = bot.getValue().stream().min(Integer::compareTo).get();
                    Integer highValue = bot.getValue().stream().max(Integer::compareTo).get();
                    String lowBot = map.get(bot.getKey()).getLeft();
                    String highBot = map.get(bot.getKey()).getRight();
                    if (lowBot.contains("output")) {
                        String split[] = lowBot.split(" ");
                        Integer binNumber = Integer.parseInt(split[1]);
                        Set<Integer> vals = outputBins.getOrDefault(binNumber, new HashSet<>());
                        vals.add(lowValue);
                        outputBins.put(binNumber, vals);
                    } else {
                        Set<Integer> vals = tmp.getOrDefault(lowBot, new HashSet<>());
                        vals.add(lowValue);
                        tmp.put(lowBot, vals);
                    }
                    if (highBot.contains("output")) {
                        String split[] = highBot.split(" ");
                        Integer binNumber = Integer.parseInt(split[1]);
                        Set<Integer> vals = outputBins.getOrDefault(binNumber, new HashSet<>());
                        vals.add(highValue);
                        outputBins.put(binNumber, vals);
                    } else {
                        Set<Integer> vals = tmp.getOrDefault(highBot, new HashSet<>());
                        vals.add(highValue);
                        tmp.put(highBot, vals);
                    }
                } else {
                    Set<Integer> vals = tmp.getOrDefault(bot.getKey(), new HashSet<>());
                    vals.addAll(bot.getValue());
                    tmp.put(bot.getKey(), vals);
                }
            }
            bots = tmp;
            for (Map.Entry<Integer, String> inputValue : inputValues.entrySet()) {
                Set<Integer> vals = bots.getOrDefault(inputValue.getValue(), new HashSet<>());
                vals.add(inputValue.getKey());
                bots.put(inputValue.getValue(), vals);
            }
            if(outputBins.containsKey(0) && outputBins.containsKey(1) && outputBins.containsKey(2)) {
                List<Integer> vals = new ArrayList<>();
                vals.addAll(outputBins.get(0));
                vals.addAll(outputBins.get(1));
                vals.addAll(outputBins.get(2));
                return vals.stream().reduce(1,(a,b) -> a*b);
            }
        }

    }

    public List<Integer> setToList(Set<Integer> vals) {
        return vals.stream().collect(Collectors.toList());
    }

    public void createBotNet(List<String> lines) {
        map = new HashMap<>();
        inputValues = new HashMap<>();
        for (String line : lines) {
            String split[] = line.split(" ");
            if (split[2].equals("goes")) {
                Integer val = Integer.parseInt(split[1]);
                String bot = split[4] + " " + split[5];
                inputValues.put(val,bot);
            } else {
                String startBot = split[0] + " " + split[1];
                String lowBot = split[5] + " " + split[6];
                String highBot = split[10] + " " + split[11];
                map.put(startBot, Pair.of(lowBot, highBot));
            }
        }
    }
}

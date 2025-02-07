package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.lang.Integer.parseInt;

public class HandyHaversacks extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2020,7,false,0);
        Map<String,Map<String,Integer>> rules = createBagRules(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(rules, "shiny gold").size();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(rules, "shiny gold");
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

   private Set<String> solutionPart1(Map<String,Map<String,Integer>> rules, String importantKey) {
        Set<String> foundBags = new HashSet<>();
        Boolean runAgain = true;
        while(runAgain) {
            Set<String> newBags = new HashSet<>();
            runAgain = false;
            for (Map.Entry<String, Map<String, Integer>> rule : rules.entrySet()) {
                if (foundBags.contains(rule.getKey())) {
                    continue;
                }
                if (rule.getValue().containsKey(importantKey)) {
                    newBags.add(rule.getKey());
                }
            }
            if (!newBags.isEmpty()) {
                runAgain = true;
                for(String bag : newBags) {
                    foundBags.addAll(solutionPart1(rules, bag));
                }
                foundBags.addAll(newBags);
            }
        }
        return foundBags;
    }

    private Long solutionPart2(Map<String,Map<String,Integer>> rules, String importantKey) {
        Map<String, Integer> innerBags = rules.get(importantKey);
        if (innerBags.isEmpty()) {
            return 0L;
        }
        Long innerBagSize = 0L;
        for(Map.Entry<String, Integer> innerBag : innerBags.entrySet()) {
            innerBagSize += innerBag.getValue();
            innerBagSize += innerBag.getValue() * solutionPart2(rules, innerBag.getKey());
        }

        return innerBagSize;
    }

    private Map<String,Map<String,Integer>> createBagRules(List<String> lines) {
        Map<String,Map<String,Integer>> rules = new HashMap<>();
        for (String line : lines) {
            String[] outerBagRules = line.split(" contain ");
            String outerkey = outerBagRules[0].replace(" bags", "");
            outerkey = outerkey.replace("bags ","").replace("bag ","");
            String [] innerBags = outerBagRules[1].split(", ");
            Map<String, Integer> innerBagRules = new HashMap<>();
            for (String innerBag : innerBags) {
                Integer firstSpace = innerBag.indexOf(" ");
                String number = innerBag.substring(0,firstSpace);
                if (number.equals("no")) {
                    continue;
                }
                String bag = innerBag.substring(firstSpace+1);
                bag = bag.replaceAll(" bags", "").replaceAll(" bag","").replaceAll("\\.","");
                innerBagRules.put(bag, parseInt(number));
            }
            rules.put(outerkey, innerBagRules);
        }
        return rules;
    }
}

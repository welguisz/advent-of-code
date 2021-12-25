package com.dwelguisz.year2020;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Integer.parseInt;

public class HandyHaversacks {
    public static void main(String[] args) {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2020/day07/testcase.txt");
        Map<String,Map<String,Integer>> rules = createBagRules(lines);
        Integer part1 = solutionPart1(rules,"shiny gold");
        System.out.println(String.format("Solution Part1: %d",part1));
    }

    public static Integer solutionPart1(Map<String,Map<String,Integer>> rules, String importantKey) {
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
                foundBags.addAll(newBags);
            }
        }
        return foundBags.size();
    }

    public static Map<String,Map<String,Integer>> createBagRules(List<String> lines) {
        Map<String,Map<String,Integer>> rules = new HashMap<>();
        for (String line : lines) {
            String[] outerBagRules = line.split(" contain ");
            String outerkey = outerBagRules[0];
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

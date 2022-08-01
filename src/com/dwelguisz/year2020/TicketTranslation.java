package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicketTranslation extends AoCDay {

    Map<Integer, List<String>> validNumbers;
    Map<String, Map<Integer, Boolean>> sectionMap;
    List<Integer> myTicket;
    List<List<Integer>> otherTickets;
    List<List<Integer>> validTickets;

    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2020/day16/input.txt");
        parseLines(lines);
        Long part1 = solutionPart1();
        Long part2 = solutionPart2();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    private void parseLines(List<String> lines) {
        int section = 0;
        validNumbers = new HashMap<>();
        otherTickets = new ArrayList<>();
        sectionMap = new HashMap<>();
        Map<Integer, Boolean> possibleVals = new HashMap<>();
        List<String> ticketClasses = new ArrayList<>();
        Integer value = 0;
        for (String line : lines) {
            if (line.length() == 0) {
                continue;
            }
            if ("your ticket:".equals(line)) {
                section = 1;
                continue;
            } else if ("nearby tickets:".equals(line)) {
                section = 2;
                continue;
            }
            if (section == 0) {
                String[] diffs = line.split(": ");
                String sectionName = diffs[0];
                ticketClasses.add(sectionName);
                possibleVals.put(value, true);
                value++;
                String[] ranges = diffs[1].split(" or ");
                for (String range: ranges) {
                    List<Integer> rangeNums = Arrays.stream(range.split("-")).map(Integer::parseInt).collect(Collectors.toList());
                    IntStream.range(rangeNums.get(0), rangeNums.get(1)+1).forEach(val -> {
                        List<String> vals = validNumbers.getOrDefault(val, new ArrayList<>());
                        vals.add(sectionName);
                        validNumbers.put(val, vals);
                    });
                }

            } else if (section == 1) {
                myTicket = Arrays.stream(line.split(",")).map(Integer::parseInt).collect(Collectors.toList());
            } else if (section == 2) {
                List<Integer> ticketLine = Arrays.stream(line.split(",")).map(Integer::parseInt).collect(Collectors.toList());
                otherTickets.add(ticketLine);
            }
        }
        for (String ticketClass : ticketClasses) {
            HashMap<Integer, Boolean> newHash = new HashMap<>();
            newHash.putAll(possibleVals);
            sectionMap.put(ticketClass, newHash);
        }
    }

    private Long solutionPart1(){
        Long sum = 0L;
        validTickets = new ArrayList<>();
        for(List<Integer> ticket : otherTickets) {
            boolean goodTicket = true;
            for (Integer sections : ticket) {
                if (!validNumbers.containsKey(sections)) {
                    sum += sections;
                    goodTicket = false;
                }
            }
            if (goodTicket) {
                validTickets.add(ticket);
            }
        }
        return sum;
    }

    private Long solutionPart2() {
        for(List<Integer> ticket : validTickets) {
            Integer columnNum = 0;
            for (Integer classSec: ticket) {
                List<String> validNums = validNumbers.get(classSec);
                for(String key : sectionMap.keySet()) {
                    if(!validNums.contains(key)) {
                        Map<Integer, Boolean> tmp = sectionMap.get(key);
                        tmp.put(columnNum, false);
                    }
                }
                columnNum++;
            }
        }

        Map<String, Integer> finalSectionMap = new HashMap<>();
        while(finalSectionMap.size() < sectionMap.size()) {
            for(String section : sectionMap.keySet()) {
                List<Map.Entry<Integer,Boolean>> vals = sectionMap.get(section).entrySet().stream()
                        .filter(entry -> entry.getValue())
                        .collect(Collectors.toList());
                if (vals.size() == 1) {
                    finalSectionMap.put(section, vals.get(0).getKey());
                }
            }
            for(Map.Entry<String, Integer> vals : finalSectionMap.entrySet()) {
                for(String section : sectionMap.keySet()) {
                    if (!vals.getKey().equals(section)) {
                        Map<Integer, Boolean> tmp = sectionMap.get(section);
                        tmp.put(vals.getValue(), false);
                        sectionMap.put(section, tmp);
                    }
                }
            }
        }
        System.out.println(finalSectionMap);
        Long product = 1L;
        for(Map.Entry<String, Integer> entry : finalSectionMap.entrySet()) {
            if (entry.getKey().startsWith("departure")) {
                product *= myTicket.get(entry.getValue());
            }
        }
        return product;
    }

}

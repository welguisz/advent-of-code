package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MonsterMessages extends AoCDay {

    public interface MessageRule {
        String calcRegex(Map<Integer, MessageRule> rules);
    }
    public static class Rule implements MessageRule{
        List<List<Integer>> options;
        String regex;
        public Rule(String rule) {
            String[] info = rule.split(" ");
            options = new ArrayList<>();
            List<Integer> opts = new ArrayList<>();
            for (int i = 0; i < info.length; i++) {
                if (info[i].equals("|")) {
                    options.add(opts);
                    opts = new ArrayList<>();
                } else {
                    opts.add(Integer.parseInt(info[i]));
                }
            }
            options.add(opts);
            regex = null;
        }

        public String calcRegex(Map<Integer, MessageRule> rules) {
            if (regex == null) {
                regex = "(?:";
                List<String> regexs = new ArrayList<>();
                for (List<Integer> opts : options) {
                    String tmp = "(?:";
                    for (Integer r : opts) {
                        tmp += rules.get(r).calcRegex(rules);
                    }
                    tmp += ")";
                    regexs.add(tmp);
                }
                regex += regexs.stream().collect(Collectors.joining("|"));
                regex += ")";
            }
           return regex;
        }
    }

    public static class CharRule implements MessageRule {
        String chr;
        public CharRule(String chr) {
            this.chr = chr;
        }

        public String calcRegex(Map<Integer, MessageRule> rules) {
            return this.chr;
        }
    }

    Map<Integer, MessageRule> rules;
    List<String> messages;
    public void solve() {
        Long parseTime = Instant.now().toEpochMilli();
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2020/day19/input.txt");
        parseLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1();
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2();
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public void parseLines(List<String> lines) {
        rules = new HashMap<>();
        messages = new ArrayList<>();
        boolean rulesSet = true;
        for (String l : lines) {
            if (l.length() == 0) {
                rulesSet = false;
            } else if (rulesSet) {
                String parts[] = l.split(": ");
                Integer ruleNumber = Integer.parseInt(parts[0]);
                if (!parts[1].contains("\"")) {
                    rules.put(ruleNumber, new Rule(parts[1]));
                } else {
                    rules.put(ruleNumber, new CharRule(parts[1].substring(1,2)));
                }
            } else {
                messages.add(l);
            }
        }
        for(Map.Entry<Integer, MessageRule> rule : rules.entrySet()) {
            rule.getValue().calcRegex(rules);
        }
    }

    public Long solutionPart1() {
        Pattern pattern = Pattern.compile(rules.get(0).calcRegex(rules));
        Long count = 0L;
        for (String l : messages) {
            Matcher m = pattern.matcher(l);
            count += m.matches() ? 1L : 0L;
        }
        return count;
    }

    public Long solutionPart2() {
        String rule42 = rules.get(42).calcRegex(rules);
        String rule31 = rules.get(31).calcRegex(rules);
        String masterRegex = "^((42+) ((42 31) | (42{2} 31{2}) | (42{3} 31{3}) | (42{4} 31{4}) | (42{5} 31{5}) | (42{6} 31{6}) | (42{7} 31{7}) | (42{8} 31{8}) | (42{9} 31{9}) | (42{10} 31{10})))$";
        masterRegex = masterRegex.replaceAll("42", rule42).replaceAll("31", rule31).replaceAll(" ","");
        Pattern pattern = Pattern.compile(masterRegex);
        Long count = 0L;
        for (String l : messages) {
            Matcher m = pattern.matcher(l);
            count += m.matches() ? 1L : 0L;
        }
        return count;
    }


}


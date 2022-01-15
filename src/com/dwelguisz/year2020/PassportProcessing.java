package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Integer.parseInt;

public class PassportProcessing extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2020/day04/input.txt");
        List<Map<String, String>> passports = processLines(lines);
        Long part1 = solutionPart1(passports);
        Long part2 = solutionPart2(passports);
        System.out.println(String.format("Solution Part1: %d",part1));
        System.out.println(String.format("Solution Part2: %d",part2));

    }

    private Long solutionPart1(List<Map<String,String>> passports) {
        return passports.stream().filter(passport -> validPassport(passport)).count();
    }

    private Long solutionPart2(List<Map<String,String>> passports) {
        return passports.stream().filter(passport -> validPassport(passport)).filter(passport -> validate(passport)).count();
    }

    private Boolean validate(Map<String, String> passport) {
        return  (validateYear(passport.get("byr"),1920,2002)) &&
                (validateYear(passport.get("iyr"), 2010, 2020)) &&
                (validateYear(passport.get("eyr"), 2020, 2030)) &&
                (validateHeight(passport.get("hgt"))) &&
                (validateHairColor(passport.get("hcl"))) &&
                (validateEyeColor(passport.get("ecl"))) &&
                (validatePassPortId(passport.get("pid")));
    }

    private Boolean validateYear(String value, Integer minYear, Integer maxYear) {
        if (value.length() != 4) {
            return false;
        }
        Integer year = parseInt(value);
        if ((year >= minYear) && (year <= maxYear)) {
            return true;
        }
        return false;
    }

    private Boolean validateHeight(String value) {
        if (value.contains("cm")) {
            Integer loc = value.indexOf("cm");
            String val = value.substring(0,loc);
            Integer valueHeight = parseInt(val);
            if ((valueHeight >= 150) && (valueHeight <= 193)) {
                return true;
            }
            return false;
        } else if (value.contains("in")) {
            Integer loc = value.indexOf("in");
            String val = value.substring(0,loc);
            Integer valueHeight = parseInt(val);
            if ((valueHeight >= 59) && (valueHeight <= 76)) {
                return true;
            }
            return false;
        }
        return false;
    }

    private Boolean validateHairColor(String value) {
        Pattern pattern = Pattern.compile("^\\#[\\dabcdef]{6}");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    private Boolean validateEyeColor(String value) {
        List<String> validEyeColor = List.of("amb","blu","brn","gry","grn","hzl","oth");
        return (validEyeColor.stream().filter(ve -> ve.equals(value)).count() == 1L);
    }

    private Boolean validatePassPortId(String value) {
        Pattern pattern = Pattern.compile("^\\d{9}");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public boolean validPassport(Map<String,String> passport) {
        String[] validKeys = new String[] {"byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"};
        Long count = Arrays.stream(validKeys).filter(key -> passport.containsKey(key)).count();
        return (count == 7L);
    }

    public List<Map<String,String>> processLines(List<String> lines) {
        List<Map<String,String>> passports = new ArrayList<>();
        Map<String, String> passport = new HashMap<>();
        for(String line : lines) {
            if (line.length() == 0) {
                passports.add(passport);
                passport = new HashMap<>();
            } else {
                String[] keyValuePairs = line.split(" ");
                for(String keyValue : keyValuePairs) {
                    String[] kv = keyValue.split(":");
                    passport.put(kv[0],kv[1]);
                }
            }
        }
        if (!passport.isEmpty()) {
            passports.add(passport);
        }
        return passports;
    }
}

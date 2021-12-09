package com.dwelguisz.year2021.helper.day8;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;

public class SevenWireDisplay {

    String upper;
    String upper_left;
    String middle;
    String upper_right;
    String lower_left;
    String lower_right;
    String lower;

    public SevenWireDisplay(List<String> input) {
        Map<String, Long> LetterMap = createLetterMap(input);
        String number1 = input.stream().filter(str -> str.length() == 2).findFirst().get();
        String number7 = input.stream().filter(str -> str.length() == 3).findFirst().get();
        String number4 = input.stream().filter(str -> str.length() == 4).findFirst().get();
        String number8 = input.stream().filter(str -> str.length() == 7).findFirst().get();
        figureSeveral(LetterMap);
        figureUpperRight(number1);
        figureUpper(number7);
        figureMiddle(number4);
        figureLower(number8);
    }

    public int decodeString(String val) {
        if (val.length() == 2) {
            return 1;
        } else if (val.length() == 3) {
            return 7;
        } else if (val.length() == 4) {
            return 4;
        } else if (val.length() == 7) {
            return 8;
        } else if (val.length() == 6) { // could be 0, 6, 9
            List<String> vals = Arrays.asList(val.split(""));
            if (!vals.contains(middle)) {
                return 0;
            } else if (vals.contains(upper_right)) {
                return 9;
            } else if (vals.contains(lower_left)) {
                return 6;
            }
        } else if (val.length() == 5) { // could be 5, 2, 3
            List<String> vals = Arrays.asList(val.split(""));
            if (vals.contains(upper_left)) {
                return 5;
            } else if (vals.contains(lower_left)) {
                return 2;
            } else {
                return 3;
            }
        }
        return -134;
    }


    public void figureSeveral(Map<String, Long> values) {
        upper_left = getValue(values, 6);
        lower_left = getValue(values, 4);
        lower_right = getValue(values, 9);
    }

    public String getValue(Map<String, Long> values, int num) {
        return values.entrySet().stream()
                .filter(entry -> entry.getValue() == num)
                .map(entry -> entry.getKey())
                .findFirst().get();
    }

    public Map<String, Long> createLetterMap(List<String> input) {
        return input.stream()
                .flatMap(in -> Arrays.stream(in.split("")))
                .collect(Collectors.groupingBy(x -> x.toString(), counting()));
    }

    public void figureUpper(String number7) {
        List<String> n7List = Arrays.asList(number7.split(""));
        upper = n7List.stream()
                .filter(str -> !str.equals(upper_right))
                .filter(str -> !str.equals(lower_right))
                .findFirst().get();
    }

    public void figureUpperRight(String number1) {
        List<String> n1List = Arrays.asList(number1.split(""));
        upper_right = n1List.stream()
                .filter(str -> !str.equals(lower_right))
                .findFirst().get();
    }

    public void figureMiddle(String number4) {
        middle = Arrays.stream(number4.split(""))
                .filter(str -> !str.equals(lower_right))
                .filter(str -> !str.equals(upper_right))
                .filter(str -> !str.equals(upper_left))
                .findFirst().get();
    }

    public void figureLower(String  number8) {
        lower = Arrays.stream(number8.split(""))
                .filter(str -> !str.equals(lower_right))
                .filter(str -> !str.equals(upper_right))
                .filter(str -> !str.equals(upper_left))
                .filter(str -> !str.equals(middle))
                .filter(str -> !str.equals(lower_left))
                .filter(str -> !str.equals(upper))
                .findFirst().get();
    }
}

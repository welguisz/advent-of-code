package com.dwelguisz.year2021.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SevenWireDisplay {

    String upper;
    String upper_left;
    String middle;
    String upper_right;
    String lower_left;
    String lower_right;
    String lower;

    public SevenWireDisplay(List<String> input) {
        Map<String, Integer> LetterMap = createLetterMap(input);
        String number1 = input.stream().filter(str -> str.length() == 2).collect(Collectors.toList()).get(0);
        String number7 = input.stream().filter(str -> str.length() == 3).collect(Collectors.toList()).get(0);
        String number4 = input.stream().filter(str -> str.length() == 4).collect(Collectors.toList()).get(0);
        String number8 = input.stream().filter(str -> str.length() == 7).collect(Collectors.toList()).get(0);
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
            List<String> vals = Arrays.stream(val.split("")).collect(Collectors.toList());
            if (!vals.contains(middle)) {
                return 0;
            } else if (vals.contains(upper_right)) {
                return 9;
            } else if (vals.contains(lower_left)) {
                return 6;
            }
        } else if (val.length() == 5) { // could be 5, 2, 3
            List<String> vals = Arrays.stream(val.split("")).collect(Collectors.toList());
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


    public void figureSeveral(Map<String, Integer> values) {
        upper_left = values.entrySet().stream().filter(entry -> entry.getValue() == 6).map(entry -> entry.getKey()).collect(Collectors.toList()).get(0);
        lower_left = values.entrySet().stream().filter(entry -> entry.getValue() == 4).map(entry -> entry.getKey()).collect(Collectors.toList()).get(0);
        lower_right = values.entrySet().stream().filter(entry -> entry.getValue() == 9).map(entry -> entry.getKey()).collect(Collectors.toList()).get(0);
    }

    public Map<String, Integer> createLetterMap(List<String> input) {
        Map<String, Integer> characterIntegerMap = new HashMap<>();
        characterIntegerMap.put("a",0);
        characterIntegerMap.put("b",0);
        characterIntegerMap.put("c",0);
        characterIntegerMap.put("d",0);
        characterIntegerMap.put("e",0);
        characterIntegerMap.put("f",0);
        characterIntegerMap.put("g",0);
        for (String in: input) {
            String[] vals = in.split("");
            for (String val : vals) {
                Integer tmp = characterIntegerMap.get(val);
                tmp++;
                characterIntegerMap.put(val, tmp);
            }
        }
        return characterIntegerMap;
    }

    public void figureUpper(String number7) {
        List<String> n7List = Arrays.stream(number7.split("")).collect(Collectors.toList());
        upper = n7List.stream().filter(str -> !str.equals(upper_right)).filter(str -> !str.equals(lower_right)).collect(Collectors.toList()).get(0);
    }

    public void figureUpperRight(String number1) {
        List<String> n1List = Arrays.stream(number1.split("")).collect(Collectors.toList());
        upper_right = n1List.stream().filter(str -> !str.equals(lower_right)).collect(Collectors.toList()).get(0);
    }

    public void figureMiddle(String number4) {
        middle = Arrays.stream(number4.split(""))
                .filter(str -> !str.equals(lower_right))
                .filter(str -> !str.equals(upper_right))
                .filter(str -> !str.equals(upper_left))
                .collect(Collectors.toList()).get(0);
    }

    public void figureLower(String  number8) {
        lower = Arrays.stream(number8.split(""))
                .filter(str -> !str.equals(lower_right))
                .filter(str -> !str.equals(upper_right))
                .filter(str -> !str.equals(upper_left))
                .filter(str -> !str.equals(middle))
                .filter(str -> !str.equals(lower_left))
                .filter(str -> !str.equals(upper))
                .collect(Collectors.toList()).get(0);

    }
}

package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class JSAbacusFramework extends AoCDay {
    List<Long> numbersInPart1;
    public void solve() {
        String myJson = "";
        try {
            myJson = new Scanner(new File("/Users/dwelguisz/personal/advent-of-code/src/resources/year2015/day12/input.txt"))
                    .useDelimiter("\\Z").next();
        } catch (FileNotFoundException e ) {
            System.out.println("Unable to find file: "+ e);
        }
        JSONObject myJsonObject = new JSONObject(myJson);
        Long part1 = solutionPart1(myJsonObject);
        Long part2 = solutionPart2(myJsonObject);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public long solutionPart1(JSONObject jsonValue) {
        numbersInPart1 = new ArrayList<>();
        processHashMap(jsonValue.toMap(), false);
        return numbersInPart1.stream().reduce(0L, (a, b) -> a + b);
    }

    public long solutionPart2(JSONObject jsonValue) {
        numbersInPart1 = new ArrayList<>();
        processHashMap(jsonValue.toMap(), true);
        return numbersInPart1.stream().reduce(0L, (a, b) -> a + b);
    }

    private void processHashMap(Map<String, Object> values, boolean part2) {
        if (part2) {
            for(Map.Entry<String, Object> val : values.entrySet()) {
                if (val.getKey().equals("red")) {
                    return;
                } else if (val.getValue() instanceof String) {
                    if (val.getValue().toString().equals("red")) {
                        return;
                    }
                }
            }
        }
        for(Map.Entry<String,Object> val : values.entrySet()) {
            processObject(val.getValue(), part2);
            try {
                Long tmp = Long.parseLong(val.getKey());
                numbersInPart1.add(tmp);
            } catch (NumberFormatException e) {
                //Do nothing
            }
        }
    }

    private void processJsonArray(ArrayList<Object> values, boolean part2) {
        for(Object value : values) {
            processObject(value, part2);
        }
    }

    private void processObject(Object obj, boolean part2) {
        if (obj instanceof ArrayList) {
            processJsonArray((ArrayList)(obj), part2);
        } else if (obj instanceof HashMap) {
            processHashMap((Map<String,Object>)(obj), part2);
        } else if (obj instanceof Long) {
            numbersInPart1.add((Long)(obj));
        } else if (obj instanceof Integer) {
            numbersInPart1.add(0L + (Integer)(obj));
        }

    }

}

package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.CharBuffer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class JSAbacusFramework extends AoCDay {
    public void solve() {
        String myJson = "";
        timeMarkers[0] = Instant.now().toEpochMilli();
            myJson = new Scanner(getFileFromResourceStream(2015,12,false,0))
                    .useDelimiter("\\Z").next();
        JSONObject myJsonObject = new JSONObject(myJson);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(myJsonObject);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(myJsonObject);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(JSONObject jsonValue) {
        return processHashMap(jsonValue.toMap(), false).stream().mapToInt(l -> l).sum();
    }

    public Integer solutionPart2(JSONObject jsonValue) {
        return processHashMap(jsonValue.toMap(), true).stream().mapToInt(l -> l).sum();
    }

    private List<Integer> processHashMap(Map<String, Object> values, boolean part2) {
        List<Integer> numbers = new ArrayList<>();
        Long keysWithRed = values.keySet().stream().filter(key -> key.equals("red")).count();
        Long valuesWithRed = values.entrySet().stream()
                .map(e -> e.getValue())
                .filter(v -> v instanceof String)
                .filter(v -> v.toString().equals("red"))
                .count();
        Long totalRed = keysWithRed + valuesWithRed;
        if (part2 && (totalRed > 0L)) {
            return numbers;
        }
        for(Map.Entry<String,Object> val : values.entrySet()) {
            numbers.addAll(processObject(val.getValue(), part2));
            try {
                Integer tmp = Integer.parseInt(val.getKey());
                numbers.add(tmp);
            } catch (NumberFormatException e) {
                //Do nothing
            }
        }
        return numbers;
    }

    private List<Integer> processJsonArray(ArrayList<Object> values, boolean part2) {
        List<Integer> numbers = new ArrayList<>();
        for(Object value : values) {
            numbers.addAll(processObject(value, part2));
        }
        return numbers;
    }

    private List<Integer> processObject(Object obj, boolean part2) {
        if (obj instanceof ArrayList) {
            return processJsonArray((ArrayList)(obj), part2);
        } else if (obj instanceof HashMap) {
            return processHashMap((Map<String,Object>)(obj), part2);
        } else if (obj instanceof Integer) {
            return List.of((Integer)(obj));
        }
        return new ArrayList<>();
    }
}

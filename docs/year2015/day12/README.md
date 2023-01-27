# Day 12: JSAbacusFramework.io

[Back to Top README file](../../../README.md)
## Overview
Difficult Level: Easy

Input: JSON object

This puzzle is about how well can you understand JSON and how your programming
language handles JSON.

## Both parts
When reading in the input, treat it as a JSON Document. When doing that, it makes
the processing easier.

```java
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
```

The above leads the following calls:

```java
    public Integer solutionPart1(JSONObject jsonValue) {
        return processHashMap(jsonValue.toMap(), false).stream().mapToInt(l -> l).sum();
    }

    public Integer solutionPart2(JSONObject jsonValue) {
        return processHashMap(jsonValue.toMap(), true).stream().mapToInt(l -> l).sum();
    }
```

|[Previous (Day 11)](../day11/README.md)|[Next (Day 13)](../day13/README.md)|
package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MedicineForRudolph extends AoCDay {
    Map<String, List<String>> replacements;
    String initialMolecule;
    Set<String> possibleValues;
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2015,19,false,0);
        createReplacements(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        doCalibrationStep();
        part1Answer = possibleValues.size();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public void doCalibrationStep() {
        possibleValues = new HashSet<>();
        for (Map.Entry<String, List<String>> val : replacements.entrySet()) {
            String searchStr = val.getKey();
            int currentPos = 0;
            String testString = initialMolecule;
            currentPos = testString.indexOf(searchStr, currentPos);
            while (currentPos >= 0) {
                currentPos = testString.indexOf(searchStr, currentPos);
                String firstPart = initialMolecule.substring(0,currentPos);
                String secondPart = initialMolecule.substring(currentPos+searchStr.length());
                secondPart = (secondPart==null) ? "" : secondPart;
                for (String replaceStr : val.getValue()) {
                    possibleValues.add(firstPart + replaceStr + secondPart);
                }
                currentPos = testString.indexOf(searchStr, currentPos+searchStr.length());
            }
        }
    }

    public void createReplacements(List<String> lines) {
        replacements = new HashMap<>();
        for(String line : lines) {
            if (line.contains("=>")) {
                String sides[] = line.split(" => ");
                List<String> values = replacements.getOrDefault(sides[0], new ArrayList<>());
                values.add(sides[1]);
                replacements.put(sides[0], values);
            } else if (line.length() > 0) {
                initialMolecule = line;
            }
        }
    }

    public Integer solutionPart2() {
        //Looking at the equations, noticed the following
        // 1) All of the symbols are elements, so for example, Al is Alinuium. H is Hydrogen. O is Oxygen and so far
        //    So if we take that approach, we can count up all of the upper case letters in the string and that is the
        //    number of total elements.
        // 2) There are some symbols that only appear in the product, so those can be ignored in steps.
        //    - Ar
        //    - Rn
        //    - Y
        //    - C
        Map<String, Integer> elementCounts = new HashMap<>();
        for (String val : replacements.keySet()) {
            elementCounts.put(val,0);
        }
        elementCounts.put("Rn",0);
        elementCounts.put("Ar",0);
        elementCounts.put("Y",0);
        elementCounts.put("C",0);
        Integer currentPos = 0;
        while (currentPos < initialMolecule.length()) {
            String temp = "";
            if (currentPos < initialMolecule.length() - 2) {
                temp = initialMolecule.substring(currentPos+1,currentPos+2);
            } else {
                temp = initialMolecule.substring(currentPos+1);
            }
            Integer nextElement = (temp.toLowerCase().equals(temp)) ? currentPos + 2 : currentPos + 1;
            nextElement = (nextElement > initialMolecule.length()) ? initialMolecule.length() : nextElement;
            String currentElement = initialMolecule.substring(currentPos,nextElement);
            elementCounts.computeIfPresent(currentElement, (a,b) -> b+1);
            currentPos = nextElement;
        }
        Integer totalElements = elementCounts.entrySet().stream().map(e -> e.getValue()).reduce(0, (a,b) -> a+b);
        return totalElements - elementCounts.get("Rn") - elementCounts.get("Ar") - 2 * elementCounts.get("Y") - elementCounts.get("C");
    }

}

package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FractalArt extends AoCDay {
    Map<String, String> transformer;
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2017,21,false,0);
        createTransformer(lines);
        String initialGrid = ".#./..#/###";
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(initialGrid, 5);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart1(initialGrid, 18);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(String grid, Integer turns) {
        Map<String, Integer> value = Map.of(".",0,"#",1);
        String currentGrid = grid;
        for (int i = 0; i < turns; i++) {
            currentGrid = transformOneTime(currentGrid);
        }
        List<String> output = convertStringToList(currentGrid);
        return output.stream().mapToInt(s -> value.getOrDefault(s,0)).sum();
    }

    public String transformOneTime(String inputGrid) {
        Integer size = inputGrid.indexOf('/');
        List<String> gridItems = new ArrayList<>();
        List<String> singleItems = convertStringToList(inputGrid);
        Integer inputSize = (size % 2 == 0) ? 2 : 3;
        for (int v = 0; v < size/inputSize; v++) {
            for (int w = 0; w < size/inputSize; w++) {
                String currentGrid = "";
                for (int x = 0; x < inputSize; x++) {
                    for (int y = 0; y < inputSize; y++) {
                        currentGrid += singleItems.get((v*(inputSize*(size+1)))+(w*inputSize)+(x*(size+1))+y);
                    }
                    currentGrid += "/";
                }
                currentGrid = currentGrid.substring(0,currentGrid.length()-1);
                String newGrid = findTransformAndRun(currentGrid, inputSize);
                gridItems.add(newGrid);
            }
        }
        return combineGrid(gridItems, inputSize + 1);
    }

    public String combineGrid(List<String> grids, Integer outputSize) {
        String combined = "";
        Integer outerSize = (int)Math.sqrt(grids.size());
        Integer matrixSize = grids.get(0).replace("/","").length();
        Integer innerSize = (int)Math.sqrt(matrixSize);
        Integer totalSize = outerSize * innerSize;
        for (int i = 0; i < totalSize; i++) {
            for(int j = 0; j < totalSize; j++) {
                Integer matrixNum = ((i / outputSize) * outerSize) + (j / outputSize);
                Integer rowNumber = i % outputSize;
                Integer colNumber = j % outputSize;
                String grid = grids.get(matrixNum);
                combined += grid.charAt(rowNumber * (outputSize+1) + colNumber);
            }
            combined += "/";
        }
        return combined.substring(0, combined.length()-1);
    }
    public String findTransformAndRun(String input, Integer size) {
        String matrix = input;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                if (transformer.containsKey(matrix)) {
                    return transformer.get(matrix);
                }
                matrix = (size == 2) ? flipMatrixSize2(matrix) : flipMatrixSize3(matrix);
            }
            matrix = (size == 2) ? rotateMatrixSize2(matrix) : rotateMatrixSize3(matrix);
        }
        return "--/--";
    }

    public String flipMatrixSize2(String input) {
        String s[] = input.split("");
        return s[1]+s[0]+s[2]+s[4]+s[3];
    }

    public String flipMatrixSize3(String input) {
        String s[] = input.split("");
        return s[2]+s[1]+s[0]+s[3]+s[6]+s[5]+s[4]+s[7]+s[10]+s[9]+s[8];
    }
    public String rotateMatrixSize2(String input) {
        String s[] = input.split("");
        return s[3]+s[0]+s[2]+s[4]+s[1];
    }

    public String rotateMatrixSize3(String input) {
        String s[] = input.split("");
        return s[8]+s[4]+s[0]+s[3]+s[9]+s[5]+s[1]+s[7]+s[10]+s[6]+s[2];
    }

    public void createTransformer(List<String> lines) {
        transformer = new HashMap<>();
        for (String line : lines) {
            String io[] = line.split(" => ");
            transformer.put(io[0], io[1]);
        }
    }
}

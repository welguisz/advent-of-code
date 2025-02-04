package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SpaceImageFormat extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,8,false,0);
        List<String> layers = createLayers(lines, 25, 6);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(layers);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(layers, 25, 6);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public List<String> createLayers(List<String> lines, Integer wide, Integer tall) {
        String remainingLine = lines.get(0);
        List<String> layers = new ArrayList<>();
        while (remainingLine.length() > 0) {
            layers.add(remainingLine.substring(0,wide * tall));
            if (remainingLine.length() > (wide * tall)) {
                remainingLine = remainingLine.substring(wide * tall);
            } else {
                remainingLine = "";
            }
        }
        return layers;
    }


    public Integer solutionPart1(List<String> layers) {
        Long minZeros = 15000L;
        Long result = 0L;
        for (String layer : layers) {
            long countZeros = layer.chars().filter(ch -> ch == '0').count();
            if (countZeros < minZeros) {
                minZeros = countZeros;
                long countOnes = layer.chars().filter(ch -> ch == '1').count();
                long countTwos = layer.chars().filter(ch -> ch == '2').count();
                result = countOnes * countTwos;
            }
        }
        return result.intValue();
    }

    public String solutionPart2(List<String> layers, int width, int tall) {
        Character chars[] = new Character[width*tall];
        for (int i = 0; i < (width*tall); i++) {
            chars[i] = '2';
        }
        for (String layer : layers) {
            for (int i = 0; i < (width*tall); i++) {
                if (chars[i] == '2') {
                    chars[i] = layer.charAt(i);
                }
            }
        }
        String finalPicture = "\n";
        for (int i = 0; i < (width*tall); i++) {
            if (chars[i] == '0') {
                finalPicture += " ";
            } else if (chars[i] == '1') {
                finalPicture += "#";
            }
            if (i % width == (width-1)) {
                finalPicture += "\n";
            }
        }
        return finalPicture;
    }
}

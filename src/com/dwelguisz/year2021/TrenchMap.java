package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2021.helper.Tuple;
import com.dwelguisz.year2021.helper.day19.Coordinate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class TrenchMap extends AoCDay {

    private static Integer IMAGE_SIZE = 100;
    private String[] enhancementAlgorithm = new String[]{};

    public TrenchMap() {
        super();
        enhancementAlgorithm = new String[]{};
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2021, 20, false, 0);
        enhancementAlgorithm = lines.get(0).split("");
        List<String> imagelines = new ArrayList<>(lines);
        imagelines.remove(lines.get(0));
        imagelines.remove(lines.get(1));
        String[][] newImage = createImage(imagelines, IMAGE_SIZE);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solution(newImage, 2);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solution(newImage, 50);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Long solution(String[][] image, Integer steps) {
        String defaultChr = ".";
        String[][] newImage = paddedImage(image, image.length, (steps+2)*2, defaultChr);
        for (int stepNumber=0; stepNumber<steps; stepNumber++) {
            newImage = processImage(newImage, defaultChr);
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < 9; i ++) {
                sb.append(defaultChr);
            }
            defaultChr = enCodeBit(sb.toString());
        }
        return countDark(newImage);
    }

    private Long countDark(String[][] image) {
        return Arrays.stream(image).map(row ->
                Arrays.stream(row)
                        .collect(Collectors.groupingBy(str ->str, Collectors.counting()))
                        .getOrDefault("#", 0L)
        ).reduce(0L, Long::sum);
    }

    private String[][] processImage(String[][] image, String defaultChar) {
        String[][] newImage = new String[image.length][image[0].length];
        for (int currentRow = 0; currentRow < image.length; currentRow++) {
            for (int currentCol = 0;  currentCol < image[currentRow].length; currentCol++) {
                newImage[currentRow][currentCol] = processNeighborhood(image, currentRow, currentCol, defaultChar);
            }
        }

        return newImage;
    }

    private String processNeighborhood(String[][] image, Integer currentRow, Integer currentCol, String defaultChar) {
        String []encoding = new String[9];
        for (int k = 0; k < 9; k++) {
            int row = (k / 3) - 1;
            int col = (k % 3) - 1;
            try {
                String bit = image[currentRow+row][currentCol+col];
                encoding[k] = bit;
            } catch (Exception e) {
                encoding[k] = defaultChar;
            }
        }
        StringBuffer sb = new StringBuffer();
        for (int k = 0; k < 9; k++) {
            sb.append(encoding[k]);
        }
      return enCodeBit(sb.toString());

    }

    private String enCodeBit(String algo) {
        String newString = algo.replace(".","0").replace("#","1");
        return enhancementAlgorithm[parseInt(newString,2)];
    }

    private String[][] paddedImage(String[][] image, Integer size, Integer pad, String defaultChar) {
        String[][] newImage = new String[size+(2*pad)][size+(2*pad)];
        for (int i = 0; i < size + (2*pad); i++) {
            for(int j = 0; j < size + (2*pad); j++) {
                newImage[i][j] = defaultChar;
            }
        }
        for (int row = 0; row < image.length; row++) {
            for(int col = 0; col < image[row].length; col++) {
                String bit = image[row][col];
                newImage[row+pad][col+pad] = bit;
            }
        }

        return newImage;
    }

    private String[][] createImage (List<String> lines, Integer size) {
        String[][] image = new String[size][size];
        Integer j = 0;
        for(String line : lines) {
            String[] tmpLine = line.split("");
            for (int i = 0; i < tmpLine.length; i++) {
                image[j][i] = tmpLine[i];
            }
            j++;
        }
        return image;
    }
}

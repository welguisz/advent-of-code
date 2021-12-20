package com.dwelguisz.year2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Integer.parseInt;

public class AdventDay20 {

    static Integer IMAGE_SIZE = 100;
    static String[] enhancementAlgorithm = new String[]{};

    public static void main(String[] args) {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2021/day20/input.txt");
        enhancementAlgorithm = lines.get(0).split("");
        List<String> imagelines = new ArrayList<>(lines);
        imagelines.remove(lines.get(0));
        imagelines.remove(lines.get(1));
        String[][] newImage = createImage(imagelines, IMAGE_SIZE);
        Long part1 = solution(newImage, 2);
        Long part2 = solution(newImage, 50);
        System.out.println(String.format("Solution Part1: %d",part1));
        System.out.println(String.format("Solution Part1: %d",part2));

    }

    public static Long solution(String[][] image, Integer steps) {
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

    public static Long countDark(String[][] image) {
        return Arrays.stream(image).map(row ->
                Arrays.stream(row)
                        .collect(Collectors.groupingBy(str ->str, Collectors.counting()))
                        .getOrDefault("#", 0L)
        ).reduce(0L, Long::sum);
    }

    public static String[][] processImage(String[][] image, String defaultChar) {
        String[][] newImage = new String[image.length][image[0].length];
        for (int currentRow = 0; currentRow < image.length; currentRow++) {
            for (int currentCol = 0;  currentCol < image[currentRow].length; currentCol++) {
                newImage[currentRow][currentCol] = processNeighborhood(image, currentRow, currentCol, defaultChar);
            }
        }

        return newImage;
    }

    public static String processNeighborhood(String[][] image, Integer currentRow, Integer currentCol, String defaultChar) {
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

    public static String enCodeBit(String algo) {
        String newString = algo.replace(".","0").replace("#","1");
        return enhancementAlgorithm[parseInt(newString,2)];
    }

    public static String[][] paddedImage(String[][] image, Integer size, Integer pad, String defaultChar) {
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

    public static String[][] createImage (List<String> lines, Integer size) {
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

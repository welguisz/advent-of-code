package com.dwelguisz.year2015;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.security.*;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;

public class AdventDay04 {
    public static void main(String[] args) {
        List<String> instructions = readFile("/home/dwelguisz/advent_of_code/src/resources/year2015/day03/input.txt");
        Integer part1 = solutionPart1("iwrupvqb");
        Integer part2 = solutionPart2("iwrupvqb");
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    static public Integer solutionPart1(String input) {
        Integer i = 0;
        while (true) {
            String str = input.concat(i.toString());
            try {
                byte[] bytesOfMessage = str.getBytes(StandardCharsets.UTF_8);
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] theMD5digest = md.digest(bytesOfMessage);
                StringBuffer hexString = new StringBuffer();
                if ((theMD5digest[0] == 0) && (theMD5digest[1] == 0) && (theMD5digest[2] >= 0) && (theMD5digest[2] < 16)) {
                    return i;
                }
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Bad algorithm selected");
            }
            i++;
        }
    }
    static public Integer solutionPart2(String input) {
        Integer i = 0;
        while (true) {
            String str = input.concat(i.toString());
            try {
                byte[] bytesOfMessage = str.getBytes(StandardCharsets.UTF_8);
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] theMD5digest = md.digest(bytesOfMessage);
                StringBuffer hexString = new StringBuffer();
                if ((theMD5digest[0] == 0) && (theMD5digest[1] == 0) && (theMD5digest[2] == 0)) {
                    return i;
                }
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Bad algorithm selected");
            }
            i++;
        }
    }
}

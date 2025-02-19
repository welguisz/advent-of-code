package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OneTimePad extends AoCDay {
    public static String MY_SALT = "jlmsuwbz";
    public static String TEST_SALT = "abc";

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,14,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines.get(0), false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart1(lines.get(0), true);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public String createHash(String input, Boolean stretched) {
        Integer numberOfHashes = stretched ? 2016 : 0;
        String tmpStr = input;
        for (int i = 0; i <= numberOfHashes; i++) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] md5sum = md.digest(tmpStr.getBytes());
                tmpStr = String.format("%032X", new BigInteger(1, md5sum)).toLowerCase();
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Bad algorithm selected");
            }
        }
        return tmpStr;
    }

    public Integer solutionPart1(String input, Boolean stretched) {
        Map<Integer, Character> ThreeInARow = new HashMap<>();
        Map<Integer, Character> FiveInARow = new HashMap<>();
        int hashNumber = 0;
        for (;hashNumber < 10000; hashNumber++) {
            String hash = createHash(input+hashNumber,stretched);
            Pair<Boolean, Character> check3 = anyCharactersInRowTheSame(hash, 3);
            Pair<Boolean, Character> check5 = anyCharactersInRowTheSame(hash, 5);
            if (check3.getLeft()) {
                ThreeInARow.put(hashNumber, check3.getRight());
            }
            if (check5.getLeft()) {
                FiveInARow.put(hashNumber, check5.getRight());
            }
        }
        int currentNumber = 0;
        int leftToFind = 64;
        while (leftToFind > 0) {
            if (currentNumber + 1000 >= hashNumber) {
                int nextEnd = hashNumber + 10000;
                if (leftToFind < 16 && leftToFind >= 6) {
                    nextEnd = hashNumber + 1000;
                } else if (leftToFind < 6) {
                    nextEnd = currentNumber + 1001;
                }
                for(;hashNumber < nextEnd; hashNumber++) {
                    String hash = createHash(input+hashNumber,stretched);
                    Pair<Boolean, Character> check3 = anyCharactersInRowTheSame(hash, 3);
                    Pair<Boolean, Character> check5 = anyCharactersInRowTheSame(hash, 5);
                    if (check3.getLeft()) {
                        ThreeInARow.put(hashNumber, check3.getRight());
                    }
                    if (check5.getLeft()) {
                        FiveInARow.put(hashNumber, check5.getRight());
                    }
                }
            }
            if (ThreeInARow.containsKey(currentNumber)) {
                final int cn = currentNumber;
                if (IntStream.range(currentNumber+1, currentNumber+1000).boxed()
                        .filter(num -> FiveInARow.containsKey(num))
                        .anyMatch(num -> ThreeInARow.get(cn) == FiveInARow.get(num))) {
                    leftToFind--;
                }
            }
            currentNumber++;
        }
        return currentNumber-1;
    }
    public Pair<Boolean, Character> anyCharactersInRowTheSame(String code, Integer length) {
        Pattern pattern = Pattern.compile("(.)\\1{" + (length-1) + "}");
        Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            return Pair.of(true, matcher.group().charAt(0));
        }
        return Pair.of(false, 'q');
    }
}

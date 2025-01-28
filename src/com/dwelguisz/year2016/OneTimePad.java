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

public class OneTimePad extends AoCDay {
    public static String MY_SALT = "jlmsuwbz";
    public static String TEST_SALT = "abc";

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,14,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines.get(0), false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        //part2Answer = solutionPart1(lines.get(0), true);
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
        Integer currentNumber = 0;
        Integer knownSize = 0;
        String[] hashes = new String[40000];
        for (Integer i = 0; i < 40000; i++) {
            String str = input.concat(i.toString());
            hashes[i] = createHash(str, stretched);
        }
        while (knownSize < 64) {
            Pair<Boolean, String> possibleCheck = anyCharactersInRowTheSame(hashes[currentNumber], 3);
            if(possibleCheck.getKey()) {
                for (Integer i = currentNumber+1; i <= currentNumber + 1000; i++) {
                    Pair<Boolean, String> has5CharsInARow = anyCharactersInRowTheSame(hashes[i], 5);
                    if (has5CharsInARow.getLeft()) {
                        if (possibleCheck.getRight().equals(has5CharsInARow.getRight())) {
                            knownSize++;
                            break;
                        }
                    }
                }
            }
            currentNumber++;
        }
        return currentNumber-1;
    }
    public Pair<Boolean, String> anyCharactersInRowTheSame(String code, Integer length) {
        Pattern pattern = Pattern.compile("(.)\\1{" + (length-1) + "}");
        Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            return Pair.of(true, matcher.group().substring(0,1));
        }
        return Pair.of(false, "q");
    }
}

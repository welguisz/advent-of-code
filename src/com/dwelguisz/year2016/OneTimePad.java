package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OneTimePad extends AoCDay {
    public static String MY_SALT = "jlmsuwbz";
    public static String TEST_SALT = "abc";

    public void solve() {
        Integer part1 = solutionPart1(MY_SALT, false);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = solutionPart1(MY_SALT, true);
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public String createHash(String input, Boolean stretched) {
        Integer numberOfHashes = stretched ? 2016 : 1;
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
        String[] hashes = new String[100000];
        for (Integer i = 0; i < 100000; i++) {
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
        Integer inARow = 0;
        String currentCheck = "q";
        String[] chrs = code.split("");
        for(int i = 0; i < chrs.length; i++) {
            if (!currentCheck.equalsIgnoreCase(chrs[i])) {
                inARow = 1;
                currentCheck = chrs[i];
            } else {
                inARow++;
                if (inARow == length) {
                    return Pair.of(true, currentCheck);
                }
            }
        }
        return Pair.of(false, currentCheck);
    }
}

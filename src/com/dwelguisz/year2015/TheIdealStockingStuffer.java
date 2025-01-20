package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.security.*;


public class TheIdealStockingStuffer extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2015,4,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(instructions.get(0));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(instructions.get(0));
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(String input) {
        Integer i = 0;
        while (true) {
            String str = input.concat(i.toString());
            try {
                byte[] bytesOfMessage = str.getBytes(StandardCharsets.UTF_8);
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] theMD5digest = md.digest(bytesOfMessage);
                if ((theMD5digest[0] == 0) && (theMD5digest[1] == 0) && (theMD5digest[2] >= 0) && (theMD5digest[2] < 16)) {
                    return i;
                }
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Bad algorithm selected");
            }
            i++;
        }
    }
    public Integer solutionPart2(String input) {
        Integer i = 0;
        while (true) {
            String str = input.concat(i.toString());
            try {
                byte[] bytesOfMessage = str.getBytes(StandardCharsets.UTF_8);
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] theMD5digest = md.digest(bytesOfMessage);
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

package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NiceGameOfChess extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,5,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines.get(0));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines.get(0));
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public String solutionPart1(String input) {
        Integer i = 0;
        Integer length = 0;
        StringBuffer password = new StringBuffer();
        while (true) {
            String str = input.concat(i.toString());
            try {
                byte[] bytesOfMessage = str.getBytes(StandardCharsets.UTF_8);
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] theMD5digest = md.digest(bytesOfMessage);
                if ((theMD5digest[0] == 0) &&
                        (theMD5digest[1] == 0) &&
                        (theMD5digest[2] >= 0) &&
                        (theMD5digest[2] < 16)) {
                    password.append(Integer.toHexString(theMD5digest[2] % 16));
                    length++;
                    if (length == 8) {
                        return password.toString();
                    }
                }
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Bad algorithm selected");
            }
            i++;
        }
    }

    public String solutionPart2(String input) {
        Integer i = 0;
        String[] password = new String[8];
        List<Integer> alreadyTaken = new ArrayList<>();
        while (true) {
            String str = input.concat(i.toString());
            try {
                byte[] bytesOfMessage = str.getBytes(StandardCharsets.UTF_8);
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] theMD5digest = md.digest(bytesOfMessage);
                if ((theMD5digest[0] == 0) &&
                        (theMD5digest[1] == 0) &&
                        (theMD5digest[2] >= 0) &&
                        (theMD5digest[2] < 16)) {
                    int strloc = theMD5digest[2] % 16;
                    if ((strloc < 8) && !alreadyTaken.contains(strloc)) {
                        if (i == 5357525) {
                            System.out.println("Stop here");
                        }
                        String tmp = Integer.toHexString(theMD5digest[3]);
                        alreadyTaken.add(strloc);
                        if (theMD5digest[3] < 0) {
                            password[strloc] = tmp.substring(6, 7);
                        } else {
                            password[strloc] = tmp.substring(0,1);
                        }
                    }
                    if (alreadyTaken.size() == 8) {
                        StringBuffer tmp = new StringBuffer();
                        for (int j = 0; j < 8; j++) {
                            tmp.append(password[j]);
                        }
                        return tmp.toString();
                    }
                }
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Bad algorithm selected");
            }
            i++;
        }

    }


}

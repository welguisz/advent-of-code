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
        part2Answer = solutionPart2(instructions.get(0), Integer.parseInt(part1Answer.toString()));
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
        documentation();
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
    public int solutionPart2(String input, int part1Answer) {
        int i = part1Answer;
        while (true) {
            String str = input.concat(""+i);
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

    void documentation() {
        puzzleName = "The Ideal Stocking Stuffer";
        difficultLevel = "2 out of 10";
        inputDescription = "8 random letter";
        skills = List.of("MD5 Encryption");
        part1Solution = """
* Start a counter at 0.
* Create a string that contains your input string plus a counter.
* Do a MD5 hash on the created String
* See if the string starts with 5 zeroes.

```java
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
```
""";
        part2Solution = """
Same as Part 1, but the first 6 characters of the digest are zero. If you want, you can start from your answer in Part1 since
for Part2 to be true, the first 5 characters have to be zero, so you can skip all the integers from
0 to `Part1 Answer`.

```java
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
```""";
    }

}

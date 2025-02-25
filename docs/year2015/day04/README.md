# Day 4 The Ideal Stocking Stuffer

[Back to Top README file](../../../README.md)

## Overview

* [Puzzle Prompt](https://adventofcode.com/2015/day/4)
* Difficult Level: 2 out of 10
* [Input](https://adventofcode.com/2015/day/4/input): 8 random letter
* Skills/Knowledge: MD5 Encryption

## Part 1 Solution:

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


## Part 2 Solution:

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
```

## Times

* Parsing: 4 ms
* Part 1 Solve time: 130 ms
* Part 2 Solve time: 1803 ms

## Solutions: 

* Part 1: 346386
* Part 2: 9958218

| | |
|:---|---:|
|[Previous (Year 2015, Day 3)](../../year2015/day03/README.md)|[Next (Year 2015, Day 5)](../../year2015/day05/README.md)|

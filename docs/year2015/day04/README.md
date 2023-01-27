# Day 4: The Ideal Stocking Stuffer

[Back to Top README file](../../../README.md)
## Overview
Difficult Level: Easy

Input: 8 random letters

Find the lowest integer that produces a MD5 hash that has a certain characterization

## Part 1
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

## Part 2
Same as Part 1, but the first 6 characters of the digest are zero.

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

|[Previous (Day 3)](../day03/README.md)|[Next (Day 5)](../day05/README.md)|
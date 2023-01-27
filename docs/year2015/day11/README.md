# Day 11: Corporate Policy

[Back to Top README file](../../../README.md)
## Overview
Difficult Level: Easy

Input: A random string

Given a set of rules, increment the string until a string matches the Corporate
Policy for passwords.

###
As an Electrical Engineer, I saw this problem as an adder with a carry. So starting
at the end of the string, add 1 and work to the beginning of the string.  The
simplified algorithm is:

```java
char[] chars = password.toCharArray();
char[] newPasswordAttempt = new char[chars.length];
char carry = 1;
for (int i = str.length-1;i >= 0;i--) {
    char tmp = (char)(chars[i]+carry);
    if (tmp > 'z') {
       tmp = 'a';
       carry = 1;
    } else {
       carry = 0; 
    }
}
```

Then just check that the password is valid.  This is the most brute force method
to get things done.  If we want to make it faster, we should skip any password
as soon as we know it is bad.  There is only rule that gives us the chance to skip
lots of bad passwords and that is to avoid the unallowed characters ('i', 'o', 'l').

So if we see a character that is bad, increment the count and change the characters
after the change to 'a'.  So the above code changes to:

```java
    public String incrementPassword(String oldPassword) {
        char chars[] = oldPassword.toCharArray();
        char newChars[] = oldPassword.toCharArray();
        char carry = 1;
        for (int i = chars.length-1; i >= 0; i--) {
            char tmp = (char) (chars[i] + carry);
            if (BAD_CHARS.contains(tmp)) {
                tmp = (char) (tmp + carry);
                for (int j = i+1; j < chars.length;j++) {
                    newChars[j] = 'a';
                }
                carry = 0;
            }
            else if (tmp > 'z') {
                carry = 1;
                tmp = 'a';
            } else {
                carry = 0;
            }
            newChars[i] = tmp;
        }
        return String.valueOf(newChars);
    }
```

### Valid checks
We need to make sure the password is valid so we have the following checks:

```java
    public boolean passwordValid(String currentPassword) {
        char charsT[] = currentPassword.toCharArray();
        Character chars[] = new Character[charsT.length];
        for (int i = 0; i < charsT.length; i++) {
            chars[i] = charsT[i];
        }
        List<Boolean> passwordCriteria = List.of(checkForIncreasingChars(chars), doesContainTwoDoubleLetters(chars));
        return passwordCriteria.stream().allMatch(t -> t);
    }

    boolean checkForIncreasingChars(Character chars[]) {
        List<List<Character>> characterGroups = IntStream.range(0,chars.length-2).boxed().map(i -> List.of(chars[i],chars[i+1],chars[i+2])).collect(Collectors.toList());
        return characterGroups.stream().anyMatch(cg -> ((cg.get(0)+1) == cg.get(1)) && ((cg.get(1)+1) == cg.get(2)));
    }

    boolean doesContainTwoDoubleLetters(Character chars[]) {
        List<List<Character>> characterGroups = IntStream.range(0,chars.length-1).boxed().map(i -> List.of(chars[i],chars[i+1])).collect(Collectors.toList());
        Set<List<Character>> doubleLetters = characterGroups.stream().filter(cg -> cg.get(0) == cg.get(1)).collect(Collectors.toSet());
        return (doubleLetters.size() == 2);
    }
```

# Part 1 and Part 2
For both parts, we just need to find the next valid password given the input.

```java
    public String solutionPart1(String oldPassword) {
        String newPassword = incrementPassword(oldPassword);
        while (!passwordValid(newPassword)) {
            newPassword = incrementPassword(newPassword);
        }
        return newPassword;
    }
```

|[Previous (Day 10)](../day10/README.md)|[Next (Day 12)](../day11/README.md)|
package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;

public class Matchsticks extends AoCDay {

    Pattern pattern = Pattern.compile("\\\\(\\\\|\\\"|x[0123456789abcdef]{2})");

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2015,8,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(instructions);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(instructions);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
        documentation();
    }

    public Integer solutionPart1(List<String> strings) {
        return strings.stream().mapToInt(s->2 + matchedCharacters(s)).sum();
    }

    public Integer solutionPart2(List<String> strings) {
        return strings.stream().mapToInt(s -> 4 + matchedCharactersPart2(s)).sum();
    }

    public int matchedCharacters(String str) {
        int count = 0;
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            int diff = matcher.end() - matcher.start();
            count += diff - 1;
        }
        return count;
    }

    public int matchedCharactersPart2(String str) {
        int count = 0;
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String subString = str.substring(matcher.start(), matcher.end());
            if (subString.contains("\\x")) {
                count += 1;
            } else if (subString.equals("\\\"")) {
                count += 2;
            } else if (subString.equals("\\\\")) {
                count += 2;
            }
        }
        return count;
    }

    void documentation() {
        puzzleName = "Matchsticks";
        difficultLevel = "2 out of 10";
        inputDescription = "lines of strings that have been encoded";
        skills = List.of("regex");
        setup = """
This puzzle becomes very easy if you have the right regex expression. With the\s
right regex expression, the puzzle becomes a straightforward counting exercise.

## Special Regex String

```java
Pattern pattern = Pattern.compile("\\\\\\\\(\\\\\\\\|\\\\\\"|x[0123456789abcdef]{2})");
```

The above regex says look for `\\` followed by one of these conditions:
* `\\`
* `"`
* `x00` -> `xff` (hexadecimal)                
""";
        part1Solution = """
With the above regex, we can write the solution as:

```java
    public Integer solutionPart1(List<String> strings) {
        return strings.stream().mapToInt(s->2 + matchedCharacters(s)).sum();
    }

    public int matchedCharacters(String str) {
        int count = 0;
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            int diff = matcher.end() - matcher.start();
            count += diff - 1;
        }
        return count;
    }

```

Why not write the above as:

```java
Integer allChars = strings.stream().mapToInt(s -> s.length());
Integer NoneEscapedCharacters = strings.stream().mapToInt(s -> s.length()-2-matchedCharacters(s)).sum();
return allChars - NoneEscapedCharacters;
```

If we do some math, we can rewrite it as:
```java
return strings.stream().mapToInt(s -> s.length() - (s.length()-2-matchedCharacters(s))).sum();
```

The `mapToInt porition` can be rewritten as `s -> s.length() - s.length() + 2 + matchedCharacters(s)`,
which can be reduced to `s -> 2 + matchedCharacters(s)`                
""";
        part2Solution = """
```java
    public Integer solutionPart2(List<String> strings) {
        return strings.stream().mapToInt(s -> 4 + matchedCharactersPart2(s)).sum();
    }

    public int matchedCharactersPart2(String str) {
        int count = 0;
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String subString = str.substring(matcher.start(), matcher.end());
            if (subString.contains("\\\\x")) {
                count += 1;
            } else if (subString.equals("\\\\\\"")) {
                count += 2;
            } else if (subString.equals("\\\\\\\\")) {
                count += 2;
            }
        }
        return count;
    }

```
""";
    }

}

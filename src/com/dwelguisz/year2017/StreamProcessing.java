package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.List;
import java.util.Stack;

public class StreamProcessing extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2017,9,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        Pair<Long, Long> answer = solutionPart1(lines);
        part1Answer = answer.getLeft();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = answer.getRight();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Pair<Long,Long> solutionPart1(List<String> lines) {
        String line = lines.get(0);
        char chars[] = line.toCharArray();
        Boolean insideGarbage = false;
        Boolean ignoreNextCharacter = false;
        Long score = 0L;
        Long depth = 0L;
        Long garbageCharacters = 0L;
        Stack<Long> stack = new Stack<>();
        for(int i = 0; i < chars.length; i++) {
            if (ignoreNextCharacter) {
                ignoreNextCharacter = false;
                continue;
            }
            if (insideGarbage) {
                if (chars[i] == '!') {
                    ignoreNextCharacter = true;
                } else if (chars[i] == '>') {
                    insideGarbage = false;
                } else {
                    garbageCharacters++;
                }
            } else if (chars[i] == '<') {
                insideGarbage = true;
            } else if (chars[i] == '{') {
                stack.push(depth);
                depth++;
            } else if (chars[i] == '}') {
                depth = stack.pop();
                score += (depth + 1);
            }
        }
        return Pair.of(score, garbageCharacters);
    }


}

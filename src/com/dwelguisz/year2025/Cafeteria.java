package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Cafeteria extends AoCDay {
    List<Long> ingredients;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 5, false, 0);
        List<Pair<Long,Long>> freshIngredients = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(freshIngredients);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(freshIngredients);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
    }

    List<Pair<Long, Long>> parseLines(List<String> lines) {
        List<Pair<Long, Long>> res = new ArrayList<>();
        boolean firstPart = true;
        ingredients = new ArrayList<>();
        for (String line : lines) {
            if (line.isEmpty()) {
                firstPart = false;
                continue;
            }
            if (firstPart) {
                String[] splits = line.split("-");
                res.add(Pair.of(Long.parseLong(splits[0]), Long.parseLong(splits[1])));
            } else {
                ingredients.add(Long.parseLong(line));
            }
        }
        return res;
    }

    long solutionPart1(List<Pair<Long,Long>> freshIngredients) {
        return ingredients.stream().filter(i -> isFresh(freshIngredients, i)).count();
    }

    boolean isFresh(List<Pair<Long, Long>> freshIngredients, Long ingredient) {
        for(Pair<Long, Long> freshIngredient : freshIngredients) {
            if ((freshIngredient.getLeft() <= (ingredient)) && (ingredient <= freshIngredient.getRight())) {
                return true;
            }
        }
        return false;
    }
    long solutionPart2(List<Pair<Long,Long>> freshIngredients) {
        List<Pair<Long,Long>> ingredients = new ArrayList<>(freshIngredients);
        Collections.sort(ingredients, (a,b) -> a.getLeft() < b.getLeft() ? -1 : 1);
        Stack<Pair<Long,Long>> finalIngredients = new Stack<>();
        finalIngredients.push(ingredients.remove(0));
        while (!ingredients.isEmpty()) {
            Pair<Long,Long> pair = finalIngredients.pop();
            Long x = pair.getLeft();
            Long y = pair.getRight();
            Pair<Long,Long> next = ingredients.remove(0);
            Long a = next.getLeft();
            Long b = next.getRight();
            if (a <= y) {
                finalIngredients.push(Pair.of(x, Long.max(b, y)));
            } else {
                finalIngredients.push(pair);
                finalIngredients.push(next);
            }
        }
        return finalIngredients.stream().map(i -> (i.getRight() - i.getLeft()) + 1).reduce(0L, Long::sum);
    }
}

package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class RambunctiousRecitation extends AoCDay {

    public void solve() {
        String line = "2,0,6,12,1,3";
        Integer part1 = solutionPart1(line, 2019);
        Integer part2 = solutionPart1(line, 30000000-1);
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    private Integer solutionPart1(String line, int turns) {
        List<Integer> numbers = Arrays.stream(line.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        Map<Integer, Integer> lastOccurence = numbers.stream()
                .collect(Collectors.toMap(Function.identity(), numbers::indexOf));
        int number = 0;
        for (int index = numbers.size(); index < turns;++index) {
            Integer last = lastOccurence.put(number, index);
            number = last == null ? 0 : index - last;
        }
        return number;
    }


}

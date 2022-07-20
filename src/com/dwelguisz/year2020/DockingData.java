package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

public class DockingData extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2020/day14/input.txt");
        Long part1 = solutionPart1(lines);
        Long part2 = solutionPart2(lines);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));

    }

    private Long solutionPart1(List<String> lines){
        Long andMask = 0L;
        Long orMask = 0L;
        Map<Integer, Long> memory = new HashMap<>();
        for(String line : lines) {
            if("mask".equals(line.substring(0,4))) {
                Pair<Long, Long> masks = createMask(line);
                andMask = masks.getLeft();
                orMask = masks.getRight();
            } else {
                String[] equal = line.split(" ");
                Long number = Long.parseLong(equal[2]);
                String memLocal = equal[0].split("\\[")[1].split("]")[0];
                Integer memLocation = Integer.parseInt(memLocal);
                memory.put(memLocation, (number | orMask ) & andMask);
            }
        }
        Long sum = 0L;
        return memory.values().stream().reduce(0L, (i, j) -> i + j);
    }

    private Pair<Long, Long> createMask(String line) {
        String[] separate = line.split(" ");
        String mask = separate[2];
        Long andMask = Long.MAX_VALUE;
        Long orMask = 0L;
        Long lcv = 1L << 35;
        String[] bits = mask.split("");
        for(String bit : bits) {
            if (!"X".equalsIgnoreCase(bit)) {
                if ("1".equals(bit)) {
                    orMask |= lcv;
                } else {
                    andMask &= ~lcv;
                }
            }
            lcv >>= 1;
        }
        return Pair.of(andMask, orMask);
    }

    private Long solutionPart2(List<String> lines) {
        Long sum = 0L;
        Map<Long, Long> fullMemory = new HashMap<>();
        Long setMask = 0L;
        List<Integer> interestingBits = new ArrayList<>();
        for (String line : lines) {
            if("mask".equals(line.substring(0,4))) {
                setMask = createAddressMaps(line);
                interestingBits = findInterestingBits(line);
            } else {
                String[] equal = line.split(" ");
                Long number = Long.parseLong(equal[2]);
                String memLocal = equal[0].split("\\[")[1].split("]")[0];
                Long memLocation = Long.parseLong(memLocal);

                List<Long> addressMasks = new ArrayList<>();
                addressMasks.add(memLocation | setMask);
                for (Integer bit : interestingBits) {
                    Long orMask = 1L << bit;
                    Long andMask = Long.MAX_VALUE & ~(1L<<bit);
                    List<Long> newAddressMasks = new ArrayList<>();

                    for(Long aMask : addressMasks) {
                        newAddressMasks.add(aMask | orMask);
                        newAddressMasks.add(aMask & andMask);
                    }
                    addressMasks = newAddressMasks;
                }
                for(Long fAddress: addressMasks) {
                    fullMemory.put(fAddress, number);
                }
            }
        }

        //return sum;
        return fullMemory.values().stream().reduce(0L, (i, j) -> i + j);
    }

    private List<Integer> findInterestingBits(String line) {
        String[] separate = line.split(" ");
        String mask = separate[2];
        List<Integer> bitPositions = new ArrayList<>();
        String[] bits = mask.split("");
        int lcv = 35;
        for(String bit : bits) {
            if ("X".equalsIgnoreCase(bit)) {
                bitPositions.add(lcv);
            }
            lcv--;
        }
        return bitPositions;
    }

    private Long createAddressMaps(String line) {
        String[] separate = line.split(" ");
        String mask = separate[2];
        Long setMask = 0L;
        Long lcv = 1L << 35;
        String[] bits = mask.split("");
        for(String bit : bits) {
            if ("1".equals(bit)) {
                setMask |= lcv;
            }
            lcv >>= 1;
        }
        return setMask;
    }
}

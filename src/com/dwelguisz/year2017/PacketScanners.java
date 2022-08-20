package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PacketScanners extends AoCDay {
    Map<Integer, Integer> scanners;
    Integer largestDepth = -1;
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2017/day13/input.txt");
        createScanners(lines);
        Integer part1 = solutionPart1();
        Integer part2 = solutionPart2();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));

    }

    public Integer solutionPart1() {
        Integer damageReport = 0;
        for (int i = 0; i <= largestDepth;i++) {
            if (isScannerAtTop(i, 0)) {
                damageReport += i * scanners.get(i);
            }
        }
        return damageReport;
    }

    public Integer solutionPart2() {
        Integer delay = 0;
        Boolean damaged = true;
        while (damaged) {
            damaged = false;
            for (int i = 0; i <= largestDepth; i++) {
                if (isScannerAtTop(i, delay)) {
                    damaged = true;
                }
            }
            if (damaged) {
                delay++;
            }
        }
        return delay;
    }

    public boolean isScannerAtTop(Integer step, Integer delay) {
        Integer currentStep = step + delay;
        if (!scanners.containsKey(step)) {
            return false;
        }
        Integer scannerDepth = scanners.get(step);
        Integer atTopModulo = (scannerDepth - 1) * 2;
        return (currentStep % atTopModulo == 0);
    }

    public void createScanners(List<String> lines) {
        scanners = new HashMap<>();
        for (String line : lines) {
            List<Integer> info = Arrays.stream(line.split(": ")).map(Integer::parseInt).collect(Collectors.toList());
            scanners.put(info.get(0), info.get(1));
            if (info.get(0) > largestDepth) {
                largestDepth = info.get(0);
            }
        }
    }
}

package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DiskDefragmentation extends AoCDay {
    public void solve() {
        String testcase = "flqrgnkx";
        String input = "ugkiagan";
        Integer part1 = solutionPart1(input);
        Integer part2 = solutionPart2(input);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));

    }

    public Integer solutionPart1(String inputStr) {
        KnotHash knotHash = new KnotHash();
        Integer count = 0;
        for (int i = 0; i < 128; i++) {
            String value = knotHash.solutionPart2(inputStr + "-" + i).toLowerCase();
            count += countOnes(value);
        }
        return count;
    }

    public Integer solutionPart2(String inputStr) {
        KnotHash knotHash = new KnotHash();
        AtomicInteger count = new AtomicInteger(0);
        char grid[][] = new char[128][128];
        for (int i = 0; i < 128; i++) {
            String value = knotHash.solutionPart2(inputStr + "-" + i).toLowerCase();
            grid[i] = convertToGrid(value).toCharArray();
        }
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                if (grid[i][j] == '1') {
                    count.incrementAndGet();
                    flagCell(grid,i,j);
                }
            }
        }
        return count.get();
    }

    public void flagCell(char[][] grid, int i, int j) {
        if (j < 0 || j > 127 || i < 0 || i > 127 || grid[i][j] != '1') {return;}
        grid[i][j] = 'x';
        flagCell(grid,i,j-1);
        flagCell(grid,i-1,j);
        flagCell(grid,i,j+1);
        flagCell(grid,i+1,j);
    }

    public String convertToGrid(String value) {
        StringBuffer sb = new StringBuffer();
        Map<Character, String> valueMap = new HashMap<>();
        valueMap.put('0',"0000");
        valueMap.put('1',"0001");
        valueMap.put('2',"0010");
        valueMap.put('3',"0011");
        valueMap.put('4',"0100");
        valueMap.put('5',"0101");
        valueMap.put('6',"0110");
        valueMap.put('7',"0111");
        valueMap.put('8',"1000");
        valueMap.put('9',"1001");
        valueMap.put('a',"1010");
        valueMap.put('b',"1011");
        valueMap.put('c',"1100");
        valueMap.put('d',"1101");
        valueMap.put('e',"1110");
        valueMap.put('f',"1111");
        for (char val : value.toCharArray()) {
            sb.append(valueMap.get(val));
        }
        return sb.toString();
    }

    public Integer countOnes(String value) {
        Integer sum = 0;
        Map<Character, Integer> valueMap = new HashMap<>();
        valueMap.put('0',0);
        valueMap.put('1',1);
        valueMap.put('2',1);
        valueMap.put('3',2);
        valueMap.put('4',1);
        valueMap.put('5',2);
        valueMap.put('6',2);
        valueMap.put('7',3);
        valueMap.put('8',1);
        valueMap.put('9',2);
        valueMap.put('a',2);
        valueMap.put('b',3);
        valueMap.put('c',2);
        valueMap.put('d',3);
        valueMap.put('e',3);
        valueMap.put('f',4);
        for (char val : value.toCharArray()) {
            sum += valueMap.get(val);
        }
        return sum;
    }
}

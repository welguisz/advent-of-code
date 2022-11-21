package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

public class LetItSnow extends AoCDay {
    public Integer TargetRow;
    public Integer TargetCol;
    public void solve() {
        Long part1 = solutionPart1(2981,3075);
        System.out.println(String.format("Part 1 Answer: %d",part1));
    }

    public Long solutionPart1(Integer targetRow, Integer targetCol) {
        Long currentNumber = 20151125L;
        Long multiplier = 252533L;
        Long divisor = 33554393L;
        Integer currentRow = 1;
        Integer currentCol = 1;
        Integer maxRow = currentRow;
        while (!(currentRow.equals(targetRow) && currentCol.equals(targetCol))) {
            Integer nextRow = currentRow - 1;
            Integer nextCol = currentCol + 1;
            if ((currentRow.equals(currentCol)) && (currentRow % 10 == 0)) {
                System.out.println("At row " + currentRow + " and column " + currentCol);
            }
            if (nextRow == 0) {
                nextRow = maxRow + 1;
                maxRow = nextRow;
                nextCol = 1;
            }
            currentNumber = (currentNumber * multiplier) % divisor;
            currentRow = nextRow;
            currentCol = nextCol;
        }
        return currentNumber;
    }
}

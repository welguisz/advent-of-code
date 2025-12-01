package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class AoCYear2025 extends AoCYear  {
    AoCDay AOC_DAYS[] = {new AoC2025Day01()};

    public AoCYear2025(int year) { super(year); }

    @Override
    public void runOneDay(int day) {
        runOneDay(year, day, true);
    }

    @Override
    public void runOneDay(int year, int day, boolean printStatements) {
        AoCDay aoCDay = AOC_DAYS[day-1];
        aoCDay.run(printStatements, year, day);
    }

    @Override
    public void getSummary(int day) {
        AOC_DAYS[day-1].printSummary(day);
    }

}

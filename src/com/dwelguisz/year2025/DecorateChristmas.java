package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class DecorateChristmas extends AoCYear  {
    AoCDay AOC_DAYS[] = {new SecretEntrance(), new GiftShop(), new Lobby(),
            new PrintingDepartment(), new Cafeteria(), new TrashCompactor()};

    public DecorateChristmas(int year) { super(year); }

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

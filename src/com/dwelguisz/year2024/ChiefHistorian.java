package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class ChiefHistorian extends AoCYear {

    AoCDay AOC_DAYS[] = {new HistorianHysteria(), new RedNosedReports(), new MullItOver(), new CeresSearch()};

    public ChiefHistorian(int year) {
        super(year);
    }

    @Override
    public void runOneDay(int day) {
        runOneDay(day, true);
    }

    @Override
    public void runOneDay(int day, boolean printStatements) {
        AoCDay aoCDay = AOC_DAYS[day-1];
        aoCDay.run(printStatements);
    }

    @Override
    public void getSummary(int day) {
        AOC_DAYS[day-1].printSummary(day);
    }


}

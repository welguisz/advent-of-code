package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class GlobalSnowProduction extends AoCYear {
    public GlobalSnowProduction(int year) { super(year); }

    @Override
    public void runOneDay(int day) {
        AoCDay aocDays[] = {new Trebuchet(), new CubeConundrum(), new GearRatios(), new Scratchcards(),
                new GiveASeedAFertilizer(), new WaitForIt(), new CamelCards()};
        AoCDay aoCDay = aocDays[day-1];
        aoCDay.run();
    }
}

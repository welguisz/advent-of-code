package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class AoCYear2022 extends AoCYear {
    public AoCYear2022(int year) {
        super(year);
    }

    @Override
    public void runOneDay(int day) {
        AoCDay aocDays[] = {new CalorieCounting()};
        AoCDay aocDay = aocDays[day-1];
        aocDay.solve();
    }
}

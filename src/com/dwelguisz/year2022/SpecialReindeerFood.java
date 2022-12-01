package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class SpecialReindeerFood extends AoCYear {
    public SpecialReindeerFood(int year) {
        super(year);
    }

    @Override
    public void runOneDay(int day) {
        AoCDay aocDays[] = {new CalorieCounting()};
        AoCDay aocDay = aocDays[day-1];
        aocDay.solve();
    }
}

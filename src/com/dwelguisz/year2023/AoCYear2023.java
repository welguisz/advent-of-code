package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class AoCYear2023 extends AoCYear {
    public AoCYear2023(int year) { super(year); }

    @Override
    public void runOneDay(int day) {
        AoCDay aocDays[] = {new Trebuchet(), new AoCDay02()};
        AoCDay aoCDay = aocDays[day-1];
        aoCDay.run();
    }
}

package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class SolarSystem extends AoCYear {
    public SolarSystem(int year) { super (year); }

    @Override
    public void runOneDay(int day) {
        AoCDay aoCDay = new AoCDay();
        switch(day) {
            case 1: {
                aoCDay = new RocketEquation();
                break;
            }
        }
        aoCDay.solve();
    }
}

package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class EasterBunny extends AoCYear {
    public EasterBunny(int year) {
        super(year);
    }

    @Override
    public void runOneDay(int day) {
        AoCDay aocDay = new AoCDay();
        switch (day) {
            case 1: {
                aocDay = new NoTimeForATaxicab();
                break;
            }
            case 2: {
                aocDay = new BathroomSecurity();
                break;
            }
        }
        aocDay.solve();
    }
}
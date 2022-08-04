package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class TemporalAnomalies extends AoCYear {
    public TemporalAnomalies(int year) { super(year); }
    @Override
    public void runOneDay(int day) {
        AoCDay aocDay = new AoCDay();
        switch (day) {
            case 1: {
                aocDay = new ChronalCalibration();
                break;
            }
        }
        aocDay.solve();
    }
}
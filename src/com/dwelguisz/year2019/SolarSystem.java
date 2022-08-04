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
            case 2: {
                aoCDay = new ProgramAlarm();
                break;
            }
            case 3: {
                aoCDay = new CrossedWires();
                break;
            }
            case 4: {
                aoCDay = new SecureContainer();
                break;
            }
            case 5: {
                aoCDay = new ChanceOfAsteriods();
                break;
            }
        }
        aoCDay.solve();
    }
}

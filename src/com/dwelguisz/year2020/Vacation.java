package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class Vacation extends AoCYear {
    public Vacation(int year) {
        super(year);
    }

    @Override
    public void runOneDay(int day) {
        AoCDay aocDay = new AoCDay();
        switch(day) {
            case 1: {
                aocDay = new ReportRepair();
                break;
            }
            case 2: {
                aocDay = new PasswordPhilosophy();
                break;
            }
            case 3: {
                aocDay = new TobogganTrajectory();
                break;
            }
            case 4: {
                aocDay = new PassportProcessing();
                break;
            }
            case 5: {
                aocDay = new BinaryBoarding();
                break;
            }
            case 6: {
                aocDay = new CustomCustoms();
                break;
            }
            case 7: {
                aocDay = new HandyHaversacks();
                break;
            }
        }
        aocDay.solve();
    }
}

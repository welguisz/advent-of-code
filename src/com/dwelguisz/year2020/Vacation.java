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
            case 8: {
                aocDay = new HandheldHalting();
                break;
            }
            case 9: {
                aocDay = new EncodingError();
                break;
            }
            case 10: {
                aocDay = new AdapterArray();
                break;
            }
            case 11: {
                aocDay = new SeatingSystem();
                break;
            }
            case 12: {
                aocDay = new RainRisk();
                break;
            }
        }
        aocDay.solve();
    }
}

package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class RetrieveTheKeys extends AoCYear {

    public RetrieveTheKeys(int year) {
        super(year);
    }

    @Override
    public void runOneDay(int day) {
        AoCDay aoCDay = new AoCDay();
        switch(day) {
            case 1: {
                aoCDay = new SonarSweep();
                break;
                }
            case 2: {
                aoCDay = new Dive();
                break;
            }
            case 3: {
                aoCDay = new BinaryDiagnostic();
                break;
            }
            case 4 : {
                aoCDay = new GiantSquid();
                break;
            }
            case 5: {
                aoCDay = new HydrothermalVenture();
                break;
            }
            case 6 : {
                aoCDay = new LanternFish();
                break;
            }
        }
        aoCDay.solve();
    }
}

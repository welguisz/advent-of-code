package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class NaughtyOrNiceList extends AoCYear {
    public NaughtyOrNiceList(int year) {
        super(year);
    }

    @Override
    public void runOneDay(int day) {
        AoCDay aocDay = new AoCDay();
        switch (day) {
            case 1: {
                aocDay = new InverseCaptcha();
                break;
            }
            case 2: {
                aocDay = new CorruptionChecksum();
                break;
            }
            case 3: {
                aocDay = new SpiralMemory();
                break;
            }
        }
        aocDay.solve();
    }
}

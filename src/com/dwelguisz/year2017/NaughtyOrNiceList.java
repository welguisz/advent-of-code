package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class NaughtyOrNiceList extends AoCYear {
    public NaughtyOrNiceList(int year) {
        super(year);
    }

    @Override
    public void runOneDay(int day) {
        AoCDay aocDays[] = {new InverseCaptcha(), new CorruptionChecksum(), new SpiralMemory(),
            new HighEntropyPassphrases(), new TwistyTrampolines(), new MemoryReallocation()};
        AoCDay aocDay = aocDays[day-1];
        aocDay.solve();
    }
}

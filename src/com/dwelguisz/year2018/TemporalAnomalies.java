package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class TemporalAnomalies extends AoCYear {
    public TemporalAnomalies(int year) { super(year); }
    @Override
    public void runOneDay(int day) {
        AoCDay days[] = {new ChronalCalibration(), new InventoryManagementSystem(), new HowYouSliceIt(), new ReposeRecord()};
        AoCDay aocDay = days[day];
        aocDay.solve();
    }
}

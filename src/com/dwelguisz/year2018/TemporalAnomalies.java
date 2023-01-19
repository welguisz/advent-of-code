package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class TemporalAnomalies extends AoCYear {
    public TemporalAnomalies(int year) { super(year); }
    @Override
    public void runOneDay(int day) {
        AoCDay days[] = {
                new ChronalCalibration(), new InventoryManagementSystem(), new HowYouSliceIt(), new ReposeRecord(),
                new AlchemicalReduction(), new ChronalCoordinates(), new TheSumOfItsParts(), new MemoryManeuver(),
                new MarbleMania(), new TheStarsAlign(), new ChronalCharge(), new SubterraneanSustainability(),
                new MineCartMadness(), new ChocolateCharts(), new BeverageBandits(), new ChronalClassification(),
                new ReserviorResearch()
        };
        AoCDay aocDay = days[day-1];
        aocDay.solve();
    }
}

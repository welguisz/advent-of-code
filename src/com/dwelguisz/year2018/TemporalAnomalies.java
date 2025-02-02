package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class TemporalAnomalies extends AoCYear {
    AoCDay AOC_DAYS[] = {
            new ChronalCalibration(), new InventoryManagementSystem(), new HowYouSliceIt(), new ReposeRecord(),
            new AlchemicalReduction(), new ChronalCoordinates(), new TheSumOfItsParts(), new MemoryManeuver(),
            new MarbleMania(), new TheStarsAlign(), new ChronalCharge(), new SubterraneanSustainability(),
            new MineCartMadness(), new ChocolateCharts(), new BeverageBandits(), new ChronalClassification(),
            new ReserviorResearch(), new SettlersOfTheNorthPole(), new GoWithTheFlow(), new ARegularMap(),
            new ChronalConversion(), new ModeMaze(), new ExperimentalEmergencyTeleportation(),
            new ImmuneSystemSimulator20XX(), new FourDimensionalAdventure()
    };


    public TemporalAnomalies(int year) { super(year); }
    @Override
    public void runOneDay(int day) {
        AoCDay aocDay = AOC_DAYS[day-1];
        aocDay.run();
    }

    @Override
    public void runOneDay(int day, boolean printStatements) {
        AoCDay aoCDay = AOC_DAYS[day-1];
        aoCDay.run(printStatements);
    }

    @Override
    public void getSummary(int day) {
        AOC_DAYS[day-1].printSummary(day);
    }

}

package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class SpecialReindeerFood extends AoCYear {
    public SpecialReindeerFood(int year) {
        super(year);
    }

    @Override
    public void runOneDay(int day) {
        AoCDay aocDays[] = {new CalorieCounting(), new RockPaperScissors(), new RucksackReorganization(),
                new CampCleanup(), new SupplyStacks(), new TuningTrouble(), new NoSpaceLeftOnDevice(),
                new TreetopTreeHouse(), new RopeBridge(), new CathodeRayTube(), new MonkeyInTheMiddle(),
                new HillClimbingAlgorithm(), new DistressSignal(), new RegolithReservoir(), new BeaconExclusionZone(),
                new AoC2022Day16()
        };
        AoCDay aocDay = aocDays[day-1];
        aocDay.solve();
    }
}

package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class SpecialReindeerFood extends AoCYear {
    AoCDay AOC_DAYS[] = {new CalorieCounting(), new RockPaperScissors(), new RucksackReorganization(),
            new CampCleanup(), new SupplyStacks(), new TuningTrouble(), new NoSpaceLeftOnDevice(),
            new TreetopTreeHouse(), new RopeBridge(), new CathodeRayTube(), new MonkeyInTheMiddle(),
            new HillClimbingAlgorithm(), new DistressSignal(), new RegolithReservoir(), new BeaconExclusionZone(),
            new ProboscideaVolcanium(), new PyroclasticFlow(), new BoilingBoulders(), new NotEnoughMinerals(),
            new GrovePositioningSystem(), new MonkeyMath(), new MonkeyMap(), new UnstableDiffusion(),
            new BlizzardBasin(), new FullOfHotAir()
    };

    public SpecialReindeerFood(int year) {
        super(year);
    }

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

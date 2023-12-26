package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class GlobalSnowProduction extends AoCYear {
    AoCDay AOC_DAYS[] = {new Trebuchet(), new CubeConundrum(), new GearRatios(), new Scratchcards(),
            new GiveASeedAFertilizer(), new WaitForIt(), new CamelCards(), new HauntedWasteland(),
            new MirageMaintenance(), new PipeMaze(), new CosmicExpansion(), new HotSprings(),
            new PointOfIncidence(), new ParabolicReflectorDish(), new LensLibrary(), new TheFloorWillBeLava(),
            new ClumsyCrucible(), new LavaductLagoon(), new APlenty(), new PulsePropagation(),
            new StepCounter(), new SandSlabs(), new ALongWalk(), new NeverTellMeTheOdds(), new AoC2023Day25()};

    public GlobalSnowProduction(int year) {
        super(year);
    }

    @Override
    public void runOneDay(int day) {
        runOneDay(day, true);
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

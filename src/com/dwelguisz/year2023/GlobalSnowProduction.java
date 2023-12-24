package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class GlobalSnowProduction extends AoCYear {
    public GlobalSnowProduction(int year) { super(year); }

    @Override
    public void runOneDay(int day) {
        AoCDay aocDays[] = {new Trebuchet(), new CubeConundrum(), new GearRatios(), new Scratchcards(),
                new GiveASeedAFertilizer(), new WaitForIt(), new CamelCards(), new HauntedWasteland(),
                new MirageMaintenance(), new PipeMaze(), new CosmicExpansion(), new HotSprings(),
                new PointOfIncidence(), new ParabolicReflectorDish(), new LensLibrary(), new TheFloorWillBeLava(),
                new ClumsyCrucible(), new LavaductLagoon(), new APlenty(), new PulsePropagation(),
                new StepCounter(), new SandSlabs(), new ALongWalk(), new AoC2023Day24()};
        AoCDay aoCDay = aocDays[day-1];
        aoCDay.run();
    }
}

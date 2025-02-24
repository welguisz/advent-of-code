package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class SolarSystem extends AoCYear {
    private static AoCDay AOC_DAYS[] = {new RocketEquation(), new ProgramAlarm(), new CrossedWires(), new SecureContainer(),
            new ChanceOfAsteriods(), new UniversalOrbitMap(), new AmplicationCircuit(), new SpaceImageFormat(),
            new SensorBoost(), new MonitoringStation(), new SpacePolice(), new TheNBodyProblem(),
            new CarePackage(), new SpaceStoichiometry(), new OxygenSystem(), new FlawedFrequencyTransmission(),
            new SetAndForget(), new ManyWorldsInterpretation(), new TractorBeam(), new DonutMaze(),
            new SpringdroidAdventure(), new SlamShuffle(), new CategorySix(), new PlanetOfDiscord(),
            new Cryostatis()
    };

    public SolarSystem(int year) { super (year); }

    @Override
    public void runOneDay(int day) {
        AoCDay aocDay = AOC_DAYS[day-1];
        aocDay.run();
    }

    @Override
    public void runOneDay(int year, int day, boolean printStatements) {
        AoCDay aoCDay = AOC_DAYS[day-1];
        aoCDay.run(printStatements, year, day);
    }

    @Override
    public void getSummary(int day) {
        AOC_DAYS[day-1].printSummary(day);
    }
}

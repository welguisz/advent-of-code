package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class SolarSystem extends AoCYear {
    public SolarSystem(int year) { super (year); }

    @Override
    public void runOneDay(int day) {
        AoCDay aocDays[] = {new RocketEquation(), new ProgramAlarm(), new CrossedWires(), new SecureContainer(),
                new ChanceOfAsteriods(), new UniversalOrbitMap(), new AmplicationCircuit(), new SpaceImageFormat(),
                new SensorBoost(), new MonitoringStation(), new SpacePolice(), new TheNBodyProblem(),
                new CarePackage(), new SpaceStoichiometry(), new OxygenSystem()
        };
        AoCDay aocDay = aocDays[day-1];
        aocDay.solve();
    }
}

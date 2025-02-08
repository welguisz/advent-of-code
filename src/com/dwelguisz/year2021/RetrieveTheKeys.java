package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class RetrieveTheKeys extends AoCYear {
    AoCDay[] AOC_DAYS = new AoCDay[] {new SonarSweep(), new Dive(), new BinaryDiagnostic(), new GiantSquid(),
            new HydrothermalVenture(), new LanternFish(), new TreacheryOfWhales(), new SevenSegmentSearch(),
            new SmokeBasin(), new SyntaxScoring(), new DumboOctopus(), new PassagePathing(),
            new TransparentOrigami(), new ExtendedPolymerization(), new Chiton(), new PacketDecoder(),
            new AdventDay17(), new Snailfish(), new BeaconScanner(), new TrenchMap(), new DiracDice(),
            new ReactorReboot(), new AdventDay23(), new AdventDay24(), new SeaCucumber()};


    public RetrieveTheKeys(int year) {
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

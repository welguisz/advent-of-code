package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class RetrieveTheKeys extends AoCYear {

    public RetrieveTheKeys(int year) {
        super(year);
    }

    @Override
    public void runOneDay(int day) {
        AoCDay[] aoCDays = new AoCDay[] {new SonarSweep(), new Dive(), new BinaryDiagnostic(), new GiantSquid(),
                new HydrothermalVenture(), new LanternFish(), new TreacheryOfWhales(), new SevenSegmentSearch(),
                new SmokeBasin(), new SyntaxScoring(), new DumboOctopus(), new PassagePathing(),
                new TransparentOrigami(), new ExtendedPolymerization(), new Chiton(), new PacketDecoder(),
                new Snailfish(), new BeaconScanner(), new TrenchMap(), new DiracDice(), new ReactorReboot(),
                new SeaCucumber()};
        AoCDay aoCDay = aoCDays[day-1];
        aoCDay.run();
    }
}

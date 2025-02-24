package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class NaughtyOrNiceList extends AoCYear {
    AoCDay AOC_DAYS[] = {new InverseCaptcha(), new CorruptionChecksum(), new SpiralMemory(),
            new HighEntropyPassphrases(), new TwistyTrampolines(), new MemoryReallocation(),
            new RecursiveCircus(), new HeardYouLikeRegisters(), new StreamProcessing(),
            new KnotHash(), new HexEd(), new DigitalPlumber(), new PacketScanners(),
            new DiskDefragmentation(), new DuelingGenerators(), new PermutationPromenade(),
            new SpinLock(), new Duet(), new ASeriesOfTubes(), new ParticleSwarm(), new FractalArt(),
            new SporificaVirus(), new CoprocessorConflagration(), new ElectromagneticMoat(),
            new TheHaltingProblem()
    };


    public NaughtyOrNiceList(int year) {
        super(year);
    }

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

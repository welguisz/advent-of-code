package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class NaughtyOrNiceList extends AoCYear {
    public NaughtyOrNiceList(int year) {
        super(year);
    }

    @Override
    public void runOneDay(int day) {
        AoCDay aocDays[] = {new InverseCaptcha(), new CorruptionChecksum(), new SpiralMemory(),
                new HighEntropyPassphrases(), new TwistyTrampolines(), new MemoryReallocation(),
                new RecursiveCircus(), new HeardYouLikeRegisters(), new StreamProcessing(),
                new KnotHash(), new HexEd(), new DigitalPlumber(), new PacketScanners(),
                new DiskDefragmentation(), new DuelingGenerators(), new PermutationPromenade(),
                new SpinLock(), new Duet(), new ASeriesOfTubes(), new ParticleSwarm(), new FractalArt(),
                new SporificaVirus()
        };
        AoCDay aocDay = aocDays[day-1];
        aocDay.solve();
    }
}

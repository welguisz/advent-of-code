package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

import javax.print.Doc;

public class Vacation extends AoCYear {
    AoCDay AOC_DAYS[] = { new ReportRepair(), new PasswordPhilosophy(), new TobogganTrajectory(),
            new PassportProcessing(), new BinaryBoarding(), new CustomCustoms(), new HandyHaversacks(),
            new HandheldHalting(), new EncodingError(), new AdapterArray(), new SeatingSystem(),
            new RainRisk(), new ShuttleSearch(), new DockingData(), new RambunctiousRecitation(),
            new TicketTranslation(), new ConwayCubes(), new OperationOrder(), new MonsterMessages(),
            new JurassicJigsaw(), new AllergenAssessment(), new CrabCombat(), new CrabCups(),
            new LobbyLayout(), new ComboBreaker()
    };


    public Vacation(int year) {
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

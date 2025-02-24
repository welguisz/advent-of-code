package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class WeatherMachine extends AoCYear {
    AoCDay AOC_DAYS[] = {new NotQuiteLisp(), new IWasToldThereWouldBeNoMath(),
            new PerfectlySphericalHousesInAVacuum(), new TheIdealStockingStuffer(),
            new DoesntHeHaveInternElvesForThis(), new ProbablyAFireHazard(), new SomeAssemblyRequired(),
            new Matchsticks(), new AllInASingleNight(), new ElvesLookElvesSay(), new CorporatePolicy(),
            new JSAbacusFramework(), new KnightsOfTheDinnerTable(), new ReindeerOlympics(),
            new ScienceForHungryPeople(), new AuntSue(), new NoSuchThingasTooMuch(), new LikeAGIFForYourYard(),
            new MedicineForRudolph(), new InfiniteElvesAndInfiniteHouses(), new RPGSimulator20XX(),
            new WizardSimulator20XX(), new OpeningTheTuringLock(), new ItHangsInTheBalance(),
            new LetItSnow()};

    public WeatherMachine(int year) { super(year);}

    @Override
    public void runOneDay(int day) {
        AoCDay aocDay = AOC_DAYS[day-1];
        aocDay.run(true, year, day);
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

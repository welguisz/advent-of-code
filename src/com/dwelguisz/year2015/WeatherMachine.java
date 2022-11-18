package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class WeatherMachine extends AoCYear {
    public WeatherMachine(int year) { super(year);}

    @Override
    public void runOneDay(int day) {
        AoCDay aocDays[] = {new NotQuiteLisp(), new IWasToldThereWouldBeNoMath(),
                new PerfectlySphericalHousesInAVacuum(), new TheIdealStockingStuffer(),
                new DoesntHeHaveInternElvesForThis(), new ProbablyAFireHazard(), new SomeAssemblyRequired(),
                new Matchsticks(), new AllInASingleNight(), new ElvesLookElvesSay(), new CorporatePolicy(),
                new JSAbacusFramework(), new KnightsOfTheDinnerTable(), new ReindeerOlympics(),
                new ScienceForHungryPeople(), new AuntSue(), new NoSuchThingasTooMuch(), new LikeAGIFForYourYard(),
                new MedicineForRudolph()};
        AoCDay aocDay = aocDays[day-1];
        aocDay.solve();

    }
}

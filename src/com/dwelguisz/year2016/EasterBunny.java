package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;
import org.checkerframework.checker.units.qual.A;

public class EasterBunny extends AoCYear {
    public EasterBunny(int year) {
        super(year);
    }

    AoCDay[] AOC_DAYS = {new NoTimeForATaxicab(), new BathroomSecurity(), new SquareWithThreeSides(),
            new SecurityThroughObscurity(), new NiceGameOfChess(), new SignalsAndNoise(),
            new InternetProtocolVersion7(), new TwoFactorAuthentication(), new ExplosiveInCyberspace(),
            new BalanceBots(), new RadioisotepeThermoelectricGenerators(), new LeonardoMonorail(),
            new MazeOfTwistyLittleCubicles(), new OneTimePad(), new TimingIsEverything(),
            new DragonChecksum(), new TwoStepsForward(), new LikeARogue(), new AnElephantNamedJoseph(),
            new FirewallRules(), new ScrambledLettersAndHash(), new GridComputing(),
            new SafeCracking(), new AirDuctSpleunking(), new ClockSignal()};

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
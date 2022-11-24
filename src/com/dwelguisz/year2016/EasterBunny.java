package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class EasterBunny extends AoCYear {
    public EasterBunny(int year) {
        super(year);
    }

    @Override
    public void runOneDay(int day) {
        AoCDay aocDays[] = {new NoTimeForATaxicab(), new BathroomSecurity(), new SquareWithThreeSides(),
                new SecurityThroughObscurity(), new NiceGameOfChess(), new SignalsAndNoise(),
                new InternetProtocolVersion7(), new TwoFactorAuthentication(), new ExplosiveInCyberspace(),
                new BalanceBots(), new RadioisotepeThermoelectricGenerators(), new LeonardoMonorail()};
        AoCDay aocDay = aocDays[day-1];
        aocDay.solve();
    }
}
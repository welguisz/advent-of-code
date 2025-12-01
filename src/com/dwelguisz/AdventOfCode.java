package com.dwelguisz;

import com.dwelguisz.base.AoCYear;
import com.dwelguisz.year2015.WeatherMachine;
import com.dwelguisz.year2016.EasterBunny;
import com.dwelguisz.year2017.NaughtyOrNiceList;
import com.dwelguisz.year2018.TemporalAnomalies;
import com.dwelguisz.year2019.SolarSystem;
import com.dwelguisz.year2020.Vacation;
import com.dwelguisz.year2021.RetrieveTheKeys;
import com.dwelguisz.year2022.SpecialReindeerFood;
import com.dwelguisz.year2023.GlobalSnowProduction;
import com.dwelguisz.year2024.ChiefHistorian;
import com.dwelguisz.year2025.DecorateChristmas;

import static java.lang.Integer.parseInt;

public class AdventOfCode {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("expected input:  <year> <day>, e.g. 2021 1");
            return;
        }
        Integer year = parseInt(args[0]);
        Integer day = parseInt(args[1]);
        AoCYear aocYears[] = {
                new WeatherMachine(year), new EasterBunny(year), new NaughtyOrNiceList(year),
                new TemporalAnomalies(year), new SolarSystem(year), new Vacation(year),
                new RetrieveTheKeys(year), new SpecialReindeerFood(year), new GlobalSnowProduction(year),
                new ChiefHistorian(year), new DecorateChristmas(year)
        };
        AoCYear aoCYear = aocYears[year - 2015];
        if (day > 0) {
            aoCYear.runOneDay(day);
        } else {
            aoCYear.runAllDays();
        }
    }
}

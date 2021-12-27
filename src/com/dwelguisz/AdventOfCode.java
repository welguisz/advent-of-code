package com.dwelguisz;

import com.dwelguisz.base.AoCYear;
import com.dwelguisz.year2021.RetrieveTheKeys;

import static java.lang.Integer.parseInt;

public class AdventOfCode {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("expected input:  <year> <day>, e.g. 2021 1");
            return;
        }
        AoCYear aoCYear = new AoCYear(-1);
        Integer year = parseInt(args[0]);
        Integer day = parseInt(args[1]);
        switch(year) {
            case 2021 : aoCYear = new RetrieveTheKeys(2021);
        }
        if (day > 0) {
            aoCYear.runOneDay(day);
        } else {
            aoCYear.runAllDays();
        }
    }
}

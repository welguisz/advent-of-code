package com.dwelguisz.base;

public class AoCYear {
    public int year;
    public String[] summaries;
    public AoCYear(int year) {
        this.year = year;
        this.summaries = new String[25];
    }

    public void runAllDays() {
        System.out.println(String.format("%4s|%25s |%20s |%20s |%20s |%20s |%20s |",
                "Day #", "Puzzle name", "Parsing Time","Part 1 Time(ms)", "Part 2 Time(ms)",
                "Part 1 Answer", "Part 2 Answer"));

        for (int i = 1; i <= 25; i++) {
            runOneDay(i, false);
            getSummary(i);
        }
    }


    public void runOneDay(int day) {
        runOneDay(day, true);
    }

    public void runOneDay(int day, boolean printStatements) {
        System.out.println(String.format("Day %d for year %d has not been written", day, year));

    }

    public void getSummary(int day) {
        System.out.println("Not yet implemented");
    }

}

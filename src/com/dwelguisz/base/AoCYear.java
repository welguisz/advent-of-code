package com.dwelguisz.base;

public class AoCYear {
    public int year;

    public AoCYear(int year) {
        this.year = year;
    }

    public void runAllDays() {
        for (int i = 1; i <= 25; i++) {
            runOneDay(i);
        }
    }


    public void runOneDay(int day) {
        System.out.println(String.format("Day %d for year %d has not been written", day, year));
    }
}

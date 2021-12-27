package com.dwelguisz.base;

public class AoCYear {
    public int year;

    public AoCYear(int year) {
        this.year = year;
    }

    public void runOneDay(int day) {
        System.out.println(String.format("Day %d for year %d has not been written", day, year));
    }
}

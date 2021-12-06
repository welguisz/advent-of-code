package com.dwelguisz.year2021.helper.day5;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class Point {
    int x;
    int y;

    public Point(String val) {
        List<String> splitValues =Arrays.stream(val.split(",")).collect(Collectors.toList());
        x = parseInt(splitValues.get(0));
        y = parseInt(splitValues.get(1));
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }




}

package com.dwelguisz.year2021.helper.day5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Line {
    Point first;
    Point second;

    public Line(String value) {
        List<String> points = Arrays.stream(value.split(" -> ")).collect(Collectors.toList());
        first = new Point(points.get(0));
        second = new Point(points.get(1));
    }

    public List<Point> drawLine(boolean diagonal) {
        if (first.getX() == second.getX()) {
            List<Point> points = new ArrayList<>();
            int min = Math.min(first.getY(), second.getY());
            int max = Math.max(first.getY(), second.getY());
            for (int i = min; i <= max; i++) {
                points.add(new Point(first.getX(), i));
            }
            return points;
        } else if(first.getY() == second.getY()) {
            List<Point> points = new ArrayList<>();
            int min = Math.min(first.getX(), second.getX());
            int max = Math.max(first.getX(), second.getX());
            for (int i = min; i <= max; i++) {
                points.add(new Point(i, first.getY()));
            }
            return points;
        }
        List<Point> points = new ArrayList<>();
        if (diagonal) {
            int numberOfPointsX = Math.abs(first.getY() - second.getY());
            int numberOfPointsY = Math.abs(first.getX() - second.getX());
            if (numberOfPointsX == numberOfPointsY) {
                int stepX = (first.getX() > second.getX()) ? -1 : 1;
                int stepY = (first.getY() > second.getY()) ? -1 : 1;
                int currentX = first.getX();
                int currentY = first.getY();
                while (numberOfPointsX >= 0) {
                    points.add(new Point(currentX, currentY));
                    currentX += stepX;
                    currentY += stepY;
                    numberOfPointsX--;
                }
            }
        }
        return points;
    }
}

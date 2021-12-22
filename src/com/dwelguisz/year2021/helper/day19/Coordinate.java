package com.dwelguisz.year2021.helper.day19;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode
public class Coordinate {
    public int x, y, z;
    public List<Long> relativeDistances;

    public Coordinate(String line) {
        String[] split = line.split(",");
        x = Integer.parseInt(split[0]);
        y = Integer.parseInt(split[1]);
        z = Integer.parseInt(split[2]);
        relativeDistances = new ArrayList<>();
    }

    public Coordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        relativeDistances = new ArrayList<>();
    }

    public int manhattanDistance(Coordinate coordinate) {
        return Math.abs(x - coordinate.x) + Math.abs(y - coordinate.y) + Math.abs(z - coordinate.z);
    }

    public void addDistance(Coordinate coordinate) {
        Long distance = Double.valueOf(Math.pow(coordinate.x - x, 2) +
                        Math.pow(coordinate.y - y, 2) +
                        Math.pow(coordinate.z - z, 2)).longValue();
        relativeDistances.add(distance);
    }

    public Coordinate determineNewPosition(Coordinate coordinate) {
        return new Coordinate(coordinate.x - x, coordinate.y - y, coordinate.z - z);
    }

    public List<Long> getRelativeDistances() {
        return relativeDistances;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "[x=" + x +
                ", y=" + y +
                ", z=" + z +
                ']';
    }
}

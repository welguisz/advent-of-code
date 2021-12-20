package com.dwelguisz.year2021.helper.day19;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
public class Coordinate {
    public Integer x;
    public Integer y;
    public Integer z;
    public List<Double> relativeDistances;

    public Coordinate(Integer x, Integer y, Integer z) {
        this.x = x;
        this.y = y;
        this.z = z;
        relativeDistances = new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format("%d,%d,%d", x,y,z);
    }

    public Coordinate subtract(Coordinate testBeaconB) {
        Integer newX = this.x - testBeaconB.x;
        Integer newY = this.y - testBeaconB.y;
        Integer newZ = this.z - testBeaconB.z;
        return new Coordinate(newX, newY, newZ);
    }

    public Integer getValue(Integer i) {
        Integer[] temp = new Integer[]{x,y,z};
        return temp[i];
    }

    public Coordinate add(Coordinate testBeaconB) {
        Integer newX = this.x + testBeaconB.x;
        Integer newY = this.y + testBeaconB.y;
        Integer newZ = this.z + testBeaconB.z;
        return new Coordinate(newX, newY, newZ);
    }

    public void addDistance(Coordinate c) {
        Double distance = Math.sqrt(Math.pow(x-c.x,2)+Math.pow(y-c.y,2)+Math.pow(z-c.z,2));
        relativeDistances.add(distance);
    }

    public List<Double> getRelativeDistance() {
        return relativeDistances;
    }

    public Coordinate determineNewPosition(Coordinate c) {
        return new Coordinate(c.x-x,c.y-y,c.z-z);
    }
}

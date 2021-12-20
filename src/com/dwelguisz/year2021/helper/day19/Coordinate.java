package com.dwelguisz.year2021.helper.day19;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Coordinate {
    public Integer x;
    public Integer y;
    public Integer z;

    public Coordinate(Integer x, Integer y, Integer z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public Coordinate add(Coordinate testBeaconB) {
        Integer newX = this.x + testBeaconB.x;
        Integer newY = this.y + testBeaconB.y;
        Integer newZ = this.z + testBeaconB.z;
        return new Coordinate(newX, newY, newZ);
    }

}

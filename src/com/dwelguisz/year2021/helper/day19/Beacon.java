package com.dwelguisz.year2021.helper.day19;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Beacon {
    private Integer originalX;
    private Integer originalY;
    private Integer originalZ;

    public Beacon(Integer x, Integer y, Integer z) {
        originalX = x;
        originalY = y;
        originalZ = z;
    }

    @Override
    public String toString() {
        return String.format("%d,%d,%d", originalX, originalY, originalZ);
    }

    public Integer getX() {
        return originalX;
    }

    public Integer getY() {
        return originalY;
    }

    public Integer getZ() {
        return originalZ;
    }

    public Beacon diffBeacon(Beacon testBeaconB) {
        Integer newX = this.originalX - testBeaconB.getX();
        Integer newY = this.originalY - testBeaconB.getY();
        Integer newZ = this.originalZ - testBeaconB.getZ();
        return new Beacon(newX, newY, newZ);
    }

    public Beacon addBeacon( Beacon testBeaconB) {
        Integer newX = this.originalX + testBeaconB.getX();
        Integer newY = this.originalY + testBeaconB.getY();
        Integer newZ = this.originalZ + testBeaconB.getZ();
        return new Beacon(newX, newY, newZ);
    }

}

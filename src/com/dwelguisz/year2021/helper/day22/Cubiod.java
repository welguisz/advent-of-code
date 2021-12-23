package com.dwelguisz.year2021.helper.day22;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class Cubiod {
    public boolean action;
    public Long xMin;
    public Long xMax;
    public Long yMin;
    public Long yMax;
    public Long zMin;
    public Long zMax;

    public Cubiod(String line) {
        String actionStr = line.split(" ")[0];
        action = actionStr.equals("on");
        String[] coords = line.split(" ")[1].split(",");
        String xcoords = coords[0].split("=")[1];
        String ycoords = coords[1].split("=")[1];
        String zcoords = coords[2].split("=")[1];
        xMin = parseLong(xcoords.split("\\.\\.")[0]);
        yMin = parseLong(ycoords.split("\\.\\.")[0]);
        zMin = parseLong(zcoords.split("\\.\\.")[0]);
        xMax = parseLong(xcoords.split("\\.\\.")[1]) + 1;
        yMax = parseLong(ycoords.split("\\.\\.")[1]) + 1;
        zMax = parseLong(zcoords.split("\\.\\.")[1]) + 1;
    }

    public Cubiod(Boolean action, Long xMin, Long xMax, Long yMin, Long yMax, Long zMin, Long zMax) {
        this.action = action;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;
    }

    public Cubiod intersection(Cubiod other) {
        if (!doesIntersect(other)) {
            return null;
        }
        return new Cubiod(this.action,
                Math.max(this.xMin, other.xMin), Math.min(this.xMax, other.xMax),
                Math.max(this.yMin, other.yMin), Math.min(this.yMax, other.yMax),
                Math.max(this.zMin, other.zMin), Math.min(this.zMax, other.zMax));
    }

    public boolean doesIntersect(Cubiod other) {
        return (this.xMin < other.xMax) && (other.xMin < this.xMax) // check x-axis
                && (this.yMin < other.yMax) && (other.yMin < this.yMax) // check y-axis
                && (this.zMin < other.zMax) && (other.zMin < this.zMax); //check z-axis
    }

    public Long volume() {
        return ((this.xMax - this.xMin) * (this.yMax - this.yMin) * (this.zMax - this.zMin));
    }

    public String toString() {
        return String.format("Cubiod(%b,%d,%d,%d,%d,%d,%d)",action, xMin, xMax, yMin, yMax, zMin, zMax);
    }
}

package com.dwelguisz.utilities;

import java.util.Objects;

public class Coord4D {
    final public int x;
    final public int y;
    final public int z;
    final public int t;
    private int hashCode;

    public Coord4D(int x, int y, int z, int t) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.t = t;
        this.hashCode = Objects.hash(x, y, z, t);
    }

    public Coord4D(Coord4D prev) {
        this.x = prev.x;
        this.y = prev.y;
        this.z = prev.z;
        this.t = prev.t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null | getClass() != o.getClass()) return false;
        Coord4D other = (Coord4D) o;
        return (this.x == other.x) && (this.y == other.y) && (this.z == other.z) && (this.t == other.t);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    public Integer manhattanDistance(Coord4D other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y) + Math.abs(this.z - other.z) + Math.abs(this.t - other.t);
    }

    public String toString() {
        return String.format("[%d,%d,%d,%d]",x,y,z,t);
    }
}

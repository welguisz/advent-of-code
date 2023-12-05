package com.dwelguisz.utilities;

import java.util.Objects;

public class Coord3DLong {
    final public long x;
    final public long y;
    final public long z;
    private Integer hashCode;

    public Coord3DLong(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.hashCode = Objects.hash(x, y, z);
    }

    public Coord3DLong(Coord3DLong prev) {
        this.x = prev.x;
        this.y = prev.y;
        this.z = prev.z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null | getClass() != o.getClass()) return false;
        Coord3DLong other = (Coord3DLong) o;
        return (this.x == other.x) && (this.y == other.y) && (this.z == other.z);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    public Long manhattanDistance(Coord3DLong other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y) + Math.abs(this.z - other.z);
    }

    public String toString() {
        return "["+x+","+y+","+z+"]";
    }

}

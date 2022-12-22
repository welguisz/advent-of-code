package com.dwelguisz.utilities;

import java.util.Objects;

public class Coord3D {
    final public int x;
    final public int y;
    final public int z;
    private int hashCode;

    public Coord3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.hashCode = Objects.hash(x, y, z);
    }

    public Coord3D(Coord3D prev) {
        this.x = prev.x;
        this.y = prev.y;
        this.z = prev.z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null | getClass() != o.getClass());
        Coord3D other = (Coord3D) o;
        return (this.x == other.x) && (this.y == other.y) && (this.z == other.z);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    public Integer manhattanDistance(Coord3D other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y) + Math.abs(this.z - other.z);
    }

}

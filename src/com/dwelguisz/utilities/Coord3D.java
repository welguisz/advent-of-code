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


}

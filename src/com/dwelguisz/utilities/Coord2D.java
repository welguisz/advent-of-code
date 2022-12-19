package com.dwelguisz.utilities;

import java.util.Objects;

public class Coord2D {
    public final int x;
    public final int y;
    private int hashCode;

    public Coord2D(int x, int y) {
        this.x = x;
        this.y = y;
        this.hashCode = Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null | getClass() != o.getClass());
        Coord2D other = (Coord2D) o;
        return (this.x == other.x) && (this.y == other.y);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

}

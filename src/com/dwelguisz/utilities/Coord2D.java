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

    public Integer manhattanDistance(Coord2D other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    public Coord2D slope(Coord2D other) {
        int run = other.x - this.x;
        int rise = other.y - this.y;
        int scaleRun = run < 0 ? -1 : 1;
        int scaleRise = rise < 0 ? -1 : 1;
        int gcd = gcd(scaleRun * run, scaleRise * rise);
        return new Coord2D(run/gcd, rise/gcd);
    }

    public String toString() {
        return "["+x+","+y+"]";
    }

    public static int gcd(Integer n1, Integer n2) {
        if (n1 == 0) {
            return n2;
        } else if (n2 == 0) {
            return n1;
        }
        int i1 = n1;
        int i2 = n2;
        while (i2 != 0) {
            int tmp = i1;
            i1 = i2;
            i2 = tmp % i2;
        }
        return i1;
    }

    public static Coord2D add(Coord2D a, Coord2D b) {
        return new Coord2D(a.x + b.x, a.y+b.y);
    }

    public Coord2D multiply(Integer t) {
        return new Coord2D(t*x, t*y);
    }

    public Coord2D modulus(Coord2D modulo) {
        return new Coord2D(x % modulo.x, y % modulo.y);
    }

    public Coord2D add(Coord2D o) {
        return add(this, o);
    }

    public Double getAngle(Coord2D other) {
        double angle = 90-Math.toDegrees(Math.atan2(y-other.y,other.x-x));
        return angle < 0 ? angle+360 : angle;
    }

}

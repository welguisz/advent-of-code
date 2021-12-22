package com.dwelguisz.year2021.helper;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Tuple<X, Y> {
    public final X x;
    public final Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public Tuple(Tuple<X, Y> tuple) {
        this.x = tuple.x;
        this.y = tuple.y;
    }
}

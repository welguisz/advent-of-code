package com.dwelguisz.year2021.helper.day13;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@EqualsAndHashCode
@AllArgsConstructor
public class Point {
    public Integer x;
    public Integer y;

    @Override
    public String toString() {
        return String.format("< %d, %d>", x, y);
    }
}

package com.dwelguisz.year2021.helper.day21;

import com.dwelguisz.year2021.AdventDay21;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Multiverse {

    public Integer p1pos;
    public Integer p2pos;
    public Integer p1score;
    public Integer p2score;

    public Multiverse(Integer p1start, Integer p2start) {
        this(p1start,p2start,0,0);
    }

    public Multiverse(Integer p1start, Integer p2start, Integer p1score, Integer p2score) {
        this.p1pos = p1start;
        this.p2pos = p2start;
        this.p1score = p1score;
        this.p2score = p2score;
    }

    public Multiverse clone() {
        return new Multiverse(p1pos, p2pos, p1score, p2score);
    }

}

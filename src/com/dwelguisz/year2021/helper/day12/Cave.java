package com.dwelguisz.year2021.helper.day12;

import java.util.HashSet;
import java.util.Set;

public class Cave {
    private String caveName;
    public final boolean isSmallCove;
    private Set<Cave> otherCaves;

    public Cave(String name) {
        this.caveName = name;
        this.isSmallCove = name.toLowerCase().equals(name);
        otherCaves = new HashSet<>();
    }

    public String getCaveName() {
        return caveName;
    }

    public Set<Cave> getOtherCaves() {
        return otherCaves;
    }

    public void insertCave(Cave other) {
        otherCaves.add(other);
    }

}

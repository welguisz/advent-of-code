package com.dwelguisz.year2015.helper.day09;

import java.util.HashMap;
import java.util.Map;

public class City {
    public String name;
    public Map<City, Integer> connections;

    public City(String name) {
        this(name, new HashMap<>());
    }
    public City(String name, Map<City, Integer> connections) {
        this.name = name;
        this.connections = connections;
    }
}

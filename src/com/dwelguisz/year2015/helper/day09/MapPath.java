package com.dwelguisz.year2015.helper.day09;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapPath {
    public List<City> pathList;
    public Map<City, Integer> visited;

    public MapPath(List<City> pathList, Map<City, Integer> visited) {
        this.pathList = pathList;
        this.visited = visited;
    }

    public MapPath duplicate() {
        return new MapPath(new ArrayList<City>(pathList), new HashMap<City, Integer>(visited));
    }
}

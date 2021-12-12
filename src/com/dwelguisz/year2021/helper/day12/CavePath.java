package com.dwelguisz.year2021.helper.day12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CavePath {
    public List<Cave> pathList;
    public Map<Cave, Integer> visited;

    public CavePath(List<Cave> pathList, Map<Cave, Integer> visited) {
        this.visited = visited;
        this.pathList = pathList;
    }

    public CavePath duplicate() {
        return new CavePath(new ArrayList<>(pathList), new HashMap<>(visited));
    }
}

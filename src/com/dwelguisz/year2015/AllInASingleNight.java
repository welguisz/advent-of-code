package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2015.helper.day09.City;
import com.dwelguisz.year2015.helper.day09.MapPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class AllInASingleNight extends AoCDay {

    static Map<String, City> santaMap;
    static List<String> cityToArrayMap;
    static Integer numberOfCities;
    static Integer[][] distances;

    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/advent_of_code/src/resources/year2015/day09/input.txt");
        createMap(lines);
        Integer part1 = solutionPart1();
        Integer part2 = solutionPart2();
        System.out.println(String.format("Part 1 Answer: %d", part1));
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public static void createMap(List<String> lines) {
        santaMap = new HashMap<>();
        for (String line:lines) {
            String[] points = line.split(" ");
            Integer distance = parseInt(points[4]);
            String city1 = points[0];
            String city2 = points[2];
            City point1 = santaMap.computeIfAbsent(city1, k -> new City(k));
            City point2 = santaMap.computeIfAbsent(city2, k -> new City(k));
            point1.connections.put(point2, distance);
            point2.connections.put(point1, distance);
        }
        cityToArrayMap = new ArrayList<>();
        numberOfCities = 0;
        for (String loc : santaMap.keySet()) {
            cityToArrayMap.add(loc);
            numberOfCities++;
        }
        distances = new Integer[numberOfCities][numberOfCities];
        for (int i = 0; i < cityToArrayMap.size(); i++) {
            for(int j = 0; j < cityToArrayMap.size(); j++) {
                distances[i][j] = 0;
                if (i == j) {
                    continue;
                }
                City start1 = santaMap.get(cityToArrayMap.get(i));
                City start2 = santaMap.get(cityToArrayMap.get(j));
                if (start1.connections.containsKey(start2)) {
                    distances[i][j] = start1.connections.get(start2);
                }
            }
        }
    }

    static Integer solutionPart1() {
        boolean[] visitedCity = new boolean[numberOfCities];
        int hamiltonianCycle = Integer.MAX_VALUE;
        List<String> visitedCities = new ArrayList<>();
        for (int i = 0; i < numberOfCities; i++) {
            visitedCity[i] = true;
            visitedCities.add(cityToArrayMap.get(i));
            hamiltonianCycle = Math.min(hamiltonianCycle, findHamiltonianCycle(distances, visitedCity, visitedCities, i, numberOfCities, 1, 0, hamiltonianCycle, false));
        }
        return hamiltonianCycle;
    }

    static Integer solutionPart2() {
        boolean[] visitedCity = new boolean[numberOfCities];
        int hamiltonianCycle = Integer.MIN_VALUE;
        List<String> visitedCities = new ArrayList<>();
        for (int i = 0; i < numberOfCities; i++) {
            visitedCity[i] = true;
            visitedCities.add(cityToArrayMap.get(i));
            hamiltonianCycle = Math.max(hamiltonianCycle, findHamiltonianCycle(distances, visitedCity, visitedCities, i, numberOfCities, 1, 0, hamiltonianCycle, true));
        }
        return hamiltonianCycle;
    }

    public static Integer findHamiltonianCycle(Integer[][]distance, boolean[] visitedCity, List<String> visitedCities, int currentPos, int cities, int count, int cost, int hamiltonianCycle, boolean part2) {
        if (count == cities) {
            hamiltonianCycle = part2 ? Math.max(hamiltonianCycle, cost) : Math.min(hamiltonianCycle, cost);
            return hamiltonianCycle;
        }

        for(int i = 0; i < cities; i++) {
            if (visitedCity[i] == false && distance[currentPos][i] > 0) {
                visitedCity[i] = true;
                visitedCities.add(cityToArrayMap.get(i));
                hamiltonianCycle = findHamiltonianCycle(distance, visitedCity, visitedCities, i, cities, count + 1, cost+ distance[currentPos][i], hamiltonianCycle, part2);
                visitedCity[i] = false;
             }
        }
        return hamiltonianCycle;


    }

    public static List<MapPath> createPaths(City currentLocation, MapPath path) {
        List<MapPath> addPaths = new ArrayList<>();
        for (final City location : currentLocation.connections.keySet()) {
            if (!path.visited.containsKey(location)) {
                final MapPath newMapPath =  path.duplicate();
                newMapPath.pathList.add(location);
                newMapPath.visited.put(location,
                        santaMap.get(location.name).connections.get(currentLocation.name));
                if (!santaMap.values().containsAll(newMapPath.pathList)) {
                    addPaths.addAll(createPaths(location, newMapPath));
                } else {
                    addPaths.add(newMapPath);
                }
            }
        }
        return addPaths;
    }

}

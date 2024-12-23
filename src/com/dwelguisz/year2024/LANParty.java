package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LANParty extends AoCDay {

    Map<String, List<String>> networks;
    Map<String, Set<String>> networkSets;
    List<List<String>> possibleLAN;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 23, false, 0);
        createNetworks(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    void createNetworks(List<String> lines) {
        networks = new HashMap<>();
        networkSets = new HashMap<>();
        possibleLAN = new ArrayList<>();
        for (String line : lines) {
            String[] computers = line.split("-");
            String src = computers[0];
            String dst = computers[1];
            networks.computeIfAbsent(src, k -> new ArrayList<>()).add(dst);
            networkSets.computeIfAbsent(src, k -> new HashSet<>()).add(dst);
            networks.computeIfAbsent(dst, k -> new ArrayList<>()).add(src);
            networkSets.computeIfAbsent(dst, k -> new HashSet<>()).add(src);
            networks.get(src).stream().filter(l -> networks.get(dst).contains(l)).forEach(l -> possibleLAN.add(List.of(src, dst, l)));
        }
    }

    long solutionPart1() {
        return possibleLAN.stream().filter(lans -> lans.stream().anyMatch(l -> l.startsWith("t"))).count();
    }

    List<String> buildClique(List<String> subClique, Map<String, List<String>> networks, Map<String, Set<String>> networkSets) {
        List<String> newClique = new ArrayList<>(subClique);
        String node = newClique.get(0);
        for (String connection : networks.get(node)) {
            boolean foundInSubClique = false;
            for (String checkNode : subClique) {
                if (!networkSets.get(checkNode).contains(connection)) {
                    foundInSubClique = true;
                    break;
                }
            }
            if (!foundInSubClique) {
                newClique.add(connection);
                break;
            }
        }
        if (newClique.size() > subClique.size()) {
            return buildClique(newClique, networks, networkSets);
        }
        return newClique;
    }

    String solutionPart2() {
        Set<String> reviewedNode = new HashSet<>();
        List<String> largestClique = new ArrayList<>();
        for (List<String> grouping : possibleLAN) {
            if (reviewedNode.contains(grouping.get(0))) {
                continue;
            }
            List<String> clique = buildClique(grouping, networks, networkSets);
            for (String host : clique) {
                reviewedNode.add(host);
            }
            if (largestClique.size() < clique.size()) {
                largestClique = clique;
            }
        }
        return largestClique.stream().sorted().collect(Collectors.joining(","));
    }
}

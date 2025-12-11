package com.dwelguisz.year2025;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public class Reactor extends AoCDay {
    public class ReactorPath {
        String node;
        Set<String> visited;
        private int hashCode;
        public ReactorPath(String node, Set<String> visited) {
            this.node = node;
            this.visited = visited;
            this.hashCode = Objects.hash(node, visited);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null | getClass() != o.getClass()) return false;
            ReactorPath other = (ReactorPath) o;
            return (this.node.equals(other.node)) && (this.visited.equals(other.visited));
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2025, 11, false, 0);
        Map<String, List<String>> values = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(values);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(values);
        timeMarkers[3] = Instant.now().toEpochMilli();
        printExplanation = true;
    }

    public Map<String, List<String>> parseLines(List<String> lines) {
        Map<String, List<String>> values = new HashMap<>();
        for (String line : lines) {
            String[] parts = line.split(": ");
            String key = parts[0];
            List<String> value = Arrays.stream(parts[1].split(" ")).toList();
            values.put(key, value);
        }
        return values;
    }

    public long solutionPart1(Map<String, List<String>> values) {
        memoizationCache = new HashMap<>();
        return dfs("you", new HashSet<>(), Set.of(), values);
    }

    public long solutionPart1BFS(Map<String, List<String>> values) {
        Set<List<String>> completedPaths = new HashSet<>();
        Queue<ReactorPath> queue = new LinkedList<>();
        queue.add(new ReactorPath("you", new HashSet<>()));
        Set<String> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            ReactorPath path = queue.poll();
            visited.add(path.node);
            if (path.node.equals("out")) {
                List<String> finalPath = new ArrayList<>(path.visited);
                finalPath.add(path.node);
                completedPaths.add(finalPath);
                continue;
            }
            List<String> nextNodes = values.get(path.node);
            Set<String> visitedPath = path.visited;
            for (String n : nextNodes) {
                Set<String> newPath = new HashSet<>(visitedPath);
                newPath.add(path.node);
                queue.add(new ReactorPath(n, newPath));
            }

        }
        return completedPaths.size();
    }


    Map<ReactorPath, Long> memoizationCache;
    public long solutionPart2(Map<String, List<String>> values) {
        memoizationCache = new HashMap<>();
        return dfs("svr", new HashSet<>(), Set.of("fft", "dac"), values);
    }

    public long dfs(String node, Set<String> visited_node, Set<String> mustIncludeNodes,Map<String, List<String>> values) {
        if (node.equals("out")) {
            return (visited_node.containsAll(mustIncludeNodes)) ? 1 : 0;
        }
        ReactorPath key = new ReactorPath(node, visited_node);
        if (memoizationCache.containsKey(key)) {
            return memoizationCache.get(key);
        }
        long total_paths = 0l;
        for (String neighbor : values.get(node)) {
            if (!visited_node.contains(neighbor)) {
                Set<String> newVisitedNode = new HashSet<>(visited_node);
                if (mustIncludeNodes.contains(neighbor)) {
                    newVisitedNode.add(neighbor);
                }
                total_paths += dfs(neighbor, newVisitedNode, mustIncludeNodes, values);
            }
        }
        memoizationCache.put(key, total_paths);
        return total_paths;
    }
}

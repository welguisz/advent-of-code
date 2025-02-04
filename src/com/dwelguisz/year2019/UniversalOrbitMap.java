package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniversalOrbitMap extends AoCDay {
    public static class OrbitMapNode {
        String id;
        List<OrbitMapNode> childNodes;
        List<OrbitMapNode> parentNodes;

        Integer depth;

        public OrbitMapNode(String id) {
            this.id = id;
            childNodes = new ArrayList<>();
            parentNodes = new ArrayList<>();
            depth = 0;
        }

        public void addChildNode(OrbitMapNode node) {
            childNodes.add(node);
        }

        public void addParentNode(OrbitMapNode node) {
            parentNodes.add(node);
        }

        public void resolveDepth(int depth) {
            this.depth = depth;
            for (OrbitMapNode node : childNodes) {
                node.resolveDepth(depth+1);
            }
        }

        public List<OrbitMapNode> pathToCOM() {
            if (id.equals("COM")) {
                List<OrbitMapNode> nodes = new ArrayList<>();
                nodes.add(this);
                return nodes;
            }
            List<OrbitMapNode> nodes = parentNodes.get(0).pathToCOM();
            nodes.add(this);
            return nodes;
        }
    }

    Map<String, OrbitMapNode> map;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2019,6,false,0);
        Map<String, OrbitMapNode> map = createOrbitMap(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(map);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(map);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Integer solutionPart1(Map<String, OrbitMapNode> map) {
        map.get("COM").resolveDepth(0);
        return map.entrySet().stream().mapToInt(e -> e.getValue().depth).sum();
    }

    Integer solutionPart2(Map<String, OrbitMapNode> map) {
        List<OrbitMapNode> myParents = map.get("YOU").pathToCOM();
        List<OrbitMapNode> santaParents = map.get("SAN").pathToCOM();
        int commonParents = 0;
        while (myParents.get(commonParents).id.equals(santaParents.get(commonParents).id)) {
            commonParents++;
        }
        //Take length of both parents and remove the common Parents so we don't double count.
        return myParents.size() + santaParents.size() - 2 *(commonParents+1);
    }

    Map<String, OrbitMapNode> createOrbitMap(List<String> lines) {
        map = new HashMap<>();
        for (String line : lines) {
            String nodes[] = line.split("\\)");
            String centerNodeStr = nodes[0];
            String orbitingNodeStr = nodes[1];
            OrbitMapNode centerNode = map.getOrDefault(centerNodeStr, new OrbitMapNode(centerNodeStr));
            OrbitMapNode orbitingNode = map.getOrDefault(orbitingNodeStr, new OrbitMapNode(orbitingNodeStr));
            centerNode.addChildNode(orbitingNode);
            orbitingNode.addParentNode(centerNode);
            map.put(centerNodeStr, centerNode);
            map.put(orbitingNodeStr, orbitingNode);
        }
        return map;
    }


}

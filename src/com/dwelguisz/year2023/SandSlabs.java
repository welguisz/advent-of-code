package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord3D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SandSlabs extends AoCDay {

    public class SandSlab {
        Coord3D brick1;
        Coord3D brick2;
        Integer linenumber;

        List<Coord3D> positions;

        public SandSlab(List<Integer> pos1, List<Integer> pos2, Integer linenumber) {
            List<Integer> lowestBrick = (pos1.get(2) <= pos2.get(2)) ? pos1 : pos2;
            List<Integer> highestBrick = (pos1.get(2) > pos2.get(2)) ? pos1 : pos2;
            brick1 = new Coord3D(lowestBrick.get(0),lowestBrick.get(1),lowestBrick.get(2));
            brick2 = new Coord3D(highestBrick.get(0),highestBrick.get(1),highestBrick.get(2));
            this.linenumber = linenumber;
            updatePositions();
        }

        public SandSlab(SandSlab o) {
            brick1 = new Coord3D(o.brick1);
            brick2 = new Coord3D(o.brick2);
            linenumber = o.linenumber;
            updatePositions();
        }

        public SandSlab(Coord3D b1, Coord3D b2, Integer linenumber) {
            brick1 = new Coord3D(b1);
            brick2 = new Coord3D(b2);
            this.linenumber = linenumber;
            updatePositions();
        }
        public void updatePositions() {
            Integer minX = (brick1.x <= brick2.x) ? brick1.x : brick2.x;
            Integer maxX = (brick1.x > brick2.x) ? brick1.x : brick2.x;
            Integer minY = (brick1.y <= brick2.y) ? brick1.y : brick2.y;
            Integer maxY = (brick1.y > brick2.y) ? brick1.y : brick2.y;
            Integer minZ = (brick1.z <= brick2.z) ? brick1.z : brick2.z;
            Integer maxZ = (brick1.z > brick2.z) ? brick1.z : brick2.z;
            positions = new ArrayList<>();
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        positions.add(new Coord3D(x,y,z));
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "brick1 : " + brick1.toString() + "; brick2: " + brick2.toString() + "; line: " + linenumber;
        }

        public int compare(SandSlab other) {
            return brick1.z - other.brick1.z;
        }

        public SandSlab fall() {
            Coord3D tbrick1 = new Coord3D(brick1.x,brick1.y,brick1.z-1);
            Coord3D tbrick2 = new Coord3D(brick2.x,brick2.y, brick2.z-1);
            return new SandSlab(tbrick1, tbrick2, linenumber);
        }
    }
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 22, false, 0);
        List<SandSlab> sandSlabs = createSandSlabs(lines);
        sandSlabs = sandSlabs.stream().sorted((a,b) -> a.compare(b)).collect(Collectors.toList());
        simulate(sandSlabs);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<SandSlab> createSandSlabs(List<String> lines) {
        List<SandSlab> blocks = new ArrayList<>();
        int lineNumber = 0;
        for (String line : lines) {
            String[] split= line.split("~");
            List<Integer> part1 = Arrays.stream(split[0].split(",")).map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> part2 = Arrays.stream(split[1].split(",")).map(Integer::parseInt).collect(Collectors.toList());
            blocks.add(new SandSlab(part1, part2, lineNumber));
            lineNumber++;
        }
        return blocks;
    }

    void falls(SandSlab brick,
                  Set<SandSlab> falling) {
        if (falling.contains(brick)) {
            return;
        }
        falling.add(brick);
        for (SandSlab parent : slabsAbove.getOrDefault(brick, new HashSet<>())) {
            Set<SandSlab> tmp = slabsBelow.get(parent).stream().filter(l -> !falling.contains(l)).collect(Collectors.toSet());
            if (tmp.isEmpty()) {
                falls(parent, falling);
            }
        }
    }

    Integer whatIf(SandSlab brick) {
        Set<SandSlab> falling = new HashSet<>();
        falls(brick,falling);
        return falling.size();
    }
    List<SandSlab> fallenBricks;
    Map<SandSlab,Set<SandSlab>> slabsAbove;
    Map<SandSlab,Set<SandSlab>> slabsBelow;

    private void simulate(List<SandSlab> sandSlabs) {
        fallenBricks = new ArrayList<>();
        Map<Coord3D, SandSlab> occupied = new HashMap<>();
        for (SandSlab slab : sandSlabs) {
            SandSlab brick = new SandSlab(slab);
            while (true) {
                SandSlab next = brick.fall();
                List<Coord3D> positions = next.positions;

                if ((next.brick1.z > 0) && (!positions.stream().anyMatch(pos -> occupied.containsKey(pos)))) {
                    brick = next;
                } else {
                    SandSlab finalBrick = brick;
                    brick.positions.stream().forEach(pos -> occupied.put(pos, finalBrick));
                    fallenBricks.add(brick);
                    break;
                }
            }
        }
        slabsAbove = new HashMap<>();
        slabsBelow = new HashMap<>();
        for (SandSlab brick : fallenBricks) {
            Set<Coord3D> inThisBrick = new HashSet<>(brick.positions);
            List<Coord3D> positions = brick.fall().positions;
            for (Coord3D position : positions) {
                if (occupied.containsKey(position) && !inThisBrick.contains(position)) {
                    slabsAbove.computeIfAbsent(occupied.get(position), k -> new HashSet<>()).add(brick);
                    slabsBelow.computeIfAbsent(brick, k -> new HashSet<>()).add(occupied.get(position));
                }
            }
        }

    }
    public Long solutionPart1() {
        return fallenBricks.stream().map(b -> whatIf(b)).filter(b -> b == 1).count();
    }

    public Long solutionPart2() {
        return fallenBricks.stream().mapToLong(b -> whatIf(b)-1).sum();
    }
}

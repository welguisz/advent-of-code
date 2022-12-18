package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PyroclasticFlow extends AoCDay {
    public void solve() {
        System.out.println("Day 17 ready to go.");
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day17/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        char[] parsedClass = parseLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(parsedClass, 2022);
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2(parsedClass, 1000000000000L);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public char[] parseLines(List<String> lines) {
        return lines.get(0).toCharArray();
    }

    public Long solutionPart1(char[] push, Integer rocks) {
        Integer i = 0;
        Integer rockNo = 0;
        Long top = 0L;
        Set<Pair<Integer,Long>> chamber = IntStream.range(0,7).boxed().map(pos -> Pair.of(pos,0L)).collect(Collectors.toSet());
        while (rockNo < rocks) {
            Set<Pair<Integer,Long>> piece = getPiece(rockNo %5, top+4);
            while(true) {
                if (push[i] == '<') {
                    piece = moveLeft(piece);
                    if (piece.stream().anyMatch(r -> chamber.contains(r))) { //piece is already there...
                        piece = moveRight(piece);
                    }
                } else {
                    piece = moveRight(piece);
                    if (piece.stream().anyMatch(r -> chamber.contains(r))) {
                        piece = moveLeft(piece);
                    }
                }
                i = (i+1)%push.length;
                piece = moveDown(piece);
                if (piece.stream().anyMatch(r -> chamber.contains(r))) {
                    piece = moveUp(piece);
                    chamber.addAll(piece);
                    top = chamber.stream().mapToLong(p -> p.getRight()).max().getAsLong();
                    break;
                }
            }
            rockNo++;
        }
        return top;
    }

    Set<Pair<Integer,Long>> getPiece(int rockNo, Long y) {
        if (rockNo == 0) {
            return Set.of(Pair.of(2,y),Pair.of(3,y),Pair.of(4,y),Pair.of(5,y));
        } else if (rockNo == 1) {
            return Set.of(Pair.of(3,y+2),Pair.of(2,y+1),Pair.of(3,y+1),Pair.of(4,y+1),Pair.of(3,y));
        } else if (rockNo == 2) {
            return Set.of(Pair.of(2,y),Pair.of(3,y),Pair.of(4,y),Pair.of(4,y+1),Pair.of(4,y+2));
        } else if (rockNo == 3) {
            return Set.of(Pair.of(2,y),Pair.of(2,y+1),Pair.of(2,y+2),Pair.of(2,y+3));
        } else if (rockNo == 4) {
            return Set.of(Pair.of(2,y+1),Pair.of(2,y),Pair.of(3,y+1),Pair.of(3,y));
        }
        return Set.of();
    }

    Set<Pair<Integer, Long>> moveLeft(Set<Pair<Integer,Long>> piece) {
        if (piece.stream().anyMatch(p -> p.getLeft() == 0)) {
            return piece;
        }
        return piece.stream().map(p -> Pair.of(p.getLeft()-1,p.getRight())).collect(Collectors.toSet());
    }

    Set<Pair<Integer, Long>> moveRight(Set<Pair<Integer,Long>> piece) {
        if (piece.stream().anyMatch(p -> p.getLeft() == 6)) {
            return piece;
        }
        return piece.stream().map(p -> Pair.of(p.getLeft()+1,p.getRight())).collect(Collectors.toSet());
    }

    Set<Pair<Integer, Long>> moveDown(Set<Pair<Integer,Long>> piece) {
        return piece.stream().map(p -> Pair.of(p.getLeft(),p.getRight()-1)).collect(Collectors.toSet());
    }

    Set<Pair<Integer, Long>> moveUp(Set<Pair<Integer,Long>> piece) {
        return piece.stream().map(p -> Pair.of(p.getLeft(),p.getRight()+1)).collect(Collectors.toSet());
    }

    public Set<Pair<Integer,Long>> findSignature(Set<Pair<Integer,Long>> chamber) {
        Long maxY = chamber.stream().mapToLong(p -> p.getRight()).max().getAsLong();
        return chamber.stream().filter(p -> maxY - p.getRight() <= 30).map(p -> Pair.of(p.getLeft(),maxY-p.getRight())).collect(Collectors.toSet());
    }

    public static class ChamberState {
        Integer iLoc;
        Integer rockId;
        Set<Pair<Integer,Long>> chamberSlice;
        Long topValue;
        Long rockNumber;
        public ChamberState(Integer iLoc, Integer rockId, Set<Pair<Integer,Long>> chamberSlice, Long topValue, Long rockNumber) {
            this.iLoc = iLoc;
            this.rockId = rockId;
            this.chamberSlice = chamberSlice;
            this.topValue = topValue;
            this.rockNumber = rockNumber;
        }

        public boolean StateEquivalent(ChamberState other) {
            if (!iLoc.equals(other.iLoc)) {
                return false;
            }
            if (!rockId.equals(other.rockId)) {
                return false;
            }
            Boolean checkSlice1 = this.chamberSlice.stream().allMatch(p -> other.chamberSlice.contains(p));
            if (!checkSlice1) {
                return false;
            }
            return other.chamberSlice.stream().allMatch(p -> this.chamberSlice.contains(p));
        }
    }

    public Pair<Boolean, Integer> findPreviousState(List<ChamberState> chamberStates, ChamberState current) {
        int index = 0;
        for (ChamberState prev : chamberStates) {
            if (chamberStates.get(index).StateEquivalent(current)) {
                return Pair.of(true, index);
            }
            index++;
        }
        return Pair.of(false, -1);
    }

    public Long solutionPart2(char[] push, Long rocks) {
        Integer i = 0;
        Long rockNo = 0L;
        Long top = 0L;
        Long added = 0L;
        Set<Pair<Integer,Long>> chamber = IntStream.range(0,7).boxed().map(pos -> Pair.of(pos,0L)).collect(Collectors.toSet());
        List<ChamberState> states = new ArrayList<>();
        while (rockNo < rocks) {
            Set<Pair<Integer,Long>> piece = getPiece((int) (rockNo %5), top+4);
            while(true) {
                if (push[i] == '<') {
                    piece = moveLeft(piece);
                    if (piece.stream().anyMatch(r -> chamber.contains(r))) { //piece is already there...
                        piece = moveRight(piece);
                    }
                } else {
                    piece = moveRight(piece);
                    if (piece.stream().anyMatch(r -> chamber.contains(r))) {
                        piece = moveLeft(piece);
                    }
                }
                i = (i+1)%push.length;
                piece = moveDown(piece);
                if (piece.stream().anyMatch(r -> chamber.contains(r))) {
                    piece = moveUp(piece);
                    chamber.addAll(piece);
                    top = chamber.stream().mapToLong(p -> p.getRight()).max().getAsLong();
                    Set<Pair<Integer,Long>> signature = findSignature(chamber);
                    Long rockModulo = rockNo%5;
                    ChamberState current = new ChamberState(i, rockModulo.intValue(), signature, top, rockNo);
                    if (rockNo == 3177L) {
                        System.out.println("Stop here");
                    }
                    Pair<Boolean, Integer> result = findPreviousState(states, current);
                    if (result.getLeft()) {
                        ChamberState previous = states.get(result.getRight());
                        Long oldRock = previous.rockNumber;
                        Long oldY = previous.topValue;
                        Long dy = top - oldY;
                        Long dRock = rockNo - oldRock;
                        Long patternRepeatingTime = (rocks - rockNo) / dRock;
                        added += patternRepeatingTime * dy;
                        rockNo += patternRepeatingTime * dRock;
                    } else {
                        states.add(current);
                    }
                    break;
                }
            }
            rockNo++;
        }
        return top + added;
    }
}

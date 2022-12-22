package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.utilities.Coord3D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AoC2022Day22 extends AoCDay {
    List<Pair<Integer,String>> steps;

    public void solve() {
        System.out.println(String.format("%s ready to go", getClass().getName()));
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day22/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        String[][] map = parsedLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Integer part1 = solutionPart1(map);
        Long part1Time = Instant.now().toEpochMilli();
        Integer part2 = solutionPart2(map);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public String[][] parsedLines(List<String> lines) {
        List<String> values = new ArrayList<>();
        Integer index = 0;
        String instruction = lines.remove(lines.size()-1);
        lines.remove(lines.size()-1);
        Integer mapHeight = lines.size() ;
        Integer maxWidth = lines.stream().mapToInt(l -> l.length()).max().getAsInt();
        String[][] map = new String[mapHeight][maxWidth];
        for (String l : lines) {
            for (int i = 0; i < maxWidth; i++) {
                if (i < l.length()) {
                    map[index][i] = l.substring(i, i + 1);
                } else {
                    map[index][i] = " ";
                }
            }
            index++;
        }
        parseInstruction(instruction);
        return map;
    }

    void parseInstruction(String instruction) {
        List<String> turns = List.of("L","R");
        List<Integer> turnLocations = IntStream.range(0,instruction.length()).filter(i -> turns.contains(instruction.substring(i,i+1))).boxed().collect(Collectors.toList());
        turnLocations.add(instruction.length());
        steps = new ArrayList<>();
        Integer index = 0;
        Integer turnIdx = 0;
        while (index < instruction.length()) {
            Integer turnLoc = turnLocations.get(turnIdx);
            Integer count = Integer.parseInt(instruction.substring(index,turnLoc));
            String turn = (turnLoc+1 >=instruction.length()) ? "Done" : instruction.substring(turnLoc, turnLoc+1);
            steps.add(Pair.of(count, turn));
            turnIdx++;
            index = turnLoc+1;
        }
    }

    Coord2D updateDir(Coord2D dir, String turn) {
        if (turn.equals("R")) {
            return new Coord2D(dir.y,-1*dir.x);
        } else if(turn.equals("L")) {
            return new Coord2D(-1*dir.y,dir.x);
        }
        return dir;
    }

    Coord2D moveForward(String map[][], Coord2D loc, Coord2D dir) {
        Integer nextX = loc.x+dir.x;
        Integer nextY = loc.y+dir.y;
        nextX -= (nextX >= map.length) ? map.length : 0;
        nextY -= (nextY >= map[0].length) ? map[0].length : 0;
        nextX += (nextX < 0) ? map.length : 0;
        nextY += (nextY < 0) ? map[0].length : 0;
        return new Coord2D(nextX, nextY);
    }

    public Integer solutionPart1(String map[][]) {
        Coord2D currentLocation = new Coord2D(0,IntStream.range(0,map[0].length).filter(i -> map[0][i].equals(".")).min().getAsInt());
        Coord2D direction = new Coord2D(0,1);
        for (Pair<Integer, String> step : steps) {
            for (int i = 0; i < step.getLeft(); i++) {
                Coord2D nextLoc = moveForward(map, currentLocation, direction);
                String nextVal = map[nextLoc.x][nextLoc.y];
                if (nextVal.equals("#")) {
                    break;
                } else if (nextVal.equals(".")) {
                    currentLocation = nextLoc;
                } else {
                    Coord2D nextLoc1 = jumpBoard(map, nextLoc, direction);
                    if (nextLoc1.equals(nextLoc)) {
                        break;
                    } else {
                        nextLoc = nextLoc1;
                    }
                    nextVal = map[nextLoc.x][nextLoc.y];
                    if (nextVal.equals("#")) {
                        break;
                    } else if (nextVal.equals(".")) {
                        currentLocation = nextLoc;
                    }
                }
            }
            direction = updateDir(direction, step.getRight());
        }
        return (currentLocation.x+1) * 1000 + (currentLocation.y+1) * 4 + decodeDir(direction);
    }

    public Coord2D jumpBoard(String map[][], Coord2D nextLoc, Coord2D dir) {
        if (dir.x == 0 && dir.y == 1) {
            for (int i = 0; i < map[nextLoc.x].length; i++) {
                if (map[nextLoc.x][i].equals("#")) {
                    return nextLoc;
                } else if (map[nextLoc.x][i].equals(".")) {
                    return new Coord2D(nextLoc.x,i);
                }
            }
        }
        if (dir.x == -1 && dir.y == 0) {
            for (int i = map.length-1; i >=0; i--) {
                if (map[i][nextLoc.y].equals("#")) {
                    return nextLoc;
                } else if (map[i][nextLoc.y].equals(".")) {
                    return new Coord2D(i,nextLoc.y);
                }
            }
        }
        if (dir.x == 0 && dir.y == -1) {
            for (int i = map[nextLoc.x].length-1;i>=0;i--) {
                if (map[nextLoc.x][i].equals("#")) {
                    return nextLoc;
                } else if (map[nextLoc.x][i].equals(".")) {
                    return new Coord2D(nextLoc.x,i);
                }
            }
        }
        if (dir.x == 1 && dir.y == 0) {
            for (int i = 0; i < map.length; i++) {
                if (map[i][nextLoc.y].equals("#")) {
                    return nextLoc;
                } else if (map[i][nextLoc.y].equals(".")) {
                    return new Coord2D(i,nextLoc.y);
                }
            }
        }
        return nextLoc;
    }
    public Integer decodeDir(Coord2D dir) {
        if (dir.x == 0 && dir.y == 1) {
            return 0;
        } else if (dir.x == -1 && dir.y == 0) {
            return 3;
        } else if (dir.x == 0 && dir.y == -1) {
            return 2;
        } else if (dir.x == -1 && dir.y == 0) {
            return 1;
        }
        return 0;
    }

    public Integer solutionPart2(String map[][]) {
        Coord2D currentLocation = new Coord2D(0,IntStream.range(0,map[0].length).filter(i -> map[0][i].equals(".")).min().getAsInt());
        Coord2D direction = new Coord2D(0,1);
        for (Pair<Integer, String> step : steps) {
            for (int i = 0; i < step.getLeft(); i++) {
                Pair<Coord2D, Coord2D> stepForward = moveCubeForward(currentLocation, direction);
                Coord2D nextLoc = stepForward.getLeft();
                Coord2D nextDirection = stepForward.getRight();
                String nextVal = map[nextLoc.x][nextLoc.y];
                if (nextVal.equals("#")) {
                    break;
                } else if (nextVal.equals(".")) {
                    currentLocation = nextLoc;
                    direction = nextDirection;
                }
            }
            direction = updateDir(direction, step.getRight());
        }
        return (currentLocation.x+1) * 1000 + (currentLocation.y+1) * 4 + decodeDir(direction);
    }

    List<Integer> createList(Integer low, Integer high) {
        return IntStream.range(low,high).boxed().collect(Collectors.toList());
    }
    Integer currentSide(Coord2D currentLocation) {
        if (createList(0,50).contains(currentLocation.x)) {
            if (createList(50,100).contains(currentLocation.y)) {
                return 1;
            } else if (createList(100,150).contains(currentLocation.y)) {
                return 2;
            }
            else if (currentLocation.y == 150) {
                return 5;
            } else if (currentLocation.y == 49) {
                return 4;
            }
        } else if (createList(50,100).contains(currentLocation.x)) {
            if (currentLocation.y == 100) {
                return 2;
            } else if (currentLocation.y == 49) {
                return 4;
            }
            return 3;
        } else if (createList(100,150).contains(currentLocation.x)) {
            if (createList(0,50).contains(currentLocation.y)) {
                return 4;
            } else if (createList(50,100).contains(currentLocation.y)) {
                return 5;
            } else if (currentLocation.y == -1) {
                return 1;
            } else if (currentLocation.y == 100) {
                return 2;
            }
        } else if (createList(150,200).contains(currentLocation.x)) {
            if (createList(0,100).contains(currentLocation.y)) {
                return 6;
            } else if (currentLocation.y == -1) {
                return 1;
            }
        } else if (currentLocation.x == -1) {
            if (createList(50,150).contains(currentLocation.y)) {
                return 6;
            }
        } else if (currentLocation.x == 200) {
            if (createList(0,50).contains(currentLocation.y)) {
                return 2;
            }
        }
        return -1;
    }

    Pair<Coord2D, Coord2D> moveCubeForward(Coord2D currentLocation, Coord2D currentDirection) {
        Integer currentSide = currentSide(currentLocation);
        Coord2D nextLoc = new Coord2D(currentLocation.x + currentDirection.x, currentLocation.y + currentDirection.y);
        Integer nextSide = currentSide(nextLoc);
        Coord2D nextDirection = currentDirection;
        if (nextSide != currentSide) {
            Pair<Integer, Coord2D> result = jumpSide(currentSide, currentDirection);
            nextDirection = result.getRight();
            nextLoc = jumpCoords(nextLoc,currentSide,result.getLeft());
        }

        return Pair.of(nextLoc, nextDirection);
    }

    Coord2D jumpCoords(Coord2D nextLoc, Integer currentSide, Integer nextSide) {
        if (currentSide == 1 && nextSide == 6) {
            return new Coord2D(151+nextLoc.x,0);
        } else if (currentSide == 1 && nextSide == 4) {
            return new Coord2D(101+nextLoc.x,0);
        } else if (currentSide == 2 && nextSide == 6) {
            return new Coord2D(199,nextLoc.y-100);
        } else if (currentSide == 2 && nextSide == 3) {
            return new Coord2D(49,nextLoc.x+50);
        } else if (currentSide == 2 && nextSide == 5) {
            return new Coord2D(Math.abs(nextLoc.x-49)+100,99);
        } else if (currentSide == 3 && nextSide == 2) {
            return new Coord2D(nextLoc.y-50,99);
        } else if (currentSide == 3 && nextSide == 4) {
            return new Coord2D(100,nextLoc.x-50);
        } else if (currentSide == 4 && nextSide == 1) {
            return new Coord2D(Math.abs(nextLoc.x-149),50);
        } else if (currentSide == 4 && nextSide == 3) {
            return new Coord2D(nextLoc.y+50,50);
        } else if (currentSide == 5 && nextSide == 2) {
            return new Coord2D(Math.abs(nextLoc.x-149),149);
        } else if (currentSide == 5 && nextSide == 6) {
            return new Coord2D(nextLoc.y+100,49);
        } else if (currentSide == 6 && nextSide == 1) {
            return new Coord2D(0,nextLoc.x-100);
        } else if (currentSide == 6 && nextSide == 2) {
            return new Coord2D(0,nextLoc.y+100);
        } else if (currentSide == 6 && nextSide == 5) {
            return new Coord2D(149,nextLoc.x-100);
        }
        return nextLoc;
    }

    Pair<Integer,Coord2D> jumpSide(Integer currentSide, Coord2D direction) {
        Coord2D right = new Coord2D(0,1);
        Coord2D down = new Coord2D(1,0);
        Coord2D left = new Coord2D(0,-1);
        Coord2D up = new Coord2D(-1,0);
        Map<Pair<Integer, Coord2D>, Pair<Integer,Coord2D>> jumpCoords = new HashMap();

        //Done by hand
        jumpCoords.put(Pair.of(1,up),Pair.of(6,right));
        jumpCoords.put(Pair.of(1,right),Pair.of(2,right));
        jumpCoords.put(Pair.of(1,down),Pair.of(3,down));
        jumpCoords.put(Pair.of(1,left),Pair.of(4,right));

        jumpCoords.put(Pair.of(2,up),Pair.of(6,up));
        jumpCoords.put(Pair.of(2,right),Pair.of(5,left));
        jumpCoords.put(Pair.of(2,down),Pair.of(3,left));
        jumpCoords.put(Pair.of(2,left),Pair.of(1,left));

        jumpCoords.put(Pair.of(3,up),Pair.of(1,up));
        jumpCoords.put(Pair.of(3,right),Pair.of(2,up));
        jumpCoords.put(Pair.of(3,down),Pair.of(5,down));
        jumpCoords.put(Pair.of(3,left),Pair.of(4,down));

        jumpCoords.put(Pair.of(4,up),Pair.of(3,right));
        jumpCoords.put(Pair.of(4,right),Pair.of(5,right));
        jumpCoords.put(Pair.of(4,down),Pair.of(6,down));
        jumpCoords.put(Pair.of(4,left),Pair.of(1,right));

        jumpCoords.put(Pair.of(5,up),Pair.of(3,up));
        jumpCoords.put(Pair.of(5,right),Pair.of(2,left));
        jumpCoords.put(Pair.of(5,down),Pair.of(6,left));
        jumpCoords.put(Pair.of(5,left),Pair.of(4,left));

        jumpCoords.put(Pair.of(6,up),Pair.of(4,up));
        jumpCoords.put(Pair.of(6,right),Pair.of(5,up));
        jumpCoords.put(Pair.of(6,down),Pair.of(2,down));
        jumpCoords.put(Pair.of(6,left),Pair.of(1,down));

        return jumpCoords.get(Pair.of(currentSide, direction));

    }

}

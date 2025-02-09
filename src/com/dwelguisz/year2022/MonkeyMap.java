package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MonkeyMap extends AoCDay {

    public static Coord2D RIGHT = new Coord2D(0,1);
    public static Coord2D DOWN = new Coord2D(1,0);
    public static Coord2D LEFT = new Coord2D(0,-1);
    public static Coord2D UP = new Coord2D(-1,0);

    List<Pair<Integer,String>> steps;
    List<Coord2D> path;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2022,22,false,0);
        String[][] map = parsedLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(map);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(map);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public List<Coord2D> parsePath(List<String> strs) {
        path = new ArrayList<>();
        String coords[] = strs.get(0).split(":::");
        for (String coord : coords) {
            String t = coord.substring(1,coord.length()-1);
            String len[] = t.split(", ");
            path.add(new Coord2D(Integer.parseInt(len[0])-1,Integer.parseInt(len[1])-1));
        }
        return path;
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
    Integer currentSide(Coord2D currentLocation, Coord2D direction) {
        if (createList(0,50).contains(currentLocation.x)) {
            if (createList(50,100).contains(currentLocation.y)) {
                return 1;
            } else if (createList(100,150).contains(currentLocation.y)) {
                return 2;
            }
            else if (currentLocation.y == 150) {
                return 5;
            } else if (currentLocation.y == 49) {
                if (direction.equals(UP)) {
                    return 3;
                }
                return 4;
            }
        } else if (createList(50,100).contains(currentLocation.x)) {
            if (currentLocation.y == 100) {
                if (direction.equals(DOWN)) {
                    return 3;
                }
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
            if (createList(0,50).contains(currentLocation.y)) {
                return 6;
            } else if (currentLocation.y == -1) {
                return 1;
            } else if (currentLocation.y == 50) {
                return 5;
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
        Integer currentSide = currentSide(currentLocation, currentDirection);
        Coord2D nextLoc = new Coord2D(currentLocation.x + currentDirection.x, currentLocation.y + currentDirection.y);
        Integer nextSide = currentSide(nextLoc, currentDirection);
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
            return new Coord2D(100+nextLoc.y,0);
        } else if (currentSide == 1 && nextSide == 4) {
            return new Coord2D(Math.abs(nextLoc.x-49)+100,0);
        } else if (currentSide == 2 && nextSide == 6) {
            return new Coord2D(199,nextLoc.y-100);
        } else if (currentSide == 2 && nextSide == 3) {
            return new Coord2D(nextLoc.y-50,99);
        } else if (currentSide == 2 && nextSide == 5) {
            return new Coord2D(Math.abs(nextLoc.x-49)+100,99);
        } else if (currentSide == 3 && nextSide == 2) {
            return new Coord2D(49, nextLoc.x+50);
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
        Map<Pair<Integer, Coord2D>, Pair<Integer,Coord2D>> jumpCoords = new HashMap();

        //Done by hand
        jumpCoords.put(Pair.of(1,UP),Pair.of(6,RIGHT));
        jumpCoords.put(Pair.of(1,RIGHT),Pair.of(2,RIGHT));
        jumpCoords.put(Pair.of(1,DOWN),Pair.of(3,DOWN));
        jumpCoords.put(Pair.of(1,LEFT),Pair.of(4,RIGHT));

        jumpCoords.put(Pair.of(2,UP),Pair.of(6,UP));
        jumpCoords.put(Pair.of(2,RIGHT),Pair.of(5,LEFT));
        jumpCoords.put(Pair.of(2,DOWN),Pair.of(3,LEFT));
        jumpCoords.put(Pair.of(2,LEFT),Pair.of(1,LEFT));

        jumpCoords.put(Pair.of(3,UP),Pair.of(1,UP));
        jumpCoords.put(Pair.of(3,RIGHT),Pair.of(2,UP));
        jumpCoords.put(Pair.of(3,DOWN),Pair.of(5,DOWN));
        jumpCoords.put(Pair.of(3,LEFT),Pair.of(4,DOWN));

        jumpCoords.put(Pair.of(4,UP),Pair.of(3,RIGHT));
        jumpCoords.put(Pair.of(4,RIGHT),Pair.of(5,RIGHT));
        jumpCoords.put(Pair.of(4,DOWN),Pair.of(6,DOWN));
        jumpCoords.put(Pair.of(4,LEFT),Pair.of(1,RIGHT));

        jumpCoords.put(Pair.of(5,UP),Pair.of(3,UP));
        jumpCoords.put(Pair.of(5,RIGHT),Pair.of(2,LEFT));
        jumpCoords.put(Pair.of(5,DOWN),Pair.of(6,LEFT));
        jumpCoords.put(Pair.of(5,LEFT),Pair.of(4,LEFT));

        jumpCoords.put(Pair.of(6,UP),Pair.of(4,UP));
        jumpCoords.put(Pair.of(6,RIGHT),Pair.of(5,UP));
        jumpCoords.put(Pair.of(6,DOWN),Pair.of(2,DOWN));
        jumpCoords.put(Pair.of(6,LEFT),Pair.of(1,DOWN));

        return jumpCoords.get(Pair.of(currentSide, direction));

    }

}

package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class JurassicJigsaw extends AoCDay {

    public static class Tile {
        public final Long id;
        String[][] grid;
        public Integer[] sideValues;
        Set<Tile> neighboringTiles;
        private int hashCode;

        public Tile northTile;
        public Tile westTile;
        public Tile southTile;
        public Tile eastTile;
        public boolean frozen;
        public Tile(Long id, String[][] grid) {
            this.id = id;
            this.grid = grid;
            calculateSideValues();
            calculateFlippedSideValues();
            neighboringTiles = new HashSet<>();
            frozen = false;
            this.hashCode = Objects.hash(id, grid);
            northTile = null;
            westTile = null;
            southTile = null;
            eastTile = null;
        }

        public void freezeTile() {
            this.frozen = true;
        }

        public boolean isFrozen() {
            return this.frozen;
        }

        public void topBecomesBottom() {
            if (frozen) {
                return;
            }
            String[][] newGrid = new String[grid.length][grid[0].length];
            for (Integer i = 0; i < grid.length; i++) {
                for (Integer j = 0; j < grid[i].length; j++) {
                    newGrid[grid.length-(i+1)][j] = grid[i][j];
                }
            }
            this.grid = newGrid;
            calculateSideValues();
            calculateFlippedSideValues();
        }

        public void rightBecomesLeft() {
            if (frozen) {
                return;
            }
            String[][] newGrid = new String[grid.length][grid[0].length];
            for (Integer i = 0; i < grid.length; i++) {
                for (Integer j = 0; j < grid[i].length; j++) {
                    newGrid[i][grid.length-(j+1)] = grid[i][j];
                }
            }
            this.grid = newGrid;
            calculateSideValues();
            calculateFlippedSideValues();
        }

        public void rotateCounterClockwise() {
            if (frozen) {
                return;
            }
            String[][] newGrid = new String[grid.length][grid[0].length];
            for (Integer i = 0; i < grid.length; i++) {
                for (Integer j = 0; j < grid[i].length; j++) {
                    newGrid[grid[i].length-(j+1)][i] = grid[i][j];
                }
            }
            this.grid = newGrid;
            calculateSideValues();
            calculateFlippedSideValues();
        }

        public void rotateClockwise() {
            if (frozen) {
                return;
            }
            String[][] newGrid = new String[grid.length][grid[0].length];
            for (Integer i = 0; i < grid.length; i++) {
                for (Integer j = 0; j < grid[i].length; j++) {
                    newGrid[j][grid[i].length-(i+1)] = grid[i][j];
                }
            }
            this.grid = newGrid;
            calculateSideValues();
            calculateFlippedSideValues();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null | getClass() != o.getClass()) return false;
            Tile other = (Tile) o;
            return (this.id.equals(other.id)) && (this.grid.equals(other.grid));
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        private void calculateSideValues() {
            Integer bitValue = 1;
            Integer rightSideIndex = grid[0].length-1;
            Integer bottomSideIndex = grid.length - 1;
            Integer topSideIndex = 0;
            Integer leftSideIndex = 0;
            sideValues = new Integer[]{0,0,0,0,0,0,0,0};
            for (int i = 0; i < grid.length; i++) {
                if (grid[topSideIndex][i].equals("#")) {
                    sideValues[0] |= bitValue;
                }
                if (grid[i][leftSideIndex].equals("#")) {
                    sideValues[1] += bitValue;
                }
                if (grid[bottomSideIndex][i].equals("#")){
                    sideValues[2] += bitValue;
                }
                if (grid[i][rightSideIndex].equals("#")) {
                    sideValues[3] += bitValue;
                }
                bitValue <<= 1;
            }
        }

        private void calculateFlippedSideValues() {
            Integer bitValue = 1;
            Integer rightSideIndex = grid[0].length-1;
            Integer bottomSideIndex = grid.length - 1;
            Integer topSideIndex = 0;
            Integer leftSideIndex = 0;
            for (int i = grid.length-1; i >= 0; i--) {
                if (grid[topSideIndex][i].equals("#")) {
                    sideValues[4] |= bitValue;
                }
                if (grid[i][leftSideIndex].equals("#")) {
                    sideValues[5] += bitValue;
                }
                if (grid[bottomSideIndex][i].equals("#")){
                    sideValues[6] += bitValue;
                }
                if (grid[i][rightSideIndex].equals("#")) {
                    sideValues[7] += bitValue;
                }
                bitValue <<= 1;
            }
        }

        public void addNeighboringTile(Tile neighbor) {
            neighboringTiles.add(neighbor);
        }

        public Integer numberOfNeighbors() {
            return neighboringTiles.size();
        }

    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2020,20,false,0);
        List<Tile> tiles = parseLines(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(tiles);
        timeMarkers[2] = Instant.now().toEpochMilli();
        String[][] grid = buildPicture(tiles);
        grid = removeBorders(grid);
        part2Answer = solutionPart2(grid);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    String[][] removeBorders(String[][] grid) {
        String[][] newGrid = new String[96][96];
        List<Integer> ignoreRows = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            ignoreRows.add(i*10);
            ignoreRows.add((i*10)+9);
        }
        Integer trueRowNumber = 0;
        for (int row = 0; row < grid.length; row++) {
            Integer trueColNumber = 0;
            if (ignoreRows.contains(row)) {
                continue;
            }
            for (int col = 0; col < grid[row].length; col++) {
                if (ignoreRows.contains(col)) {
                    continue;
                }
                newGrid[trueRowNumber][trueColNumber] = grid[row][col];
                trueColNumber++;
            }
            trueRowNumber++;
        }
        return newGrid;
    }

    List<Tile> parseLines(List<String> lines) {
        List<Tile> tiles = new ArrayList<>();
        Long id = 0L;
        List<String> grid = new ArrayList<>();
        for (String l : lines) {
            if (l.length() == 0) {
                tiles.add(new Tile(id, convertToGrid(grid)));
                grid = new ArrayList<>();
            } else if (l.contains("Tile")) {
                id = Long.parseLong(l.substring(5,9));
            } else {
                grid.add(l);
            }
        }
        return tiles;
    }

    Long solutionPart1(List<Tile> tiles) {
        Map<Integer, ArrayList<Tile>> matchingSides = new HashMap<>();
        for (Tile tile : tiles) {
            for (Integer value : tile.sideValues) {
                ArrayList<Tile> mapTiles = matchingSides.getOrDefault(value, new ArrayList<>());
                mapTiles.add(tile);
                matchingSides.put(value, mapTiles);
            }
        }
        for (Map.Entry<Integer, ArrayList<Tile>> findMatching : matchingSides.entrySet()) {
            if (findMatching.getValue().size() != 2) {
                continue;
            }
            Tile tile0 = findMatching.getValue().get(0);
            Tile tile1 = findMatching.getValue().get(1);
            tile0.addNeighboringTile(tile1);
            tile1.addNeighboringTile(tile0);
        }
        List<Tile> cornerTiles = tiles.stream().filter(t -> t.numberOfNeighbors().equals(2)).collect(Collectors.toList());
        return cornerTiles.stream().mapToLong(t -> t.id).reduce(1L, (a,b) -> a* b);
    }

    String [][] buildPicture(List<Tile> tiles) {
        List<Tile> cornerTiles = tiles.stream().filter(t -> t.numberOfNeighbors().equals(2)).collect(Collectors.toList());
        Set<Tile> frozenTiles = new HashSet<>();
        Tile currentTile = cornerTiles.get(0);
        ArrayDeque<Tile> nextTiles = new ArrayDeque<>();
        while(frozenTiles.size() < tiles.size()) {
            currentTile.freezeTile();
            Set<Tile> neighbors = currentTile.neighboringTiles;
            for (Tile neighbor : neighbors) {
                if (frozenTiles.contains(currentTile)) {
                    continue;
                }
                Integer currentIndex = 0;
                Integer neighborIndex = 0;
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (currentTile.sideValues[i].equals(neighbor.sideValues[j])) {
                            currentIndex = i;
                            neighborIndex = j;
                        }
                    }
                }
                nextTiles.add(neighbor);
                attachTiles(currentTile, neighbor, currentIndex, neighborIndex);
            }
            frozenTiles.add(currentTile);
            currentTile = nextTiles.pollFirst();
        }
        return createPicture(frozenTiles);
    }

    String[] getGridRow(Tile currentTile, Integer row) {
        if (currentTile.eastTile == null) {
            return currentTile.grid[row];
        } else {
            String[] tmp = getGridRow(currentTile.eastTile, row);
            Integer totalLength = currentTile.grid[row].length + tmp.length;
            String[] finalRow = new String[totalLength];
            for (int i = 0; i < currentTile.grid[row].length; i++) {
                finalRow[i] = currentTile.grid[row][i];
            }
            for (int i = 0; i < tmp.length; i++) {
                finalRow[i+currentTile.grid[row].length] = tmp[i];
            }
            return finalRow;
        }
    }

    String[][] createPicture(Set<Tile> arrangedTiles) {
        Tile startingTile = arrangedTiles.stream()
                .filter(t -> t.numberOfNeighbors().equals(2))
                .filter(t -> t.eastTile != null)
                .filter(t -> t.southTile != null)
                .collect(Collectors.toList()).get(0);
        Tile currentTile = startingTile;
        String[][] tmp = new String[currentTile.grid.length*12][currentTile.grid.length*12];
        Integer rowNumber = 0;
        while (currentTile != null) {
            for (int i = 0; i < currentTile.grid.length; i++) {
                tmp[rowNumber+i] = getGridRow(currentTile, i);
            }
            rowNumber += currentTile.grid.length;
            currentTile = currentTile.southTile;
        }
        return tmp;
    }

    void attachTiles(Tile currentTile, Tile otherTile, Integer currentIndex, Integer otherIndex) {
        if (currentIndex == 0) {
            currentTile.northTile = otherTile;
            otherTile.southTile = currentTile;
        } else if (currentIndex == 1) {
            currentTile.westTile = otherTile;
            otherTile.eastTile = currentTile;
        } else if (currentIndex == 2) {
            currentTile.southTile = otherTile;
            otherTile.northTile = currentTile;
        } else if (currentIndex == 3) {
            currentTile.eastTile = otherTile;
            otherTile.westTile = currentTile;
        } else if (currentIndex == 4) {
            currentTile.northTile = otherTile;
            otherTile.southTile = currentTile;
        } else if (currentIndex == 5) {
            currentTile.westTile = otherTile;
            otherTile.eastTile = currentTile;
        } else if (currentIndex == 6) {
            currentTile.southTile = otherTile;
            otherTile.northTile = currentTile;
        } else {
            currentTile.eastTile = otherTile;
            otherTile.westTile = currentTile;
        }
        List<List<Integer>> doNothing = List.of(List.of(0,2), List.of(1,3), List.of(2,0), List.of(3,1), List.of(4,6), List.of(5,7), List.of(6,4), List.of(7,5));
        List<List<Integer>> topGoesDn = List.of(List.of(0,0), List.of(1,7), List.of(2,2), List.of(3,5), List.of(4,4), List.of(5,3), List.of(6,6), List.of(7,1));
        List<List<Integer>> rotContCW = List.of(List.of(0,1), List.of(1,6), List.of(2,3), List.of(3,4), List.of(4,5), List.of(5,2), List.of(6,7), List.of(7,0));
        List<List<Integer>> rotCWLbRi = List.of(List.of(0,3), List.of(1,2), List.of(2,1), List.of(3,0), List.of(4,7), List.of(5,6), List.of(6,5), List.of(7,4));
        List<List<Integer>> rotRgtTwo = List.of(List.of(0,4), List.of(1,5), List.of(2,6), List.of(3,7), List.of(4,0), List.of(5,1), List.of(6,2), List.of(7,3));
        List<List<Integer>> rotCWtgDn = List.of(List.of(0,5), List.of(1,4), List.of(2,7), List.of(3,6), List.of(4,1), List.of(5,0), List.of(6,3), List.of(7,2));
        List<List<Integer>> rightGLft = List.of(List.of(0,6), List.of(1,1), List.of(2,4), List.of(3,3), List.of(4,2), List.of(5,5), List.of(6,0), List.of(7,7));
        List<List<Integer>> rotateCWs = List.of(List.of(0,7), List.of(1,0), List.of(2,5), List.of(3,2), List.of(4,3), List.of(5,4), List.of(6,1), List.of(7,6));
        List<Integer> options = List.of(currentIndex, otherIndex);
        if (doNothing.contains(options)) {
            otherTile.freezeTile();
            return;
        }
        if (topGoesDn.contains(options)){
            otherTile.topBecomesBottom();
            otherTile.freezeTile();
            return;
        }
        if (rotContCW.contains(options)){
            otherTile.rotateCounterClockwise();
            otherTile.freezeTile();
            return;
        }
        if (rotCWLbRi.contains(options)){
            otherTile.rotateClockwise();
            otherTile.rightBecomesLeft();
            otherTile.freezeTile();
            return;
        }
        if (rotRgtTwo.contains(options)){
            otherTile.rotateClockwise();
            otherTile.rotateClockwise();
            otherTile.freezeTile();
            return;
        }
        if (rotCWtgDn.contains(options)){
            otherTile.rotateClockwise();
            otherTile.topBecomesBottom();
            otherTile.freezeTile();
            return;
        }
        if (rightGLft.contains(options)) {
            otherTile.rightBecomesLeft();
            otherTile.freezeTile();
            return;
        }
        if (rotateCWs.contains(options)){
            otherTile.rotateClockwise();
            otherTile.freezeTile();
            return;
        }
    }

    List<Coord2D> monsterLocations(String[][] grid, List<Coord2D> importantPlaces, Integer maxRow, Integer maxCol) {
        List<Coord2D> monsterLoc = new ArrayList<>();
        for (int row = 0; row < grid.length-maxRow; row++) {
            for (int col = 0; col < grid[row].length-maxCol; col++) {
                Boolean monsterFound = true;
                for (Coord2D p : importantPlaces) {
                    if (!grid[row+p.x][col+p.y].equals("#")) {
                        monsterFound = false;
                    }
                }
                if (monsterFound) {
                    monsterLoc.add(new Coord2D(row, col));
                }
            }
        }
        return monsterLoc;
    }

    Boolean correctOrientation(String[][] grid, List<Coord2D> importantPlaces, Integer maxRow, Integer maxCol) {
        return !monsterLocations(grid, importantPlaces, maxRow, maxCol).isEmpty();
    }

    Long solutionPart2(String[][] grid) {
        List<String> seaMonster = List.of("                  # ",
                                          "#    ##    ##    ###",
                                          " #  #  #  #  #  #   ");
        List<Coord2D> importantPlaces = new ArrayList<>();
        Integer row = 0;
        for (String l : seaMonster) {
            String chrs[] = l.split("");
            for (int col = 0; col < chrs.length; col++) {
                if(chrs[col].equals("#")) {
                    importantPlaces.add(new Coord2D(row, col));
                }
            }
            row++;
        }
        Integer maxCols = seaMonster.get(0).length();
        Tile completePicture = new Tile(-1L, grid);
        Boolean seaMonstersFound = correctOrientation(completePicture.grid, importantPlaces, 3, maxCols);
        Integer turn = 1;
        while (!seaMonstersFound) {
            if (turn % 4 == 0) {
                completePicture.topBecomesBottom();
            } else {
                completePicture.rotateClockwise();
            }
            seaMonstersFound = correctOrientation(completePicture.grid, importantPlaces, 3, maxCols);
            turn++;
        }
        completePicture.freezeTile();
        List<Coord2D> monsterPlaces = monsterLocations(completePicture.grid, importantPlaces, 3, maxCols);
        String[][] finalGrid = createFinalGrid(completePicture.grid, monsterPlaces, importantPlaces, 3, maxCols);
        return countWaves(finalGrid);
    }

    String[][] createFinalGrid(String[][] grid, List<Coord2D> monsterPlaces, List<Coord2D> importantPlaces, Integer maxRow, Integer maxCols) {
        Set<Coord2D> justPutZeroes = new HashSet<>();
        for (Coord2D mp : monsterPlaces) {
            for (Coord2D ip: importantPlaces) {
                justPutZeroes.add(mp.add(ip));
            }
        }
        String[][] newGrid = new String[grid.length][grid[0].length];
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (justPutZeroes.contains(new Coord2D(row, col))) {
                    newGrid[row][col] = "O";
                } else {
                    newGrid[row][col] = grid[row][col];
                }

            }
        }
        return newGrid;
    }

    Long countWaves(String[][] grid) {
        Long count = 0L;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col].equals("#")) {
                    count++;
                }
            }
        }
        return count;
    }
}

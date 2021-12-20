package com.dwelguisz.year2021.helper.day19;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Scanner {
    private Integer id;
    private Set<Beacon> beacons;
    private List<Scanner> alternateScanners;
    private Integer alternateScannerIdx;

    private static Integer[][][] ROTATIONS = {
            {{-1,0,0},{0,-1,0},{0,0,1}},
            {{-1,0,0},{0,0,-1},{0,-1,0}},
            {{-1,0,0},{0,0,1},{0,1,0}},
            {{-1,0,0},{0,1,0},{0,0,-1}},
            {{0,-1,0},{-1,0,0},{0,0,-1}},
            {{0,-1,0},{0,0,-1},{1,0,0}},
            {{0,-1,0},{0,0,1},{-1,0,0}},
            {{0,-1,0},{1,0,0},{0,0,1}},
            {{0,0,-1},{-1,0,0},{0,1,0}},
            {{0,0,-1},{0,-1,0},{-1,0,0}},
            {{0,0,-1},{0,1,0},{1,0,0}},
            {{0,0,-1},{1,0,0},{0,-1,0}},
            {{0,0,1},{-1,0,0},{0,-1,0}},
            {{0,0,1},{0,-1,0},{1,0,0}},
            {{0,0,1},{0,1,0},{-1,0,0}},
            {{0,0,1},{1,0,0},{0,1,0}},
            {{0,1,0},{-1,0,0},{0,0,1}},
            {{0,1,0},{0,0,-1},{-1,0,0}},
            {{0,1,0},{0,0,1},{1,0,0}},
            {{0,1,0},{1,0,0},{0,0,-1}},
            {{1,0,0},{0,-1,0},{0,0,-1}},
            {{1,0,0},{0,0,-1},{0,1,0}},
            {{1,0,0},{0,0,1},{0,-1,0}},
            {{1,0,0},{0,1,0},{0,0,1}}
    };

    public Scanner (Integer id) {
        this.id = id;
        beacons = new HashSet<>();
        alternateScanners = new ArrayList<>();
        alternateScannerIdx = -1;
    }

    public Scanner clone() {
        Scanner newScanner = new Scanner(this.id);
        newScanner.addAllBeacons(this.beacons);
        newScanner.setAlternateScannerIdx(this.alternateScannerIdx);
        newScanner.setAlternateScanners(this.alternateScanners);
        return newScanner;
    }

    public void setAlternateScanners(List<Scanner> alternateScanners) {
        this.alternateScanners = alternateScanners;
    }
    public void setAlternateScannerIdx(Integer alternateScannerIdx) {
        this.alternateScannerIdx = alternateScannerIdx;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(String.format("--- Scanner %d ----",id));
        return sb.toString();
    }

    public Integer getId() {
        return id;
    }

    public Set<Beacon> getBeacons() {
        return beacons;
    }

    public void addNewBeacon(Beacon beacon) {
        beacons.add(beacon);
    }

    public List<Scanner> getAlternateScanners() {
        return alternateScanners;
    }

    public void generateAlternateOrientation() {
        for (Integer[][] rot : ROTATIONS) {
            Scanner newScanner = new Scanner(id);
            for (Beacon beacon : beacons) {
                newScanner.addNewBeacon(doRotation(beacon, rot[0], rot[1], rot[2]));
            }
            alternateScanners.add(newScanner);
        }
    }

    private Beacon doRotation(Beacon beacon, Integer[] xRotation, Integer[] yRotation, Integer[] zRotation) {
        Integer newX = beacon.getX() * xRotation[0] + beacon.getY() + xRotation[1] + beacon.getZ() * xRotation[2];
        Integer newY = beacon.getX() * yRotation[0] + beacon.getY() + yRotation[1] + beacon.getZ() * yRotation[2];
        Integer newZ = beacon.getX() * zRotation[0] + beacon.getY() + zRotation[1] + beacon.getZ() * zRotation[2];
        return new Beacon(newX, newY, newZ);
    }

    public void addAllBeacons(Set<Beacon> beacons) {
        beacons.addAll(beacons);
    }


//    public static class Coordinate {
//        public int x,y,z;
//        public List<Double> relativeDistances;
//        private Coordinate(String line) {
//            var split = line.split(",");
//            x = Integer.parseInt(split[0]);
//            y = Integer.parseInt(split[1]);
//            z = Integer.parseInt(split[2]);
//            relativeDistances = new ArrayList<>();
//        }
//
//        public Coordinate(int x, int y, int z) {
//            this.x = x;
//            this.y = y;
//            this.z = z;
//            relativeDistances = new ArrayList<>();
//        }
//
//        public Integer manhattanDistance(Coordinate c) {
//            return Math.abs(x - c.x) + Math.abs(y - c.y) + Math.abs(z - c.z);
//        }
//
//        public void addDistance(Coordinate c) {
//            Double value = Math.sqrt(Math.pow(c.x -x, 2) + Math.pow(c.y-y,2) + Math.pow(c.z-z,2));
//            relativeDistances.add(value);
//        }
//
//        public Coordinate determineNewPosition
//    }

}

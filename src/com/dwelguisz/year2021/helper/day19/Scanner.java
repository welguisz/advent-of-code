package com.dwelguisz.year2021.helper.day19;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Scanner {
    private Integer id;
    private Set<Coordinate> beacons;
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

    public Set<Coordinate> getBeacons() {
        return beacons;
    }

    public void addNewBeacon(Coordinate beacon) {
        beacons.add(beacon);
    }

    public List<Scanner> getAlternateScanners() {
        return alternateScanners;
    }

    public void generateAlternateOrientation() {
        for (Integer[][] rot : ROTATIONS) {
            Scanner newScanner = new Scanner(id);
            for (Coordinate beacon : beacons) {
                newScanner.addNewBeacon(doRotation(beacon, rot[0], rot[1], rot[2]));
            }
            alternateScanners.add(newScanner);
        }
    }

    private Coordinate doRotation(Coordinate beacon, Integer[] xRotation, Integer[] yRotation, Integer[] zRotation) {
        Integer newX = beacon.x * xRotation[0] + beacon.y + xRotation[1] + beacon.z * xRotation[2];
        Integer newY = beacon.x * yRotation[0] + beacon.y + yRotation[1] + beacon.z * yRotation[2];
        Integer newZ = beacon.x * zRotation[0] + beacon.y + zRotation[1] + beacon.z * zRotation[2];
        return new Coordinate(newX, newY, newZ);
    }

    public void addAllBeacons(Set<Coordinate> beacons) {
        beacons.addAll(beacons);
    }

    
}

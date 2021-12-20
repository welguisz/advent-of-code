package com.dwelguisz.year2021.helper.day19;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Scanner {
    private Integer id;
    private Set<Coordinate> beacons;
    private List<Scanner> alternateScanners;
    private Integer alternateScannerIdx;
    private Coordinate scannerLocation;

    private static Integer[][] ROTATIONS = {
            {0,1,2,1,1,1},
            {0,1,2,1,1,-1},
            {0,1,2,1,-1,1},
            {0,1,2,1,-1,-1},
            {0,1,2,-1,1,1},
            {0,1,2,-1,1,-1},
            {0,1,2,-1,-1,1},
            {0,1,2,-1,-1,-1},

            {0,2,1,1,1,1},
            {0,2,1,1,1,-1},
            {0,2,1,1,-1,1},
            {0,2,1,1,-1,-1},
            {0,2,1,-1,1,1},
            {0,2,1,-1,1,-1},
            {0,2,1,-1,-1,1},
            {0,2,1,-1,-1,-1},

            {1,0,2,1,1,1},
            {1,0,2,1,1,-1},
            {1,0,2,1,-1,1},
            {1,0,2,1,-1,-1},
            {1,0,2,-1,1,1},
            {1,0,2,-1,1,-1},
            {1,0,2,-1,-1,1},
            {1,0,2,-1,-1,-1},

            {1,2,0,1,1,1},
            {1,2,0,1,1,-1},
            {1,2,0,1,-1,1},
            {1,2,0,1,-1,-1},
            {1,2,0,-1,1,1},
            {1,2,0,-1,1,-1},
            {1,2,0,-1,-1,1},
            {1,2,0,-1,-1,-1},

            {2,0,1,1,1,1},
            {2,0,1,1,1,-1},
            {2,0,1,1,-1,1},
            {2,0,1,1,-1,-1},
            {2,0,1,-1,1,1},
            {2,0,1,-1,1,-1},
            {2,0,1,-1,-1,1},
            {2,0,1,-1,-1,-1},

            {2,1,0,1,1,1},
            {2,1,0,1,1,-1},
            {2,1,0,1,-1,1},
            {2,1,0,1,-1,-1},
            {2,1,0,-1,1,1},
            {2,1,0,-1,1,-1},
            {2,1,0,-1,-1,1},
            {2,1,0,-1,-1,-1},

    };

    public Scanner (Integer id) {
        this.id = id;
        beacons = new HashSet<>();
        alternateScanners = new ArrayList<>();
        alternateScannerIdx = -1;
        scannerLocation = new Coordinate(0,0,0);
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

    public void setScannerLocation(Coordinate scannerLocation) {
        this.scannerLocation = scannerLocation;
    }

    public Coordinate getScannerLocation() {
        return scannerLocation;
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
        beacons.forEach(b ->
                beacons.forEach(c -> {
                        if (b != c) {
                            beacon.addDistance(c);
                        }
        }));
    }

    public List<Scanner> getAlternateScanners() {
        return alternateScanners;
    }

    public void generateAlternateOrientation() {
        Integer altId = 0;
        for (Integer[] rot : ROTATIONS) {
            Scanner newScanner = new Scanner(altId);
            for (Coordinate beacon : beacons) {
                newScanner.addNewBeacon(doRotation(beacon, rot));
            }
            alternateScanners.add(newScanner);
            altId++;
        }
    }

    public Scanner add(Coordinate o) {
        Scanner movedScanner = new Scanner(id);
        movedScanner.addAllBeacons(beacons.stream().map(c -> new Coordinate(c.x+o.x,c.y+o.y,c.z+o.z)).collect(Collectors.toSet()));
        movedScanner.setScannerLocation(o);
        return movedScanner;
    }

    private Coordinate doRotation(Coordinate beacon, Integer[] rotation) {
        Integer newX = beacon.getValue(rotation[0]);
        Integer newY = beacon.getValue(rotation[1]);
        Integer newZ = beacon.getValue(rotation[2]);
        newX *= rotation[3];
        newY *= rotation[4];
        newZ *= rotation[5];
        return new Coordinate(newX, newY, newZ);
    }


    public void addAllBeacons(Set<Coordinate> beacons) {
        this.beacons.addAll(beacons);
        this.beacons.forEach(b -> {
            this.beacons.forEach(c -> {
                if (b != c) {
                    b.addDistance(c);
                }
            });
        });
    }

    
}

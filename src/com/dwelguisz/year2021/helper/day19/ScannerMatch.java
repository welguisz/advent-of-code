package com.dwelguisz.year2021.helper.day19;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
public class ScannerMatch {
    private Beacon beaconOffset;
    private Scanner removeScanner;
    private Set<Beacon> beacons;
}

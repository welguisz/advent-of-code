package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.AoCYear;

public class RetrieveTheKeys extends AoCYear {

    public RetrieveTheKeys(int year) {
        super(year);
    }

    @Override
    public void runOneDay(int day) {
        AoCDay aoCDay = new AoCDay();
        switch(day) {
            case 1: {
                aoCDay = new SonarSweep();
                break;
                }
            case 2: {
                aoCDay = new Dive();
                break;
            }
            case 3: {
                aoCDay = new BinaryDiagnostic();
                break;
            }
            case 4 : {
                aoCDay = new GiantSquid();
                break;
            }
            case 5: {
                aoCDay = new HydrothermalVenture();
                break;
            }
            case 6 : {
                aoCDay = new LanternFish();
                break;
            }
            case 7: {
                aoCDay = new TreacheryOfWhales();
                break;
            }
            case 8: {
                aoCDay = new SevenSegmentSearch();
                break;
            }
            case 9: {
                aoCDay = new SmokeBasin();
                break;
            }
            case 10: {
                aoCDay = new SyntaxScoring();
                break;
            }
            case 11: {
                aoCDay = new DumboOctopus();
                break;
            }
            case 12: {
                aoCDay = new PassagePathing();
                break;
            }
            case 13: {
                aoCDay = new TransparentOrigami();
                break;
            }
            case 14: {
                aoCDay = new ExtendedPolymerization();
                break;
            }
            case 18: {
                aoCDay = new Snailfish();
                break;
            }
            case 19: {
                aoCDay = new BeaconScanner();
                break;
            }
            case 20: {
                aoCDay = new TrenchMap();
                break;
            }
            case 21: {
                aoCDay = new DiracDice();
                break;
            }
            case 22 : {
                aoCDay = new ReactorReboot();
                break;
            }
            case 25: {
                aoCDay = new SeaCucumber();
                break;
            }
        }
        aoCDay.solve();
    }
}

package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class BinaryDiagnostic extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> instructions = readResoruceFile(2021,3,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        int oxygen = getRating(instructions, '1', true);
        int carbondioxide = getRating(instructions, '0', false);
        part1Answer = calculateBinary(instructions);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = oxygen * carbondioxide;
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    static private Integer getRating(final List<String> binaryValues, final char midVal, final boolean oxygen) {
        List<String> values = binaryValues;
        StringBuffer sb = new StringBuffer();
        int pos = 0;
        while (values.size() > 1) {
            values = calculateReduceNumbers(values, pos, midVal, oxygen);
            pos++;
        }
        return parseInt(values.get(0), 2);
    }

    static private List<String> calculateReduceNumbers(final List<String> values, int pos, char midVal, boolean oxygen) {
        List<Integer> positionCountis0 = new ArrayList<>();
        List<Integer> positionCountis1 = new ArrayList<>();
        for (int i = 0; i< values.get(0).length(); i++) {
            positionCountis0.add(0);
            positionCountis1.add(0);
        }
        for(int i = 0; i < values.size(); i++) {
            for(int j=0;j < values.get(i).length(); j++) {
                if (values.get(i).charAt(j) == '0') {
                    positionCountis0.set(j, positionCountis0.get(j) + 1);
                } else {
                    positionCountis1.set(j, positionCountis1.get(j) + 1);
                }
            }
        }
        char importVar = ' ';
        if (oxygen && (positionCountis0.get(pos) > positionCountis1.get(pos))) {
            importVar = '0';
        } else if (!oxygen &&(positionCountis0.get(pos) > positionCountis1.get(pos))) {
            importVar = '1';
        } else if (positionCountis0.get(pos) == positionCountis1.get(pos)) {
            importVar = midVal;
        } else if (oxygen && (positionCountis0.get(pos) < positionCountis1.get(pos))){
            importVar = '1';
        } else {
            importVar = '0';
        }
        List<String> reducedValues = new ArrayList<>();
        for(int i = 0; i < values.size(); i++) {
            if(values.get(i).charAt(pos) == importVar) {
                reducedValues.add(values.get(i));
            }
        }
        return reducedValues;
    }

    static private Integer calculateBinary(final List<String> instructions) {
        List<Integer> positionCountis0 = new ArrayList<>();
        List<Integer> positionCountis1 = new ArrayList<>();
        for (int i = 0; i< instructions.get(0).length(); i++) {
            positionCountis0.add(0);
            positionCountis1.add(0);
        }
        for(int i = 0; i < instructions.size(); i++) {
            for(int j=0;j < instructions.get(i).length(); j++) {
                if (instructions.get(i).charAt(j) == '0') {
                    positionCountis0.set(j, positionCountis0.get(j) + 1);
                } else {
                    positionCountis1.set(j, positionCountis1.get(j) + 1);
                }
            }
        }
        StringBuffer gamma = new StringBuffer();
        StringBuffer epsilon = new StringBuffer();
        for(int i = 0; i < instructions.get(0).length(); i++) {
            if (positionCountis0.get(i) < positionCountis1.get(i)) {
                gamma.append("0");
                epsilon.append("1");
            } else {
                gamma.append("1");
                epsilon.append("0");
            }
        }
        int finalGamma = parseInt(gamma.toString(), 2);
        int finalEpsilon = parseInt(epsilon.toString(), 2);
        return finalGamma * finalEpsilon;
    }


}

package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.List;

public class TwoFactorAuthentication extends AoCDay {
    Boolean[][] screen;
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2016,8,false,0);
        loadScreen(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = printScreen();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public String printScreen() {
        StringBuffer tmp = new StringBuffer("\n");
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 50; j++) {
                if (screen[i][j]) {
                    tmp.append("#");
                } else {
                    tmp.append(" ");
                }
            }
            tmp.append("\n");
        }
        return tmp.toString();
    }

    public void loadScreen(List<String> lines) {
        screen = new Boolean[6][50];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 50; j++) {
                screen[i][j] = false;
            }
        }
        for(String line : lines) {
            String[] words = line.split(" ");
            if (words[0].equals("rect")) {
                String[] dims = words[1].split("x");
                Integer wide = Integer.parseInt(dims[0]);
                Integer tall = Integer.parseInt(dims[1]);
                for (int i = 0; i < tall; i++) {
                    for (int j = 0; j < wide; j++) {
                        screen[i][j] = true;
                    }
                }
            } else { //rotate
                Integer shift = Integer.parseInt(words[4]);
                String[] locs = words[2].split("=");
                Integer loc = Integer.parseInt(locs[1]);
                if (words[1].equals("column")) {
                    shiftDown(loc, shift);
                } else { //row
                    shiftRight(loc, shift);
                }
            }
        }
    }

    private void shiftRight(int columnNumber, int shift) {
        Boolean[] temp = new Boolean[50];
        for (int i = 0 ; i < 50; i++) {
            temp[i] = screen[columnNumber][i];
        }
        for (int i = 0; i < 50; i++) {
            if (shift >= 50) {
                shift -= 50;
            }
            screen[columnNumber][shift] = temp[i];
            shift++;
        }
    }

    private void shiftDown(int rowNumber, int shift) {
        Boolean[] temp = new Boolean[6];
        for (int i = 0; i < 6; i++) {
            temp[i] = screen[i][rowNumber];
        }
        for (int i = 0; i < 6; i++) {
            if (shift >= 6) {
                shift -= 6;
            }
            screen[shift][rowNumber] = temp[i];
            shift++;
        }
    }

    public Integer solutionPart1() {
        Integer count = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 50; j++) {
                if(screen[i][j]) {
                    count += 1;
                }
            }
        }
        return count;
    }
}

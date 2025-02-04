package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChronalConversion extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,21,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

//    #ip: 2
//    00: seti 123 0 3   --> reg[3] = 123   //01111011 --> 7b
//    01: bani 3 456 3   --> reg[3] = reg[3] & 456; // --> 7b & 148 --> 48 (72) // always
//    02: eqri 3 72 3    --> reg[3] = (reg[3] == 72) ? 1 : 0;  so this is 1
//    03: addr 3 2 2     --> reg[2] = reg[2] + reg[3] -> ip becomes 5
//    04: seti 0 0 2
//    05: seti 0 5 3        -->  reg[3] = 0
//    06: bori 3 65536 1    -->  reg[1] = reg[3] | 65536
//    07: seti 10373714 2 3 -->  reg[3] = 10373714
//    08: bani 1 255 5      -->  reg[5] = reg[1] & 255;
//    09: addr 3 5 3        -->  reg[3] = reg[3] + reg[5]
//    10: bani 3 16777215 3 -->  reg[3] = reg[3] & 16777215
//    11: muli 3 65899 3    -->  reg[3] = reg[3] * 65899;
//    12: bani 3 16777215 3 -->  reg[3] = reg[3] & 16777215
//    13: gtir 256 1 5      -->  reg[5] = (256 > reg[1]) ? 1 : 0
//    14: addr 5 2 2        -->  reg[2] = reg[2] + 1;  (if reg[1] is greater than 256, skip next step, else go to next step (line 16)
//    15: addi 2 1 2        -->  reg[2] = reg[2] + 1;  Go to line 17
//    16: seti 27 7 2       -->  reg[2] = 27; go to line 28.
//    17: seti 0 3 5        -->  reg[5] = 0;
//    18: addi 5 1 4        -->  reg[4] = reg[5] + 1
//    19: muli 4 256 4      -->  reg[4] = reg[4] * 256;
//    20: gtrr 4 1 4        -->  reg[4] = (reg[4] > reg[1]) ? 1: 0
//    21: addr 4 2 2        -->  reg[2] = reg[2] + reg[4]  (if reg[4] is greater than reg1, go to line 23)
//    22: addi 2 1 2        -->  reg[2] = reg[2] + 1 (go to line 24)
//    23: seti 25 4 2       -->  reg[2] = 25 // go to line 25
//    24: addi 5 1 5        -->  reg[5] = reg[5] + 1;
//    25: seti 17 0 2       -->  reg[2] = 17; go to line 17
//    26: setr 5 2 1        -->  reg[1] = reg[5]
//    27: seti 7 4 2        -->  reg[2] = 7; go to line 7
//    28: eqrr 3 0 5        -->  reg[5] = (reg[3] == reg[0]) ? 1: 0
//    29: addr 5 2 2        -->  reg[2] = reg[5] + reg[2]  //ends programs if reg[3] == reg[0]
//    30: seti 5 7 2        -->  reg[2] = 5 // go to line 5

    public Long solutionPart1(List<String> lines) {
        Long registers[] = new Long[]{0L,0L,0L,0L,0L,0L};
        Long line7Number = Long.parseLong(lines.get(7).split(" ")[2]);
        Long line8Number = Long.parseLong(lines.get(8).split(" ")[1]);
        Long line9Number = Long.parseLong(lines.get(9).split(" ")[2]);
        Long line11Number = Long.parseLong(lines.get(11).split(" ")[2]);
        Long line12Number = Long.parseLong(lines.get(12).split(" ")[2]);
        Long line13Number = Long.parseLong(lines.get(13).split(" ")[2]);
        Long line20Number = Long.parseLong(lines.get(20).split(" ")[2]);
        registers[3] = 0L;  //line 5
        //    05: seti 0 5 3        -->  reg[3] = 0
        //    06: bori 3 65536 1    -->  reg[1] = reg[3] | 65536

        while (true) {
            registers[1] = registers[3] | line7Number;   //bori 3 65536 1
            registers[3] = line8Number;    //seti 10373714 2 3
            while (true) {
                registers[5] = registers[1] & line9Number;   //bani 1 255 5
                registers[3] = registers[3] + registers[5];  // addr 3 5 3
                registers[3] = registers[3] & line11Number;  // bani 3 16777215 3
                registers[3] = registers[3] * line12Number;  //muli 3 65899 3
                registers[3] = registers[3] & line13Number;  // bani 3 16777215 3
                if (registers[1] < 256) {    //gtir 256 1 5
                    break;
                }
                registers[1] = registers[1] / 256;
            }
            return registers[3];
        }
    }

    public Long solutionPart2(List<String> lines) {
        Long registers[] = new Long[]{0L,0L,0L,0L,0L,0L};
        Long line7Number = Long.parseLong(lines.get(7).split(" ")[2]);
        Long line8Number = Long.parseLong(lines.get(8).split(" ")[1]);
        Long line9Number = Long.parseLong(lines.get(9).split(" ")[2]);
        Long line11Number = Long.parseLong(lines.get(11).split(" ")[2]);
        Long line12Number = Long.parseLong(lines.get(12).split(" ")[2]);
        Long line13Number = Long.parseLong(lines.get(13).split(" ")[2]);
        registers[3] = 0L;  //line 5
        Set<Long> values = new HashSet<>();
        Long lastSeen = 0L;
        while (true) {
            registers[1] = registers[3] | line7Number;   //bori 3 65536 1
            registers[3] = line8Number;    //seti 10373714 2 3
            while (true) {
                registers[5] = registers[1] & line9Number;   //bani 1 255 5
                registers[3] = registers[3] + registers[5];  // addr 3 5 3
                registers[3] = registers[3] & line11Number;  // bani 3 16777215 3
                registers[3] = registers[3] * line12Number;  //muli 3 65899 3
                registers[3] = registers[3] & line13Number;  // bani 3 16777215 3
                if (registers[1] < 256) {    //gtir 256 1 5
                    break;
                }
                registers[1] = registers[1] / 256;
            }
            if (values.contains(registers[3])) {
                return lastSeen;
            }
            values.add(registers[3]);
            lastSeen = registers[3];
        }
    }

}

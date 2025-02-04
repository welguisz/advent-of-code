package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class GoWithTheFlow extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,19,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Long solutionPart1(List<String> lines) {
        WristDevice wristDevice = new WristDevice(true);
        List<String> trueProgram = new ArrayList<>(lines);
        String setIP = trueProgram.remove(0);
        wristDevice.setProgramCounterToReg(Long.parseLong(setIP.split(" ")[1]));
        wristDevice.setProgramStr(trueProgram);
        wristDevice.run();
        return wristDevice.registers.get(0L);
    }

    //How I did this part:
    // * Added a heatMap and saw that certain instructions were getting hit hard, these lines were 3,4,5,6,8,9,10,11
    // * This indicated that there was a loop there.
    // * What was the loop doing.
    // * Basically it was
    //     for (int x =1; x < reg4; x++) {
    //        for (int y = 1; y < reg4; y++) {
    //            if (x*y == reg4) {
    //                reg0 += x;
    //            }
    //         }
    //     }
    // So this could be reduced to the following:
    //     factorSum = 0L;
    //     for (int x = 1; x < reg4; x++) {
    //         if (reg4 % x == 0) {
    //             factorSum += x;
    //         }
    //     }
    //     return factorSum
    Long solutionPart2(List<String> lines) {
        WristDevice wristDevice = new WristDevice(true);
        List<String> trueProgram = new ArrayList<>(lines);
        String setIP = trueProgram.remove(0);
        wristDevice.setProgramCounterToReg(Long.parseLong(setIP.split(" ")[1]));
        wristDevice.setProgramStr(trueProgram);
        wristDevice.registers.put(0L,1L);
        wristDevice.setStopCount(20);
        //wristDevice.setStopCount(8*10551347+100);
        wristDevice.run();
        //List<Long> regs = LongStream.range(0,6).boxed().map(l -> wristDevice.registers.get(l)).collect(Collectors.toList());
        //System.out.println(String.format("%d: %s",-1,regs));

        //for (int i = 0; i < trueProgram.size(); i++) {
        //    System.out.println(String.format("Instruction %d: %d",i,wristDevice.heatMap.get(i)));
        //}
        Long number = wristDevice.registers.get(4L);
        Long factorCount = 0L;
        for (Long factor = 1L; factor <= number; factor++) {
            if (number % factor == 0) {
                factorCount+=factor;
            }
        }
        return factorCount;
    }
}

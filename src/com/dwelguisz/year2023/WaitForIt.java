package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public class WaitForIt extends AoCDay {
    public void solve() {
        List<Pair<Long, Long>> records = new ArrayList<>();
        records.add(Pair.of(44L,202L));
        records.add(Pair.of(82L,1076L));
        records.add(Pair.of(69L, 1138L));
        records.add(Pair.of(81L, 1458L));
        List<Pair<Long,Long>> oneRecord = new ArrayList<>();
        oneRecord.add(Pair.of(44826981L, 202107611381458L));
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 6, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(records);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart1(oneRecord);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Long solutionPart1(List<Pair<Long,Long>> records) {
        List<Long> values = new ArrayList<>();
        for (Pair<Long, Long> record : records) {
            values.add(LongStream.range(0L,record.getLeft()).map(time -> (record.getLeft() - time) * time)
                    .filter(v -> v > record.getRight())
                    .count());
        }
        return values.stream().reduce(1L, (a,b) -> a*b);
    }

    public Long solutionPart2() {
        return 0L;
    }

}

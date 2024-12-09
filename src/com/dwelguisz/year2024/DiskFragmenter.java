package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;
import java.util.stream.LongStream;

public class DiskFragmenter extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 9, false, 0);
        String[] diskmap = lines.get(0).split("");
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(diskmap, false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart1(diskmap, true);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    long solutionPart1(String[] diskmap, boolean part2) {
        Map<Long, Pair<Long, Long>> files = new HashMap<>();
        Map<Long, Long> disk = new HashMap<>();
        Long diskLoc = 0l;
        Long fileId = 0l;
        boolean usedSpace = true;
        for (String digit : diskmap) {
            Long length = Long.parseLong(digit);
            if (usedSpace) {
                files.put(fileId, Pair.of(diskLoc, length));
                for (int i = 0; i < length; i++) {
                    disk.put(diskLoc + i, fileId);
                }
                fileId++;
            }
            usedSpace ^= true;
            diskLoc += length;
        }
        if (part2) {
            List<Long> filesToCompact = new ArrayList<>(LongStream.range(0, fileId).boxed().toList());
            Collections.reverse(filesToCompact);
            for (Long fileIdCompact : filesToCompact) {
                Long insertPos = 0l;
                while (insertPos < files.get(fileIdCompact).getLeft()) {
                    final Long iP = insertPos;;
                    if (LongStream.range(0, files.get(fileIdCompact).getRight()).allMatch(l -> !disk.containsKey(l + iP))) {
                        LongStream.range(0, files.get(fileIdCompact).getRight()).forEach(i -> {
                            disk.remove(files.get(fileIdCompact).getLeft() + i);
                            disk.put(iP + i, fileIdCompact);
                        });
                        break;
                    } else {
                        insertPos++;
                    }
                }
            }

        } else {
            long left = 0;
            long right = disk.keySet().stream().max(Long::compare).get();
            while (left < right) {
                if (disk.containsKey(right)) {
                    Long copyFileId = disk.get(right);
                    disk.remove(right);
                    while (disk.containsKey(left)) {
                        left++;
                    }
                    disk.put(left, copyFileId);
                }
                right--;
            }
        }
        return disk.entrySet().stream().map(e -> e.getKey() * e.getValue()).reduce(0l, Long::sum);
    }
}

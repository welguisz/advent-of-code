package com.dwelguisz.year2024;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;
import java.util.stream.LongStream;

public class DiskFragmenter extends AoCDay {
    Map<Long, Long> diskG;
    Map<Long, Pair<Long, Long>> filesG;
    List<PriorityQueue<Long>> freeSpace;
    Long fileId;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2024, 9, false, 0);
        String[] diskmap = lines.get(0).split("");
        createPartitions(diskmap);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(new HashMap<>(diskG));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(diskG, filesG, freeSpace);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    void createPartitions(String[] diskmap) {
        diskG = new HashMap<>();
        filesG = new HashMap<>();
        freeSpace = new ArrayList<>();
        for (long i = 0; i < 10; i++) {
            freeSpace.add(new PriorityQueue<>(500, (b,a) -> Math.toIntExact(b - a)));
        }
        Long diskLoc = 0L;
        fileId = 0L;
        boolean usedSpace = true;
        for (String digit : diskmap) {
            long length = Long.parseLong(digit);
            if (usedSpace) {
                filesG.put(fileId, Pair.of(diskLoc, length));
                for (int i = 0; i < length; i++) {
                    diskG.put(diskLoc + i, fileId);
                }
                fileId++;
            } else {
                if (length > 0L) {
                    PriorityQueue<Long> tmp = freeSpace.get(Math.toIntExact(length));
                    tmp.add(diskLoc);
                }
            }
            usedSpace ^= true;
            diskLoc += length;
        }
    }

    long solutionPart1(Map<Long, Long> disk) {
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
        return disk.entrySet().stream()
                .map(e -> e.getKey() * e.getValue())
                .reduce(0L, Long::sum);
    }

    long solutionPart2(Map<Long, Long> disk, Map<Long, Pair<Long, Long>> files, List<PriorityQueue<Long>> freeSpace) {
        List<Long> filesToCompact = new ArrayList<>(LongStream.range(0, fileId).boxed().toList());
        Collections.reverse(filesToCompact);
        int lcv = 0;
        while (lcv < filesToCompact.size()) {
            Long fileIdCompact = filesToCompact.get(lcv);
            Pair<Long, Long> fileInfo = files.get(fileIdCompact);
            Long fileSize = fileInfo.getRight();
            int sizeToUse = -1;
            long smallestLoc = Long.MAX_VALUE;
            for (int i = Math.toIntExact(fileSize); i < 10; i++) {
                PriorityQueue<Long> tmp = freeSpace.get(i);
                if (!tmp.isEmpty()) {
                    Long nextLoc = tmp.peek();
                    if (nextLoc < smallestLoc) {
                        smallestLoc = nextLoc;
                        sizeToUse = i;
                    }
                }
            }
            if (sizeToUse > -1) {
                PriorityQueue<Long> tmp = freeSpace.get(sizeToUse);
                final long insertPosition = tmp.peek();
                final long fileLoc = fileInfo.getLeft();
                int remainingSpace = sizeToUse - Math.toIntExact(fileSize);
                if (fileLoc < insertPosition) {
                    lcv++;
                    continue;
                }
                tmp.poll();
                if (remainingSpace > 0) {
                    PriorityQueue<Long> tmp2 = freeSpace.get(remainingSpace);
                    tmp2.add(insertPosition + fileSize);
                }
                LongStream.range(0, files.get(fileIdCompact).getRight()).forEach(i -> {
                    disk.remove(fileLoc + i);
                    disk.put(insertPosition + i, fileIdCompact);
                });
            }
            lcv++;
        }
        return disk.entrySet().stream()
                .map(e -> e.getKey() * e.getValue())
                .reduce(0L, Long::sum);
    }
}

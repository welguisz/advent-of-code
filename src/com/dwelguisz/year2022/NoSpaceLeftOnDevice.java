package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NoSpaceLeftOnDevice extends AoCDay {

    public static class Directory {
        Map<String, Directory> directoryMap;
        Map<String, Long> fileInformation;
        String id;

        public Directory(String id) {
            this.id = id;
            directoryMap = new HashMap<>();
            fileInformation = new HashMap<>();
        }

        public void addSubDirectory(String name) {
            directoryMap.put(name, new Directory(name));
        }

        public void addFileInfo(String name, Long size) {
            fileInformation.put(name, size);
        }

        public List<Long> getAllDirectorySizes() {
            List<Long> sizes = new ArrayList<>();
            for(Map.Entry<String, Directory> d : directoryMap.entrySet()) {
                sizes.addAll(d.getValue().getAllDirectorySizes());
            }
            sizes.add(directorySize());
            return sizes;
        }
        public Long directorySize() {
            Long sum = 0L;
            for(Map.Entry<String, Long> f : fileInformation.entrySet()) {
                sum += f.getValue();
            }
            for(Map.Entry<String, Directory> d : directoryMap.entrySet()) {
                sum += d.getValue().directorySize();
            }
            return sum;
        }
    }


    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2022,7,false,0);
        Directory root = createDirectory(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(root);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(root);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Directory createDirectory(List<String> lines) {
        Directory headDirectory = new Directory("/");
        Pair<Directory, Integer> res = subDirectory(lines, 1, headDirectory);
        return res.getLeft();
    }

    public Pair<Directory, Integer> subDirectory(List<String> lines, Integer pointer, Directory currentDirectory) {
        boolean stayHere = true;
        while (stayHere) {
            String line = lines.get(pointer);
            String cmd[] = line.split(" ");
            if (cmd[0].equals("$")) {
                if (cmd[1].equals("ls")) {
                    pointer++;
                } else if (cmd[1].equals("cd")) {
                    if (cmd[2].equals("..")) {
                        pointer++;
                        return Pair.of(currentDirectory, pointer);
                    } else {
                        String dirName = cmd[2];
                        Directory subDir = currentDirectory.directoryMap.get(dirName);
                        pointer++;
                        Pair<Directory, Integer> res = subDirectory(lines, pointer, subDir);
                        pointer = res.getRight();
                        currentDirectory.directoryMap.put(dirName, res.getLeft());
                    }
                }
            } else {
                if (cmd[0].equals("dir")) {
                    currentDirectory.addSubDirectory(cmd[1]);
                    pointer++;
                } else {
                    currentDirectory.addFileInfo(cmd[1], Long.parseLong(cmd[0]));
                    pointer++;
                }
            }
            if (pointer == lines.size()) {
                stayHere = false;
            }
        }
        return Pair.of(currentDirectory, pointer);
    }

    public Long solutionPart1(Directory root) {
        return root.getAllDirectorySizes().stream().filter(s -> s < 100000).mapToLong(l -> l).sum();
    }

    public Long solutionPart2(Directory root) {
        final Long rootDirectorySize = root.directorySize();
        final Long unusedSpace = 70000000 - rootDirectorySize;
        final Long minSpace = 30000000 - unusedSpace;
        return root.getAllDirectorySizes().stream()
                .filter(l -> !l.equals(rootDirectorySize))
                .mapToLong(l -> l)
                .filter(l -> l > minSpace)
                .min().getAsLong();
    }
}

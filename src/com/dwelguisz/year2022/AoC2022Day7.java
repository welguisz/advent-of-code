package com.dwelguisz.year2022;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AoC2022Day7 extends AoCDay {

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
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2022/day07/input.txt");
        Directory root = createDirectory(lines);
        Long part1 = solutionPart1(root);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Long part2 = solutionPart2(root);
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Directory createDirectory(List<String> lines) {
        Directory headDirectory = new Directory("/");
        Pair<Directory, Integer> res = subNode(lines, 1, headDirectory);
        return res.getLeft();
    }

    public Pair<Directory, Integer> subNode(List<String> screen, Integer pointer, Directory currentDirectory) {
        boolean stayHere = true;
        while (stayHere) {
            String line = screen.get(pointer);
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
                        Pair<Directory, Integer> res = subNode(screen, pointer, subDir);
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
            if (pointer == screen.size()) {
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

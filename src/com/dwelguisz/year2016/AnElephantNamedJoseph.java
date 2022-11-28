package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.List;

public class AnElephantNamedJoseph extends AoCDay {
    public static Integer TEST_ELVES = 5;
    public static Integer MY_ELVES = 3012210;
    public void solve() {
        Integer part1 = solutionPart1(MY_ELVES);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = solutionPart2(MY_ELVES,10000);
        System.out.println(String.format("Part 2 Answer: %d",part2));
        //31682 -- too low

        // 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20.. Elf 1 selects. Elf 11 removed (1,20)
        // 1,2,3,4,5,6,7,8,9,10,12,13,14,15,16,17,18,18,20 .. Elf 2 select. Elf 12 removed (2,19)
        // 1,2,3,4,5,6,7,8,9,10,13,14,15,16,17,18,19,20 .. Elf 3 select. Elf 14 removed (3,18)
        // 1,2,3,4,5,6,7,8,9,10,13,15,16,17,18,19,20 .. Elf 4 select. Elf 15 removed (4,17)
        // 1,2,3,4,5,6,7,8,9,10,13,16,17,18,19,20 .. Elf 5 select, Elf 17 removed (5,16)
        // 1,2,3,4,5,6,7,8,9,10,13,16,18,19,20 .. Elf 6 select, Elf 18 removed (6,15)
        // 1,2,3,4,5,6,7,8,9,10,13,16,19,20 .. ELf 7 select, Elf 20 removed (7, 14)
        // 1,2,3,4,5,6,7,8,9,10,13,16,19 .. Elf 8 select, Elf 1 removed (8, 13)

        // 2,3,4,5,6,7,8,9,10,13,16,19 .. Elf 9 select, Elf 3 removed (8, 12)
        // 3,4,5,6,7,8,9,10,13,16,19 .. Elf 10 select, Elf 4 removed (8, 11)
        // 3,5,6,7,8,9,10,13,16,19 .. Elf 13 select, Elf 6 removed (8,10)
        // 3,5,7,8,9,10,13,16,19 .. ELF 16 select, Elf 7 removed (8, 9)
        // 3,5,8,9,10,13,16,19 .. Elf 19 select, Elf 9 removed (8,8)

        // 3,5,8,10,13,16,19 .. Elf 3 selects, Elf 10 removed (1,7)
        // 3,5,8,13,16,19 .. Elf 5 selects, Elf 16 removed (2,6)
        // 3,5,8,13,19 .. Elf 8 selects, Elf 19 removed (3,5)
        // 3,5,8,13 .. Elf 13 selects, Elf 5 removed (4,4)
        // 3,8,13 .. Elf 3 selects, Elf 8 removed(
        // 3, 13 .. Elf 13 selects. Elf 3 removed
        // 13 wins
    }

    // In the first round, the elves in even spots are removed.  So, when we get to the next round, need
    // to look at the number of elves from the previous Round
    // If the number of elves in the previous round was even, then elf 1 takes presents elf 3, elf 5 takes presents from elf 7, and so on.
    //   this could be written as [1,5,9,13,17,21]
    // If the number of elves in the previous round was odd, then the last elf takes presents from elf 1. elf 3 takes presents elf 5, elf 7 takes presents from elf 9, and so on
    //   this could be written as [3,7,11,15,19,23]
    // So we need to start off with the elf in position 1. If the previous was even, the elf stays
    //  if the previous was odd, the elf leaves and the elf in position 2 takes over.  So this could be taken down
    public Integer solutionPart1(Integer numberOfElves) {
        Integer currentNumber = numberOfElves;
        Integer elfInPosition1 = 1;
        Integer multiplier = 1;
        Boolean previousWasOdd = false;
        while (currentNumber > 1) {
            if (previousWasOdd) {
                elfInPosition1 += multiplier;
            }
            previousWasOdd = currentNumber %2 == 1;
            currentNumber /= 2;
            multiplier *= 2;
        }
        return elfInPosition1;
    }

    public Integer solutionPart2(Integer numberOfElves, Integer nodeSize) {
        ElvesList circularElephant = new ElvesList(1,numberOfElves, nodeSize);
        int currentPosition = 0;
        while (circularElephant.size > 1) {
            Integer removalPosition = (currentPosition + circularElephant.size/2) % circularElephant.size;
            circularElephant.remove(removalPosition);
            currentPosition += (removalPosition < currentPosition) ? 0 : 1;

            if (currentPosition >= circularElephant.size) {
                currentPosition = 0;
            }
        }
        return circularElephant.get(0);
    }

    public static class ElvesNode {
        List<Integer> elves;
        ElvesNode next;
        public ElvesNode (Integer startNumber, int size) {
            elves = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                elves.add(startNumber + i);
            }
            next = null;
        }
        public void setNext(ElvesNode next) {
            this.next = next;
        }
        public Integer getSize() {
            return elves.size();
        }

        public Integer getElement(int index) {
            return elves.get(index);
        }
        public void removeElfFromSeat(int index) {
            Integer value = elves.remove(index);
            //System.out.println("Elf #" + value);
        }
    }

    public static class ElvesList {
        ElvesNode head;
        int size;
        public ElvesList(int start, int size, int nodeSize) {
            head = new ElvesNode(start, nodeSize);
            this.size = size;
            ElvesNode prev = head;
            size -= nodeSize;
            Integer startNumber = start + nodeSize;
            while(size > 0) {
                ElvesNode next = new ElvesNode(startNumber, nodeSize);
                prev.setNext(next);
                prev = next;
                size -= nodeSize;
                startNumber += nodeSize;
                if (size < nodeSize) {
                    nodeSize = size;
                }
            }
        }

        public Integer get(Integer index){
            ElvesNode currentNode = head;
            while (currentNode != null) {
                if (index < currentNode.getSize()) {
                    return currentNode.getElement(index);
                }
                index -= currentNode.getSize();
                currentNode = currentNode.next;
            }
            return -1;
        }

        public void remove(Integer index) {
            ElvesNode prevNode = head;
            ElvesNode currentNode = head;
            while (currentNode != null) {
                if (index < currentNode.getSize()) {
                    currentNode.removeElfFromSeat(index);
                    size--;
                    if (currentNode.getSize() == 0) {
                        if (head == currentNode) {
                            head = currentNode.next;
                        } else {
                            prevNode.setNext(currentNode.next);
                        }
                    }
                    return;
                }
                index -= currentNode.getSize();
                prevNode = currentNode;
                currentNode = currentNode.next;
            }
        }
    }
}

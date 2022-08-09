package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MemoryReallocation extends AoCDay {
    public void solve() {
        //Integer block[] = {0,2,7,0};
        Integer block[] = {14,0,15,12,11,11,3,5,1,6,8,4,9,1,8,4};

        Pair<Integer, Integer> solution = solutionPart1(block);
        System.out.println(String.format("Part 1 Answer: %d",solution.getLeft()));
        System.out.println(String.format("Part 1 Answer: %d",solution.getRight()));
    }

    public Pair<Integer,Integer> solutionPart1(Integer[] block) {
        List<List<Integer>> seenBefore = new ArrayList<>();
        Integer count = 0;
        Boolean notSeenBefore = true;
        Integer seenBeforeNumber = -1;
        while (notSeenBefore) {
            Integer maxBlock = findMaxBlock(block);
            Integer[] currentBlock = createNewBlock(block, maxBlock);

            Pair<Boolean, Integer> notSeenBeforePair = listContainsItem(seenBefore, Arrays.stream(block).collect(Collectors.toList()));
            notSeenBefore = !notSeenBeforePair.getLeft();
            seenBeforeNumber = notSeenBeforePair.getRight();
            seenBefore.add(Arrays.stream(block).collect(Collectors.toList()));
            block = currentBlock;
            if (notSeenBefore) {
                count++;
            }
        }
        return Pair.of(count, count- seenBeforeNumber);
    }

    public Pair<Boolean,Integer> listContainsItem(List<List<Integer>> knownBlocks, List<Integer> block) {
        if (knownBlocks.size() == 0) {
            return Pair.of(false,-1);
        }
        Integer number = 0;
        for(List<Integer> knownBlock : knownBlocks) {
            boolean thisIsBlock = true;
            for(int j = 0; j < knownBlock.size(); j++) {
                if (block.get(j) != knownBlock.get(j)) {
                    thisIsBlock = false;
                }
            }
            if (thisIsBlock) {
                return Pair.of(true, number);
            }
            number++;
        }
        return Pair.of(false,-1);
    }

    public Integer[] createNewBlock(Integer[] block, Integer startBlock) {
        Integer blocksToMove = block[startBlock];
        Integer[] newBlock = block.clone();
        newBlock[startBlock] = 0;
        while (blocksToMove > 0) {
            startBlock++;
            if (startBlock >= block.length) {
                startBlock -= block.length;
            }
            newBlock[startBlock]++;
            blocksToMove--;
        }
        return newBlock;
    }

    public Integer findMaxBlock(Integer[] block) {
        Integer blockNumber = 0;
        Integer maxNum = -1;
        for (int i = 0; i < block.length; i++) {
            if (block[i] > maxNum) {
                blockNumber = i;
                maxNum = block[i];
            }
        }
        return blockNumber;
    }
}

package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MemoryManeuver extends AoCDay {

    public static class Node {
        List<Integer> metadata;
        List<Node> childNodes;
        Integer expectedNumberOfChildNodes;
        Integer expectedNumberOfMetadata;

        public Node() {
            metadata = new ArrayList<>();
            childNodes = new ArrayList<>();
            expectedNumberOfChildNodes = 0;
            expectedNumberOfMetadata = 0;
        }

        public Integer getValue() {
            if (expectedNumberOfChildNodes == 0) {
                return metadata.stream().mapToInt(i -> i).sum();
            }
            Integer value = 0;
            for (Integer m : metadata) {
                if (m > childNodes.size()) {
                    continue;
                }
                value += childNodes.get(m-1).getValue();
            }
            return value;
        }

        public Integer getMetadataSum() {
            Integer sum = metadata.stream().mapToInt(i -> i).sum();
            for (Node child : childNodes) {
                sum += child.getMetadataSum();
            }
            return sum;
        }

        public void addToMetadata(Integer value) {
            metadata.add(value);
        }

        public void addToChildNodes(Node child) {
            childNodes.add(child);
        }
    }

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2018/day08/input.txt");
        List<Integer> numbers = Arrays.stream(lines.get(0).split(" ")).map(Integer::parseInt).collect(Collectors.toList());
        Node headNode = createNodes(numbers);
        Integer part1 = headNode.getMetadataSum();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Integer part2 = headNode.getValue();
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Node createNodes(List<Integer> numbers) {
        Node headNode = new Node();
        Pair<Node, Integer> topNode = subNode(numbers, 0, headNode);
        return topNode.getLeft();

    }

    public Pair<Node, Integer> subNode(List<Integer> numbers, Integer pointer, Node currentNode) {
        Integer numberOfChildNodes = numbers.get(pointer);
        Integer currentPointer = pointer + 1;
        Integer numberOfMetadata = numbers.get(currentPointer);
        currentPointer++;
        currentNode.expectedNumberOfChildNodes = numberOfChildNodes;
        currentNode.expectedNumberOfMetadata = numberOfMetadata;
        while (currentNode.childNodes.size() < currentNode.expectedNumberOfChildNodes) {
            Pair<Node, Integer> childNode = subNode(numbers, currentPointer, new Node());
            currentPointer = childNode.getRight();
            currentNode.addToChildNodes(childNode.getLeft());
        }
        while (currentNode.metadata.size() < currentNode.expectedNumberOfMetadata) {
            currentNode.addToMetadata(numbers.get(currentPointer));
            currentPointer++;
        }
        return Pair.of(currentNode, currentPointer);
    }
}

package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
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
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,8,false,0);
        List<Integer> numbers = Arrays.stream(lines.get(0).split(" ")).map(Integer::parseInt).collect(Collectors.toList());
        timeMarkers[1] = Instant.now().toEpochMilli();
        Node headNode = createNodes(numbers);
        part1Answer = headNode.getMetadataSum();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = headNode.getValue();
        timeMarkers[3] = Instant.now().toEpochMilli();
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

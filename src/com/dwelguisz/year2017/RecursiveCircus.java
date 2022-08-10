package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2017.helper.CircusNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecursiveCircus extends AoCDay {
    public void solve(){
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2017/day07/input.txt");
        Map<String,CircusNode> orderedNodes = createCircusNode(lines);
        String part1 = solutionPart1(orderedNodes);
        Integer part2 = solutionPart2(orderedNodes, part1);
        System.out.println(String.format("Part 1 Answer: %s",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public String solutionPart1(Map<String, CircusNode> orderedNodes) {
        return orderedNodes.entrySet().stream()
                .filter(entry -> !entry.getValue().hasParentNode())
                .map(entry -> entry.getKey())
                .collect(Collectors.toList()).get(0);
    }

    public Integer solutionPart2(Map<String, CircusNode> orderedNodes, String topNode) {
        CircusNode currentNode = orderedNodes.get(topNode);
        CircusNode parentNode = orderedNodes.get(topNode);
        while (!currentNode.isBalanced()) {
            parentNode = currentNode;
            currentNode = currentNode.unBalancedNode();
        }
        List<Integer> parentChildrenNumbers = parentNode.getChildrenSums();
        Integer currentNodeValue = currentNode.getStackWeight();
        Integer targetWeight = 0;
        for (Integer weight : parentChildrenNumbers) {
            if (!weight.equals(currentNodeValue)) {
                targetWeight = weight;
                break;
            }
        }
        Integer diff = currentNodeValue - targetWeight;
        return currentNode.getValue() - diff;
    }

    public Map<String,CircusNode> createCircusNode(List<String> lines) {
        Map<String, CircusNode> allNodes = new HashMap<>();
        for(String line : lines) {
            Boolean hasChildren = line.contains("->");
            String firstPart[] = line.split("\\)");
            Integer value = Integer.parseInt(firstPart[0].split("\\(")[1]);
            String name = firstPart[0].split(" ")[0];
            CircusNode node = allNodes.getOrDefault(name, new CircusNode(name));
            node.setValue(value);
            if (hasChildren) {
                String tmp[] = line.split(" -> ");
                String children[] = tmp[1].split(", ");
                for (String child : children) {
                    CircusNode childNode = allNodes.getOrDefault(child, new CircusNode(child));
                    childNode.setParentNode(node);
                    node.addChildrenNode(childNode);
                    allNodes.put(child, childNode);
                }
            }
            allNodes.put(name, node);
        }
        return allNodes;
    }
}

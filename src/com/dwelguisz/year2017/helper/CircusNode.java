package com.dwelguisz.year2017.helper;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CircusNode {
    CircusNode parentNode;
    List<CircusNode> childrenNode;
    String name;
    Integer value;

    public CircusNode(String name) {
        this.parentNode = null;
        this.childrenNode = new ArrayList<>();
        this.name = name;
        this.value = 0;
    }

    public Boolean hasParentNode() {
        return this.parentNode != null;
    }

    public Integer getStackWeight() {
        Integer sum = this.value;
        for (CircusNode child : childrenNode) {
            sum += child.getStackWeight();
        }
        return sum;
    }

    public Boolean isBalanced() {
        List<Integer> childrenSums = getChildrenSums();
        System.out.println("Node " + this.name + ".childrenSums: " + childrenSums.stream().map(String::valueOf).collect(Collectors.joining(",")));
        for(int i = 0; i < childrenSums.size() - 1; i++) {
            if (!childrenSums.get(i).equals(childrenSums.get(i+1))) {
                return false;
            }
        }
        return true;
    }

    public void printInfo() {
        List<Integer> childrenSums = getChildrenSums();
        System.out.println("Node " + this.name);
        System.out.println("Value: " + this.value);
        System.out.println("childrenSums: " + childrenSums.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    public CircusNode unBalancedNode() {
        List<Integer> childrenSums = getChildrenSums();
        int i;
        for(i = 0; i < childrenSums.size() - 1; i++) {
            if (!childrenSums.get(i).equals(childrenSums.get(i+1))) {
                break;
            }
        }
        int nextTestLoc = i+2;
        if (nextTestLoc >= childrenSums.size()) {
            nextTestLoc -= childrenSums.size();
        }
        if (childrenSums.get(i).equals(childrenSums.get(nextTestLoc))) {
            int index = i + 1;
            if (index >= childrenSums.size()) {
                index -= childrenSums.size();
            }
            return childrenNode.get(index);
        }
        return childrenNode.get(i);

    }

    public List<Integer> getChildrenSums() {
        return childrenNode.stream()
                .map(node -> node.getStackWeight())
                .collect(Collectors.toList());
    }

    public void setParentNode(CircusNode parentNode) {
        this.parentNode = parentNode;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void addChildrenNode(CircusNode node) {
        childrenNode.add(node);
    }

}

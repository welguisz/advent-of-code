# Day 17: No Such Thing as Too Much

[Back to Top README file](../../../README.md)
## Overview
Difficult Level: Easy

Input: List of container sizes

## The key idea
The key to this problem is to see that we are looking for as many combinations
of the containers that will hold the requested amount of egg nog.  We will be
looking at possible combinations of containers and the sum of the list. To do this,
we have to create a way to get all of the combinations and store them.

```java
    public List<List<Integer>> createPossibleCombinations(List<String> lines, Integer target) {
        List<Integer> containerSizes = new ArrayList<>();
        for (String line : lines){
            containerSizes.add(Integer.parseInt(line));
        }
        List<List<Integer>> possibleItems = new ArrayList<>();
        for(int i = 0; i < containerSizes.size(); i++) {
            possibleItems.addAll(combinations(containerSizes,i, target));
        }
        return possibleItems;
    }

    public List<List<Integer>> combinations(List<Integer> inputSet, int k, int target) {
        List<List<Integer>> results = new ArrayList<>();
        combinationsInternal(inputSet, k, results, new ArrayList<>(), 0, target);
        return results;
    }

    public void combinationsInternal(List<Integer> inputSet, int k, List<List<Integer>> results, ArrayList<Integer> accumulator, int index, int target) {
        int needToAccumulate = k - accumulator.size();
        int canAccumulate = inputSet.size() - index;
        if (accumulator.size() == k) {
            Integer sum = accumulator.stream().mapToInt(i -> i).sum();
            if (sum.equals(target)) {
                results.add(new ArrayList<>(accumulator));
                return;
            }
            if (sum > target) {
              return;
            }
        } else if (needToAccumulate <= canAccumulate) {
            combinationsInternal(inputSet, k, results, accumulator, index + 1, target);
            accumulator.add(inputSet.get(index));
            combinationsInternal(inputSet,k,results,accumulator,index+1, target);
            accumulator.remove(accumulator.size()-1);
        }
    }
```

Now for part1, we can just do the following:

```java
    public Integer solutionPart1(List<List<Integer>> possibleCombinations) {
        return possibleCombinations.size();
    }
```

And part 2 requires to find the minimum number of containers used and find any
other combinations that use the same number of containers.

```java
    public Long solutionPart2(List<List<Integer>> possibleCombinations) {
        Integer minimum = possibleCombinations.stream().mapToInt(nums -> nums.size()).min().getAsInt();
        return possibleCombinations.stream().filter(pi -> pi.size() == minimum).count();
    }
```

|[Previous (Day 16)](../day16/README.md)|[Next (Day 18)](../day18/README.md)|
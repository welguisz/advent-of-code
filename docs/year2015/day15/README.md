# Day 15: Science for Hungry People

[Back to Top README file](../../../README.md)
## Overview
Difficult Level: Easy/Medium

Input: List of Ingredients with different characteristics

## Parsing
Parsing is straightforward with the following setup for each line:

```java
Name: capacity C, durability D, flavor F, texture T, calories K
```
I will be storing each ingredient in a class that contains the following 
information: `name`, `capacity`, `durability`, `flavor`, `texture`, and
`calories`.

After parsing, we can figure out the total Score for each possible recipe and
how many calories are in each recipe.

Since we have to use exactly 100 teaspoons and have to use at least one teaspoon
of each ingredient, we can create the following loops:

```java
        List<Pair<Integer,Integer>> recipes = new ArrayList<>();
        for (int i = 1; i < 97; i++) {
            for (int j = 1; j < (97-i); j++) {
                for (int k = 1; k < (97-(i+j)); k++) {
                    int l = 100 - (i + j + k);
                    ingredientList.get(0).setCurrentTsp(i);
                    ingredientList.get(1).setCurrentTsp(j);
                    ingredientList.get(2).setCurrentTsp(k);
                    ingredientList.get(3).setCurrentTsp(l);
                    int totalCapacityScore = ingredientList.stream().mapToInt(Ingredient::getCapacityScore).sum();
                    int totalDurabilityScore = ingredientList.stream().mapToInt(Ingredient::getDurabilityScore).sum();
                    int totalFlavorScore = ingredientList.stream().mapToInt(Ingredient::getFlavorScore).sum();
                    int totalTextureScore = ingredientList.stream().mapToInt(Ingredient::getTextureScore).sum();
                    int totalCaloires = ingredientList.stream().mapToInt(Ingredient::getCaloriesScore).sum();
                    List<Integer> noNegatives = List.of(totalCapacityScore, totalDurabilityScore, totalFlavorScore, totalTextureScore);
                    if (noNegatives.stream().anyMatch(n -> n <= 0)) {
                        continue;
                    }
                    Integer totalScore = noNegatives.stream().reduce(1, (a, b) -> a * b);
                    recipes.add(Pair.of(totalScore, totalCaloires));
                }
            }
        }
        return recipes;
```

With the recipes containing the `score` and `calories` for each viable combination,
we can now just do some stream operations and get the answers:

## Part 1

```java
    public Integer solutionPart1(List<Pair<Integer,Integer>> recipes) {
        return recipes.stream().mapToInt(r -> r.getLeft()).max().getAsInt();
    }
```

## Part 2

```java
    public Integer solutionPart2(List<Pair<Integer,Integer>> recipes) {
        return recipes.stream().filter(r -> r.getRight().equals(500)).mapToInt(r -> r.getLeft()).max().getAsInt();
    }
```

|[Previous (Day 14)](../day14/README.md)|[Next (Day 16)](../day16/README.md)|
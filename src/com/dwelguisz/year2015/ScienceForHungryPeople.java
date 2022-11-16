package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.List;

public class ScienceForHungryPeople extends AoCDay {
    public static class Ingredient {
        public String name;
        public Integer capacity;
        public Integer durability;
        public Integer flavor;
        public Integer texture;
        public Integer calories;

        public Integer currentTsp;

        public Ingredient(String name, Integer capacity, Integer durability, Integer flavor, Integer texture, Integer calories) {
            this.name = name;
            this.capacity = capacity;
            this.durability = durability;
            this.flavor = flavor;
            this.texture = texture;
            this.calories = calories;
            this.currentTsp = 0;
        }

        public void setCurrentTsp(Integer tsp) {
            currentTsp = tsp;
        }

        public Integer getCurrentTsp() {
            return currentTsp;
        }

        public Integer getCapacityScore() {
            return capacity * currentTsp;
        }
        public Integer getDurabilityScore() {
            return durability * currentTsp;
        }
        public Integer getFlavorScore() {
            return flavor * currentTsp;
        }
        public Integer getTextureScore() {
            return texture * currentTsp;
        }
        public Integer getCaloriesScore() {
            return calories * currentTsp;
        }
    }

    List<Ingredient> ingredientList;

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2015/day15/input.txt");
        createIngredients(lines);
        Long part1 = solutionPart1();
        System.out.println(String.format("Part 1 Answer: %d", part1));
        Long part2 = solutionPart2();
        System.out.println(String.format("Part 2 Answer: %d", part2));
    }

    public Long solutionPart1() {
        int numberOfIngredients = ingredientList.size();
        Long maxScore = 0L;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                for (int k = 0; k < 100; k++) {
                    for (int l = 0; l < 100; l++) {
                        if (i + j + k + l != 100) {
                            continue;
                        }
                        ingredientList.get(0).setCurrentTsp(i);
                        ingredientList.get(1).setCurrentTsp(j);
                        ingredientList.get(2).setCurrentTsp(k);
                        ingredientList.get(3).setCurrentTsp(l);
                        int totalCapacityScore = ingredientList.stream().map(Ingredient::getCapacityScore).reduce(0, (a,b) -> a+b);
                        int totalDurabilityScore = ingredientList.stream().map(Ingredient::getDurabilityScore).reduce(0, (a,b) -> a+b);
                        int totalFlavorScore = ingredientList.stream().map(Ingredient::getFlavorScore).reduce(0, (a,b) -> a+b);
                        int totalTextureScore = ingredientList.stream().map(Ingredient::getTextureScore).reduce(0, (a,b) -> a+b);
                        if (totalCapacityScore <=0 || totalDurabilityScore <= 0 || totalFlavorScore <= 0 || totalTextureScore <= 0) {
                            continue;
                        }
                        Long totalScore = 1L * totalCapacityScore * totalDurabilityScore * totalFlavorScore * totalTextureScore;
                        if (totalScore > maxScore) {
                            maxScore = totalScore;
                        }
                    }
                }
            }
        }
        return maxScore;
    }

    public Long solutionPart2() {
        int numberOfIngredients = ingredientList.size();
        Long maxScore = 0L;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                for (int k = 0; k < 100; k++) {
                    for (int l = 0; l < 100; l++) {
                        if (i + j + k + l != 100) {
                            continue;
                        }
                        ingredientList.get(0).setCurrentTsp(i);
                        ingredientList.get(1).setCurrentTsp(j);
                        ingredientList.get(2).setCurrentTsp(k);
                        ingredientList.get(3).setCurrentTsp(l);
                        int totalCapacityScore = ingredientList.stream().map(Ingredient::getCapacityScore).reduce(0, (a,b) -> a+b);
                        int totalDurabilityScore = ingredientList.stream().map(Ingredient::getDurabilityScore).reduce(0, (a,b) -> a+b);
                        int totalFlavorScore = ingredientList.stream().map(Ingredient::getFlavorScore).reduce(0, (a,b) -> a+b);
                        int totalTextureScore = ingredientList.stream().map(Ingredient::getTextureScore).reduce(0, (a,b) -> a+b);
                        int totalCaloires = ingredientList.stream().map(Ingredient::getCaloriesScore).reduce(0, (a,b) -> a+b);
                        if (totalCapacityScore <=0 || totalDurabilityScore <= 0 || totalFlavorScore <= 0 || totalTextureScore <= 0) {
                            continue;
                        }
                        if (totalCaloires != 500) {
                            continue;
                        }
                        Long totalScore = 1L * totalCapacityScore * totalDurabilityScore * totalFlavorScore * totalTextureScore;
                        if (totalScore > maxScore) {
                            maxScore = totalScore;
                        }
                    }
                }
            }
        }
        return maxScore;
    }


    public void createIngredients(List<String> lines) {
        ingredientList = new ArrayList<>();
        for (String line : lines) {
            String splits[] = line.split(" ");
            String name = splits[0].substring(0,splits[0].length()-1);
            Integer capacity = Integer.parseInt(splits[2].substring(0, splits[2].length()-1));
            Integer durability = Integer.parseInt(splits[4].substring(0, splits[4].length()-1));
            Integer flavor = Integer.parseInt(splits[6].substring(0, splits[6].length()-1));
            Integer texture = Integer.parseInt(splits[8].substring(0, splits[8].length()-1));
            Integer calories = Integer.parseInt(splits[10]);
            ingredientList.add(new Ingredient(name, capacity, durability, flavor, texture, calories));
        }
    }
}

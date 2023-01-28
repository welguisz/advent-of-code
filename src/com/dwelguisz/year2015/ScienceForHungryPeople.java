package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
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

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2015,15,false,0);
        List<Pair<Integer,Integer>> recipes = createIngredients(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(recipes);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(recipes);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(List<Pair<Integer,Integer>> recipes) {
        return recipes.stream().mapToInt(r -> r.getLeft()).max().getAsInt();
    }

    public Integer solutionPart2(List<Pair<Integer,Integer>> recipes) {
        return recipes.stream().filter(r -> r.getRight().equals(500)).mapToInt(r -> r.getLeft()).max().getAsInt();
    }

    public List<Pair<Integer, Integer>> createIngredients(List<String> lines) {
        List<Ingredient> ingredientList = new ArrayList<>();
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
        List<Pair<Integer,Integer>> recipes = new ArrayList<>();
        for (int i = 1; i < 97; i++) {
            for (int j = 1; j < (98-i); j++) {
                for (int k = 1; k < (99-(i+j)); k++) {
                    int l = 100 - (i + j + k);
                    ingredientList.get(0).setCurrentTsp(i);
                    ingredientList.get(1).setCurrentTsp(j);
                    ingredientList.get(2).setCurrentTsp(k);
                    ingredientList.get(3).setCurrentTsp(l);
                    List<Integer> noNegatives = new ArrayList<>();
                    noNegatives.add(ingredientList.stream().mapToInt(Ingredient::getCapacityScore).sum());
                    noNegatives.add(ingredientList.stream().mapToInt(Ingredient::getDurabilityScore).sum());
                    noNegatives.add(ingredientList.stream().mapToInt(Ingredient::getFlavorScore).sum());
                    noNegatives.add(ingredientList.stream().mapToInt(Ingredient::getTextureScore).sum());
                    int totalCaloires = ingredientList.stream().mapToInt(Ingredient::getCaloriesScore).sum();
                    if (noNegatives.stream().anyMatch(n -> n <= 0)) {
                        continue;
                    }
                    Integer totalScore = noNegatives.stream().reduce(1, (a, b) -> a * b);
                    recipes.add(Pair.of(totalScore, totalCaloires));
                }
            }
        }
        return recipes;
    }
}

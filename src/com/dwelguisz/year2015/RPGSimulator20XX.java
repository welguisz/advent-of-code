package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.util.ArrayList;
import java.util.List;

public class RPGSimulator20XX extends AoCDay {

    public static class Item {
        public String name;
        public Integer cost;
        public Integer damage;
        public Integer armor;

        public Item(String name, Integer cost, Integer damage, Integer armor) {
            this.name = name;
            this.cost = cost;
            this.damage = damage;
            this.armor = armor;
        }
    }

    public static class Player {
        public String name;
        public Integer hitPoints;
        public Integer baseDamage;
        public Integer baseArmor;
        public Item weapon;
        public Item defense;
        public List<Item> myRings;

        public Player(String name, Integer hitPoints, Integer baseDamage, Integer baseArmor) {
            this.name = name;
            this.hitPoints = hitPoints;
            this.baseDamage = baseDamage;
            this.baseArmor = baseArmor;
            this.defense = new Item("none",0,0,0);
            this.weapon = new Item("none",0,0,0);
            myRings = new ArrayList<>();

        }
        public void setWeapon(Item weapon) {
            this.weapon = weapon;
        }
        public void setDefense(Item defense) {
            this.defense = defense;
        }
        public void setRings(List<Item> rings) {
            this.myRings = rings;
        }
        public Integer getDamage() {
            return baseDamage + weapon.damage + myRings.stream().map(r -> r.damage).reduce(0, (a,b)->a+b);
        }
        public Integer getArmor() {
            return  baseArmor + defense.armor + myRings.stream().map(r -> r.armor).reduce(0, (a,b)->a+b);
        }
        public Integer getCost() {
            return weapon.cost + defense.cost + myRings.stream().map(r -> r.cost).reduce(0,(a,b)->a+b);
        }
    }

    List<Item> weapons;
    List<Item> armors;
    List<Item> rings;

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2015/day21/input.txt");
        Player boss = setupItems(lines);
        Integer part1 = solutionPart1(boss);
        Integer part2 = solutionPart2(boss);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Integer solutionPart1(Player bossImport) {
        Integer minCost = Integer.MAX_VALUE;
        List<List<Item>> ringConfiguration = new ArrayList<>();
        ringConfiguration.addAll(combinations(rings,2));
        for (Item weapon : weapons) {
            for (Item armor : armors) {
                for (List<Item> ring : ringConfiguration) {
                    Player boss = new Player(bossImport.name, bossImport.hitPoints, bossImport.baseDamage, bossImport.baseArmor);
                    Player player = new Player("me",100,0,0);
                    player.setWeapon(weapon);
                    player.setDefense(armor);
                    player.setRings(ring);
                    if (doesPlayer1Win(player, boss)) {
                        Integer cost = player.getCost();
                        minCost = Integer.min(cost,minCost);
                    }
                }
            }
        }
        return minCost;
    }

    public Integer solutionPart2(Player bossImport) {
        Integer maxCost = Integer.MIN_VALUE;
        List<List<Item>> ringConfiguration = new ArrayList<>();
        ringConfiguration.addAll(combinations(rings,2));
        for (Item weapon : weapons) {
            for (Item armor : armors) {
                for (List<Item> ring : ringConfiguration) {
                    Player boss = new Player(bossImport.name, bossImport.hitPoints, bossImport.baseDamage, bossImport.baseArmor);
                    Player player = new Player("me",100,0,0);
                    player.setWeapon(weapon);
                    player.setDefense(armor);
                    player.setRings(ring);
                    if (!doesPlayer1Win(player, boss)) {
                        Integer cost = player.getCost();
                        maxCost = Integer.max(cost,maxCost);
                    }
                }
            }
        }
        return maxCost;
    }


    public boolean doesPlayer1Win(Player player1, Player boss) {
        Integer player1Attack = Math.max(1,player1.getDamage() - boss.getArmor());
        Integer bossAttack = Math.max(1,boss.getDamage() - player1.getArmor());
        while (true) {
            boss.hitPoints -= player1Attack;
            if (boss.hitPoints <= 0) {
                return true;
            }
            player1.hitPoints -= bossAttack;
            if (player1.hitPoints <= 0) {
                return false;
            }
        }
    }

    public Player setupItems(List<String> lines) {
        weapons = new ArrayList<>();
        armors = new ArrayList<>();
        rings = new ArrayList<>();
        Integer bhp = 0;
        Integer bdamage = 0;
        Integer barmor = 0;
        Boolean bossItems = false;
        List<Item> temp = new ArrayList<>();
        String option = "";
        for (String line : lines) {
            if (line.length() == 0) {
                if (option.equals("Weapons")) {
                    weapons = temp;
                } else if (option.equals("Armor")) {
                    armors = temp;
                } else if (option.equals("Rings")) {
                    rings = temp;
                }
                temp = new ArrayList<>();
            }
            else if (line.contains("Cost")) {
                String split[] = line.split(":");
                option = split[0];
            }
            else if (bossItems) {
                String split[] = line.split(": ");
                Integer value = Integer.parseInt(split[1]);
                if (split[0].equals("Hit Points")) {
                    bhp = value;
                } else if (split[0].equals("Damage")) {
                    bdamage = value;
                } else if (split[0].equals("Armor")) {
                    barmor = value;
                }
            } else if (line.contains("Boss")) {
                bossItems = true;
            } else {
                String split[] = line.split("\\s{1,}");
                Integer diff = 0;
                if (option.equals("Rings")) {
                    diff = 1;
                }
                String itemName = split[0];
                Integer cost = Integer.parseInt(split[1+diff]);
                Integer damage = Integer.parseInt(split[2+diff]);
                Integer armor = Integer.parseInt(split[3+diff]);
                temp.add(new Item(itemName,cost,damage,armor));
            }

        }
        armors.add(new Item("no armor",0,0,0));
        rings.add(new Item("no rings",0,0,0));
        rings.add(new Item("no rings2",0,0,0));
        return new Player("boss", bhp, bdamage, barmor);
    }

    public List<List<Item>> combinations(List<Item> inputSet, int k) {
        List<List<Item>> results = new ArrayList<>();
        combinationsInternal(inputSet, k, results, new ArrayList<>(), 0);
        return results;
    }

    public void combinationsInternal(List<Item> inputSet, int k, List<List<Item>> results, ArrayList<Item> accumulator, int index) {
        int needToAccumulate = k - accumulator.size();
        int canAccumulate = inputSet.size() - index;
        if (accumulator.size() == k) {
            results.add(new ArrayList<>(accumulator));
        } else if (needToAccumulate <= canAccumulate) {
            combinationsInternal(inputSet, k, results, accumulator, index + 1);
            accumulator.add(inputSet.get(index));
            combinationsInternal(inputSet,k,results,accumulator,index+1);
            accumulator.remove(accumulator.size()-1);
        }
    }

}

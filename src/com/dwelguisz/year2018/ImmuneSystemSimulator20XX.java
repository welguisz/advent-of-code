package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ImmuneSystemSimulator20XX extends AoCDay {

    public static class ImmuneGroup {
        public Integer numberOfUnits;
        public Integer hitPoints;
        public List<String> weakDefense;
        public List<String> strongDefense;
        public Pair<String, Integer> attack;
        public Integer initiative;
        public String group;
        public Integer id;
        public ImmuneGroup(Integer numberOfUnits, Integer hitPoints, List<String> weakDefense, List<String> strongDefense,
                           Pair<String, Integer> attack, Integer initiative, String group, Integer id) {
            this.numberOfUnits = numberOfUnits;
            this.hitPoints = hitPoints;
            this.weakDefense = weakDefense;
            this.strongDefense = strongDefense;
            this.attack = attack;
            this.initiative = initiative;
            this.group = group;
            this.id = id;
        }

        public Integer effectivePower() {
            return numberOfUnits * attack.getRight();
        }

        public Pair<Integer, Integer> attackOrderSort() {
            return Pair.of(effectivePower(), initiative);
        }

        public Integer damageTaken(ImmuneGroup attacker) {
            if (strongDefense.contains(attacker.attack.getLeft())) {
                return 0;
            }
            if (weakDefense.contains(attacker.attack.getLeft())) {
                return attacker.effectivePower()*2;
            }
            return attacker.effectivePower();
        }

        public void hurtBy(ImmuneGroup attacker) {
            Integer damage = damageTaken(attacker);
            Integer casualties = damage / hitPoints;
            casualties = (casualties > numberOfUnits) ? numberOfUnits : casualties;
            numberOfUnits -= casualties;
            if (numberOfUnits < 0) {
                numberOfUnits = 0;
            }
        }

        @Override
        public String toString() {
            return String.format("%s %d",group, id);
        }

    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,24,false,0);
        List<ImmuneGroup> combatants = parsedLines(lines,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(combatants);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<ImmuneGroup> parsedLines(List<String> lines, Integer boost) {
        List<ImmuneGroup> combatants = new ArrayList<>();
        String group = "not set";
        Integer id = 0;
        for (String l : lines) {
            if (l.contains(":")) {
                group = l.substring(0,l.length()-1);
                id = 1;
                continue;
            } else if (l.length() == 0) {
                continue;
            }
            String tmp[] = l.split(" ");
            Integer numberOfUnits = Integer.parseInt(tmp[0]);
            Integer hitPoints = Integer.parseInt(tmp[4]);
            Integer initiative = Integer.parseInt(tmp[tmp.length-1]);
            Pair<String, Integer> attack = Pair.of(tmp[tmp.length-5],Integer.parseInt(tmp[tmp.length-6]));
            if (group.equals("Immune System")) {
                attack = Pair.of(tmp[tmp.length-5],Integer.parseInt(tmp[tmp.length-6])+boost);
            }
            Integer openParenthesis = l.indexOf('(');
            Integer closeParenthesis = l.indexOf(')');
            List<String> weakDefense = new ArrayList<>();
            List<String> strongDefense = new ArrayList<>();
            if (openParenthesis > 0) {
                String defenseRatings = l.substring(openParenthesis + 1, closeParenthesis);
                String defenses[] = defenseRatings.split("; ");
                for (String defense : defenses) {
                    Boolean weak = defense.contains("weak");
                    String tmpdef[] = defense.split(" ");
                    List<String> defs = new ArrayList<>();
                    for (int i = 2; i < tmpdef.length; i++) {
                        String df = tmpdef[i].contains(",") ? tmpdef[i].substring(0, tmpdef[i].length() - 1) : tmpdef[i];
                        defs.add(df);
                    }
                    if (weak) {
                        weakDefense = defs;
                    } else {
                        strongDefense = defs;
                    }
                }
            }
            combatants.add(new ImmuneGroup(numberOfUnits,hitPoints,weakDefense,strongDefense,attack,initiative,group,id));
            id++;

        }
        return combatants;
    }

    public Integer fight(List<ImmuneGroup> combatants) {
        List<ImmuneGroup> infection = combatants.stream()
                .filter(c -> c.group.equals("Infection"))
                .collect(Collectors.toList());
        List<ImmuneGroup> immune = combatants.stream().filter(c -> c.group.equals("Immune System")).collect(Collectors.toList());
        Integer previousUnits = infection.stream().mapToInt(ig -> ig.numberOfUnits).sum() + immune.stream().mapToInt(ig -> ig.numberOfUnits).sum();
        while (!infection.isEmpty() && !immune.isEmpty()) {
            List<ImmuneGroup> orderedCombatants = combatants.stream().filter(ig -> ig.numberOfUnits > 0).sorted((b,a) -> {
                Pair<Integer, Integer> aValues = a.attackOrderSort();
                Pair<Integer, Integer> bValues = b.attackOrderSort();
                if (aValues.getLeft().equals(bValues.getLeft())) {
                    return aValues.getRight().compareTo(bValues.getRight());
                }
                else {
                    return aValues.getLeft().compareTo(bValues.getLeft());
                }
            }).collect(Collectors.toList());
            HashMap<ImmuneGroup, Integer> targets = chooseTargets(orderedCombatants);
            if (targets.entrySet().stream().allMatch(t -> t.getValue().equals(-1))) {
                System.out.println("Stalemate");
                return -1;
            }
            List<Integer> order = targets.entrySet().stream().map(t -> t.getKey().initiative).sorted((a,b)-> Integer.compare(b,a)).collect(Collectors.toList());
            for (Integer i : order) {
                ImmuneGroup attack = targets.entrySet().stream()
                        .filter(t -> t.getKey().initiative.equals(i))
                        .map(e->e.getKey()).collect(Collectors.toList()).get(0);
                Integer defenderInt = targets.get(attack);
                if (defenderInt == -1) {
                    continue;
                }
                ImmuneGroup defender = targets.entrySet().stream()
                        .filter(t -> t.getKey().initiative.equals(defenderInt))
                        .map(e->e.getKey()).collect(Collectors.toList()).get(0);
                defender.hurtBy(attack);
            }
            infection = targets.entrySet().stream()
                    .filter(t -> t.getKey().group.equals("Infection"))
                    .filter(t -> t.getKey().numberOfUnits > 0)
                    .map(t -> t.getKey())
                    .collect(Collectors.toList());
            immune = targets.entrySet().stream()
                    .filter(t -> t.getKey().group.equals("Immune System"))
                    .filter(t -> t.getKey().numberOfUnits > 0)
                    .map(t -> t.getKey())
                    .collect(Collectors.toList());
            Integer currentUnits = infection.stream().mapToInt(g -> g.numberOfUnits).sum() + immune.stream().mapToInt(g -> g.numberOfUnits).sum();
            if (previousUnits.equals(currentUnits)) {
                return -1;
            }
            previousUnits = currentUnits;
        }
        return infection.stream().mapToInt(g -> g.numberOfUnits).sum() + immune.stream().mapToInt(g -> g.numberOfUnits).sum();
    }

    HashMap<ImmuneGroup, Integer> chooseTargets(List<ImmuneGroup> combatants) {
        HashMap<ImmuneGroup, Integer> targets = new HashMap<>();
        for (ImmuneGroup c : combatants) {
            targets.put(c, -1);
        }
        Set<Integer> underAttack = new HashSet<>();
        for (ImmuneGroup c : combatants) {
            Integer victim = -1;
            Integer vDamage = 0;
            for (ImmuneGroup d : combatants) {
                if (c.group.equals(d.group)) {
                    continue;
                } else if (underAttack.contains(d.initiative)) {
                    continue;
                }
                Integer damage = d.damageTaken(c);
                if (damage > vDamage) {
                    vDamage = damage;
                    victim = d.initiative;
                }
            }
            targets.put(c,victim);
            underAttack.add(victim);
        }
        return targets;
    }

    public Integer solutionPart1(List<ImmuneGroup> combatants) {
        return fight(combatants);
    }

    public Integer solutionPart2(List<String> lines) {
        Boolean needMoreBoost = true;
        Integer boost = 0;
        Integer unitsRemaining = -1;
        while(needMoreBoost) {
            boost++;
            List<ImmuneGroup> combatants = parsedLines(lines, boost);
            unitsRemaining = fight(combatants);
            needMoreBoost = combatants.stream().filter(c -> c.numberOfUnits > 0).anyMatch(c -> c.group.equals("Infection"));
        }
        return unitsRemaining;
    }
}

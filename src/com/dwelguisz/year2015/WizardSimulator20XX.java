package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WizardSimulator20XX extends AoCDay {

    public static Integer MAX_MANA = 1500;
    public static class Spell {
        String name;
        int cost;
        int damage;
        int healing;
        int armor;
        int recharge;
        int activeTurns;

        public Spell(String name, int cost, int damage, int healing, int armor, int recharge, int activeTurns) {
            this.name = name;
            this.cost = cost;
            this.damage = damage;
            this.healing = healing;
            this.armor = armor;
            this.recharge = recharge;
            this.activeTurns = activeTurns;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class Player {
        public String name;
        public int hitPoints;
        public int baseDamage;
        public int baseArmor;
        public int mana;
        public Map<Spell, Integer> spellsInEffect;
        public int spent;
        public List<Spell> spellsCast;

        public Player(String name, int hitPoints, int baseDamage, int baseArmor, int mana, int spent) {
            this.name = name;
            this.hitPoints = hitPoints;
            this.baseDamage = baseDamage;
            this.baseArmor = baseArmor;
            this.mana = mana;
            this.spent = spent;
            spellsInEffect = new HashMap<>();
            spellsCast = new ArrayList<>();
        }

        static Player copyOf(Player original) {
            Player copy = new Player(original.name, original.hitPoints, original.baseDamage, original.baseArmor, original.mana, original.spent);
            for (Spell c : original.spellsCast) {
                copy.spellsCast.add(c);
            }
            for (Map.Entry<Spell, Integer> sc : original.spellsInEffect.entrySet()) {
                copy.spellsInEffect.put(sc.getKey(), sc.getValue());
            }
            return copy;
        }
        public boolean spellInEffect(Spell spell) {
            Integer count = spellsInEffect.getOrDefault(spell,0);
            if (count.equals(0)) {
                return false;
            }
            return true;
        }

        public void startSpell(Spell spell) {
            if (!spell.name.equals("Nothing")) {
                spellsInEffect.put(spell, spell.activeTurns);
            }
        }
        public List<Integer> getAttack(Spell spell) {
            Integer attack = 0;
            Integer healing = 0;
            Integer protection = 0;
            Integer recharge = 0;
            if ("Magic Missile".equals(spell.name) || "Drain".equals(spell.name)) {
                attack += spell.damage;
            }
            if ("Drain".equals(spell.name)) {
                healing += 2;
            }
            for (Map.Entry<Spell, Integer> currentSpell : spellsInEffect.entrySet()) {
                if (currentSpell.getValue().equals(0)) {
                    continue;
                }
                attack += currentSpell.getKey().damage;
                healing += currentSpell.getKey().healing;
                recharge += currentSpell.getKey().recharge;
                protection += currentSpell.getKey().armor;
                Integer val = currentSpell.getValue();
                val--;
                currentSpell.setValue(val);
            }
            List values = new ArrayList<>();
            values.add(attack);
            values.add(healing);
            values.add(recharge);
            values.add(protection);
            startSpell(spell);
            return values;
        }
    }

    List<Spell> spells;

    public void solve(){
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2015,21,false,0);
        setupItems();
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart1(true);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Integer solutionPart1(Boolean hardMode) {
        Player player1 = new Player("me", 50,0,0,500,0);
        Player boss = new Player("boss",55,8,0,0,0);
        List<Integer> results = doesPlayer1Win(player1, boss, hardMode);
        return results.stream().min(Integer::compareTo).get();
    }

    public List<Integer> doesPlayer1Win(Player player, Player boss1, Boolean hardMode) {
        List<Spell> goodSpells = availableSpells(player);
        //If unable to cast spells on Player's turn, die.
        //MAX_MANA is to prevent the simulator getting in an infinite loop
        if ((goodSpells.size() == 0) || (player.spent > MAX_MANA)) {
            return new ArrayList<>();
        }
        //If hard mode, decrement player's hitPoints. If it goes to 0, player is dead.
        if (hardMode) {
            player.hitPoints--;
            if (player.hitPoints <= 0) {
                return new ArrayList<>();
            }
        }
        List<Integer> results = new ArrayList<>();
        for (Spell spellCast : goodSpells) {
            //Copy information for all possible scenarios.
            Player player1 = Player.copyOf(player);
            Player boss = Player.copyOf(boss1);
            player1.spellsCast.add(spellCast);
            player1.spent += spellCast.cost;
            player1.mana -= spellCast.cost;
            List<Integer> effects = player1.getAttack(spellCast);
            boss.hitPoints -= effects.get(0);
            player1.hitPoints += effects.get(1);
            player1.mana += effects.get(2);
            if (boss.hitPoints <= 0) {
                results.add(player1.spent);
                continue;
            }
            //Boss Turn -- Boss can't use magic, so automatically get spells[0]
            effects = player1.getAttack(spells.get(0));
            boss.hitPoints -= effects.get(0);
            player1.hitPoints += effects.get(1);
            player1.mana += effects.get(2);
            Integer bossAttack = Integer.max(1,boss.baseDamage - effects.get(3));
            if (boss.hitPoints <= 0) {
                results.add(player1.spent);
                continue;
            }
            player1.hitPoints -= bossAttack;
            if (player1.hitPoints <= 0) {
                continue;
            }
            results.addAll(doesPlayer1Win(player1,boss,hardMode));
        }
        return results;
    }

    public List<Spell> availableSpells(Player player) {
        return spells.stream().filter(s -> s.cost <= player.mana)
                .filter(s -> !s.name.equals("Nothing"))
                .filter(s -> !player.spellInEffect(s))
                .collect(Collectors.toList());
    }

    public void setupItems() {
        spells = new ArrayList<>();
        spells.add(new Spell("Nothing",0,0,0,0,0,0));
        spells.add(new Spell("Magic Missile",53,4,0,0,0,0));
        spells.add(new Spell("Drain",73,2,2,0,0,0));
        spells.add(new Spell("Shield",113,0,0,7,0,6));
        spells.add(new Spell("Poison",173,3,0,0,0,6));
        spells.add(new Spell("Recharge",229,0,0,0,101,5));
    }
}

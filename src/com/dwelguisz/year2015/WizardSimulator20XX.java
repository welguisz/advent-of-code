package com.dwelguisz.year2015;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import lombok.Value;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WizardSimulator20XX extends AoCDay {

    public static Integer MAX_MANA = 50000;
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
            copy.spellsCast.addAll(original.spellsCast);
            copy.spellsInEffect.putAll(original.spellsInEffect);
            return copy;
        }
        public boolean spellInEffect(Spell spell) {
            Integer count = spellsInEffect.getOrDefault(spell,0);
            return (count > 1);
        }

        public void startSpell(Spell spell) {
            if (!spell.name.equals("Nothing")) {
                spellsInEffect.put(spell, spell.activeTurns);
            }
        }
        public Pair<List<Integer>, Spell> getAttack(Spell spell) {
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
            Spell canUse = null;
            for (Map.Entry<Spell, Integer> currentSpell : spellsInEffect.entrySet()) {
                if (currentSpell.getValue().equals(0)) {
                    canUse = currentSpell.getKey();
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
            return Pair.of(values, canUse);
        }
    }

    @Value
    public class Game {
        Player player;
        Player boss;
        boolean hardMode;
        boolean playerWon;
        boolean bossWon;

        List<Spell> spellsAllowedToCast() {
            return spells.stream().filter(s -> s.cost <= player.mana)
                    .filter(s -> !s.name.equals("Nothing"))
                    .filter(s -> !player.spellInEffect(s))
                    .toList();
        }

        boolean ableToCastSpell() {
            return !spellsAllowedToCast().isEmpty();
        }

        Integer manaSpent() {
            return player.spent;
        }

        public List<Game> nextRound() {
            if (hardMode) {
                player.hitPoints--;
                if (player.hitPoints <= 0) {
                    return List.of(new Game(player, boss, true, false, true));
                }
            }
            if (!ableToCastSpell()) {
                return List.of(new Game(player, boss, hardMode, false, true));
            }
            List<Spell> goodSpells = spellsAllowedToCast();
            List<Game> nextRound = new ArrayList<>();
            for (Spell spellCast : goodSpells) {
                Game result = nextGame(spellCast);
                nextRound.add(result);
            }
            return nextRound;
        }

        Game nextGame(Spell spellCast) {
            Spell toReturn = null;
            //Copy information for all possible scenarios.
            Player newPlayer = Player.copyOf(player);
            Player newBoss = Player.copyOf(boss);

            newPlayer.spellsCast.add(spellCast);
            newPlayer.spent += spellCast.cost;
            newPlayer.mana -= spellCast.cost;

            Pair<List<Integer>, Spell> results = newPlayer.getAttack(spellCast);
            if (results.getRight() != null) {
                toReturn = results.getRight();
            }
            List<Integer> effects = results.getLeft();
            newBoss.hitPoints -= effects.get(0);
            newPlayer.hitPoints += effects.get(1);
            newPlayer.mana += effects.get(2);
            if (newBoss.hitPoints <= 0) {
                return new Game(newPlayer, newBoss, hardMode, true, false);
            }

            //Boss Turn -- Boss can't use magic, so automatically get spells[0]
            results = newPlayer.getAttack(spells.get(0));
            effects = results.getLeft();
            newBoss.hitPoints -= effects.get(0);
            newPlayer.hitPoints += effects.get(1);
            newPlayer.mana += effects.get(2);
            Integer bossAttack = Integer.max(1,newBoss.baseDamage - effects.get(3));
            if (newBoss.hitPoints <= 0) {
                return new Game(newPlayer, newBoss, hardMode, true, false);
            }
            newPlayer.hitPoints -= bossAttack;
            if (newPlayer.hitPoints <= 0) {
                return new Game(newPlayer, newBoss, hardMode, false, true);
            }
            return new Game(newPlayer, newBoss, hardMode, false, false);
        }
    }

    List<Spell> spells;

    public void solve(){
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2015,22,false,0);
        Coord2D bossInfo = parseLines(lines);
        setupItems();
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(false, bossInfo);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart1(true, bossInfo);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    Coord2D parseLines(List<String> lines) {
        Pattern pattern = Pattern.compile(":\\s+(?<value>\\d+)");
        List<Integer> values = new ArrayList<>();
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                values.add(Integer.valueOf(matcher.group("value")));
            }
        }
        return new Coord2D(values.get(0), values.get(1));
    }

    public Integer solutionPart1(Boolean hardMode, Coord2D bossInfo) {
        Player player1 = new Player("me", 50,0,0,500,0);
        Player boss = new Player("boss",bossInfo.x,bossInfo.y,0,0,0);
        Game game = new Game(player1, boss, hardMode, false, false);
        PriorityQueue<Game> pq = new PriorityQueue<>(300, Comparator.comparingInt(Game::manaSpent));
        pq.add(game);
        PriorityQueue<Integer> manaSpent = new PriorityQueue<>();
        while (!pq.isEmpty()) {
            Game current = pq.poll();
            List<Game> nextRound = current.nextRound();
            List<Game> validGames = nextRound.stream().filter(g -> !g.bossWon).toList();
            validGames.stream().filter(g -> g.playerWon).forEach(g -> manaSpent.add(g.manaSpent()));
            final Integer manaSpentMax = manaSpent.isEmpty() ? MAX_MANA : manaSpent.peek() + 100;
            pq.addAll(
                    validGames.stream()
                            .filter(g -> !g.playerWon)
                            .filter(g -> g.manaSpent() <= manaSpentMax).toList()
            );
        }
        return manaSpent.poll();
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

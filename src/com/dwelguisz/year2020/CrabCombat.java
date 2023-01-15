package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class CrabCombat extends AoCDay {

    public static class Player {
        Integer id;
        ArrayDeque<Long> cards;

        public Player(Integer id, ArrayDeque<Long> cards) {
            this.id = id;
            this.cards = cards;
        }

        public boolean noMoreCards() {
            return cards.isEmpty();
        }

        public Long getTopCard() {
            return cards.pollFirst();
        }

        public void addCardToBottom(Long card) {
            cards.add(card);
        }

        public Long getScore() {
            Long score = 0L;
            Integer multiplier = 1;
            while (!cards.isEmpty()) {
                score += cards.pollLast() * multiplier;
                multiplier++;
            }
            return score;
        }

        public ArrayDeque<Long> getSubset(Integer subset) {
            Stack<Long> tmp = new Stack<>();
            ArrayDeque<Long> nextSet = new ArrayDeque<>();
            for (int i = 0; i < subset; i++) {
                Long t = cards.pollFirst();
                nextSet.add(t);
                tmp.push(t);
            }
            for (int i = 0; i < subset; i++) {
                cards.addFirst(tmp.pop());
            }
            return nextSet;
        }

        public List<Long> getCards() {
            List<Long> tmp = new ArrayList<>();
            while(!cards.isEmpty()) {
                tmp.add(cards.pollFirst());
            }
            tmp.stream().forEach(t -> cards.add(t));
            return tmp;
        }

    }

    public void solve() {
        Long parseTime = Instant.now().toEpochMilli();
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2020/day22/input.txt");
        List<Player> players = parseLine(lines);
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(players);
        Long part1Time = Instant.now().toEpochMilli();
        players = parseLine(lines);
        Long part2 = solutionPart2(players);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    List<Player> parseLine(List<String> lines) {
        List<Player> players = new ArrayList<>();
        Integer id = 0;
        ArrayDeque<Long> cards = new ArrayDeque<>();
        for (String l : lines) {
            if (l.contains("Player")) {
                id = Integer.parseInt(l.substring(7,8));
            } else if (l.length() == 0) {
                players.add(new Player(id, cards));
                cards = new ArrayDeque<>();
            } else {
                cards.add(Long.parseLong(l));
            }
        }
        players.add(new Player(id, cards));
        return players;
    }

    Long solutionPart1(List<Player> players) {
        while (players.stream().noneMatch(p -> p.noMoreCards())) {
            Long card1 = players.get(0).getTopCard();
            Long card2 = players.get(1).getTopCard();
            if (card1 > card2) {
                players.get(0).addCardToBottom(card1);
                players.get(0).addCardToBottom(card2);
            } else {
                players.get(1).addCardToBottom(card2);
                players.get(1).addCardToBottom(card1);
            }
        }
        if (players.get(0).noMoreCards()) {
            return players.get(1).getScore();
        }
        return players.get(0).getScore();
    }

    Long solutionPart2(List<Player> players) {
        Boolean result = recursiveCombat(players);
        if (result) {
            return players.get(0).getScore();
        }
        return players.get(1).getScore();
    }

    Boolean recursiveCombat(List<Player> players) {
        Set<List<Long>> player1PreviousHands = new HashSet<>();
        Set<List<Long>> player2PreviousHands = new HashSet<>();
        while (players.stream().noneMatch(p -> p.noMoreCards())) {
            List<Long> check = players.get(0).getCards();
            List<Long> check1 = players.get(1).getCards();
            if (player1PreviousHands.contains(check) || player2PreviousHands.contains(check1)) {
                return true;
            }
            player1PreviousHands.add(check);
            player2PreviousHands.add(check1);
            Long card1 = players.get(0).getTopCard();
            Long card2 = players.get(1).getTopCard();
            Boolean player1Wins = false;
            if (card1 > players.get(0).cards.size() || card2 > players.get(1).cards.size()) {
                player1Wins = (card1 > card2);
            } else {
                ArrayDeque<Long> player1newCards = players.get(0).getSubset(card1.intValue());
                Player player1Rec = new Player(1, player1newCards);
                ArrayDeque<Long> player2NewCards = players.get(1).getSubset(card2.intValue());
                Player player2Rec = new Player(2, player2NewCards);
                Boolean result = recursiveCombat(List.of(player1Rec, player2Rec));
                player1Wins = result;
            }
            if (player1Wins) {
                players.get(0).addCardToBottom(card1);
                players.get(0).addCardToBottom(card2);
            } else {
                players.get(1).addCardToBottom(card2);
                players.get(1).addCardToBottom(card1);
            }
        }
        if (players.get(0).noMoreCards()) {
            return false;
        }
        return true;
    }

}

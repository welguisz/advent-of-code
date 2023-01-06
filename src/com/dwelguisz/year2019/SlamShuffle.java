package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class SlamShuffle extends AoCDay {

    public static class ShuffleInstruction {
        public static enum Action {
            NEW_STACK,
            CUT,
            DEAL
        };

        Action action;
        Integer number;

        public ShuffleInstruction(Action action, Integer number) {
            this.action = action;
            this.number = number;
        }
    }

    public static class SpaceCards {
        Long deckSize;
        ArrayDeque<Long> cards;
        Long totalCut;
        Long totalIncrement;

        public SpaceCards(Long deckSize, boolean createDeck) {
            this.deckSize = deckSize;
            this.cards = new ArrayDeque<>();
            this.totalCut = 0L;
            this.totalIncrement = 1L;
            if (createDeck) {
                for (Long i = 0L; i < deckSize; i++) {
                    cards.add(i);
                }
            }
        }

        public void incrementAndCut(ShuffleInstruction instruction) {
            Long inc = 0L;
            Long cut = 0L;
            if (instruction.action == ShuffleInstruction.Action.CUT) {
                inc = 1L;
                cut = instruction.number.longValue();
            } else if (instruction.action == ShuffleInstruction.Action.DEAL) {
                inc = instruction.number.longValue();
                cut = 0L;
            } else if (instruction.action == ShuffleInstruction.Action.NEW_STACK) {
                inc = -1L;
                cut = 1L;
            }
            totalIncrement = (totalIncrement * inc) % deckSize;
            totalCut = (totalCut * inc + cut) % deckSize;
        }

        public void dealIntoNewStack() {
            ArrayDeque<Long> newDeck = new ArrayDeque<>();
            while (!cards.isEmpty()) {
                newDeck.add(cards.pollLast());
            }
            this.cards = newDeck;
        }

        public void cut(Integer number) {
            if (number < 0) {
                Integer num = Math.abs(number);
                for (int i = 0; i < num; i++) {
                    Long card = this.cards.pollLast();
                    this.cards.addFirst(card);
                }
            } else if (number > 0) {
                for (int i = 0; i < number; i++) {
                    Long card = this.cards.pollFirst();
                    this.cards.addLast(card);
                }
            }
        }

        public void dealWithIncrement(Integer number) {
            ArrayDeque<Long> newDeck = new ArrayDeque<>();
            for (Long i = 0L; i < this.deckSize; i++) {
                newDeck.addFirst(0L);
            }
            Integer movement = 0;
            while (!this.cards.isEmpty()) {
                if (movement == 0) {
                    Long card = this.cards.pop();
                    newDeck.pop();
                    newDeck.addFirst(card);
                    movement = number;
                } else {
                    Long card = newDeck.pop();
                    newDeck.addLast(card);
                    movement--;
                }
            }
            this.cards = newDeck;
            this.cut(number);
        }

        public Long indexOf(Long value) {
            Long index = 0L;
            while (index < cards.size()) {
                if (cards.peekFirst().equals(value)) {
                    return index;
                }
                cards.addLast(cards.pollFirst());
                index++;
            }
            return this.cards.getFirst();
        }

    }

    public void solve() {
        Long parseTime = Instant.now().toEpochMilli();
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2019/day22/input.txt");
        List<ShuffleInstruction> instructions = parseLines(lines);
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(instructions, 10007, 2019);
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2(instructions, 119315717514047L, 101741582076661L, 2020);
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public List<ShuffleInstruction> parseLines(List<String> lines) {
        List<ShuffleInstruction> instructions = new ArrayList<>();
        for (String l : lines) {
            String[] parts = l.split(" ");
            if (l.startsWith("cut")) {
                instructions.add(new ShuffleInstruction(ShuffleInstruction.Action.CUT, Integer.parseInt(parts[1])));
            } else if (l.contains("new")) {
                instructions.add(new ShuffleInstruction(ShuffleInstruction.Action.NEW_STACK, 0));
            } else if (l.contains("increment")) {
                instructions.add(new ShuffleInstruction(ShuffleInstruction.Action.DEAL, Integer.parseInt(parts[3])));
            }
        }
        return instructions;
    }

    public Long solutionPart1(List<ShuffleInstruction> instructions, Integer deckSize, Integer startCard) {
        SpaceCards cards = new SpaceCards(deckSize.longValue(), true);
        for (ShuffleInstruction instruction : instructions) {
            if (instruction.action == ShuffleInstruction.Action.CUT) {
                cards.cut(instruction.number);
            } else if (instruction.action == ShuffleInstruction.Action.NEW_STACK) {
                cards.dealIntoNewStack();
            } else if (instruction.action == ShuffleInstruction.Action.DEAL) {
                cards.dealWithIncrement(instruction.number);
            }
        }
        return cards.indexOf(startCard.longValue());
    }

    public Long solutionPart2(List<ShuffleInstruction> instructions, Long deckSize, Long iterations, Integer startCard) {
        SpaceCards cards = new SpaceCards(deckSize, false);
        for (ShuffleInstruction instruction : instructions) {
            cards.incrementAndCut(instruction);
        }
        BigInteger totalInc = new BigInteger(String.valueOf(cards.totalIncrement));
        BigInteger finalInc = totalInc.modPow(new BigInteger(String.valueOf(iterations)), new BigInteger(String.valueOf(deckSize)));
        BigInteger totalCut = new BigInteger(String.valueOf(cards.totalCut));
        BigInteger iteration = new BigInteger(String.valueOf(iterations-1));
        BigInteger ds = new BigInteger(String.valueOf(deckSize));
        BigInteger finalCut = totalCut.multiply(geometricSum(totalInc,iteration, ds)).mod(ds);
        BigInteger base = finalCut.add(new BigInteger(String.valueOf(startCard)));
        BigInteger location = base.multiply(finalInc.modInverse(ds)).mod(ds);
        return Long.parseLong(location.toString());
    }

    public BigInteger geometricSum(BigInteger base, BigInteger exponent, BigInteger modulus) {
        BigInteger val = base.modPow(exponent.add(new BigInteger("1")), modulus).subtract(new BigInteger("1"));
        BigInteger modInv = base.subtract(new BigInteger("1")).modInverse(modulus);
        BigInteger bigNum = val.multiply(modInv);
        return bigNum.mod(modulus);
    }

}

package com.dwelguisz.year2021;

import com.dwelguisz.year2021.helper.Tuple;
import com.dwelguisz.year2021.helper.day21.Multiverse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.counting;

public class AdventDay21 {
    //Dirac Die universe
    // 111 - 3 : 112 - 4 : 113 - 5 : 121 - 4 : 122 - 5 : 123 - 6 : 131 - 5 : 132 - 6 : 133 : 7
    // 211 - 4 : 212 - 5 : 213 - 6 : 221 - 5 : 222 - 6 : 223 - 7 : 231 - 6 : 232 - 7 : 233 : 8
    // 311 - 5 : 312 - 6 : 313 - 7 : 321 - 6 : 322 - 7 : 323 - 8 : 331 - 7 : 332 - 8 : 333 : 9
    public static Map<Integer, Long> DIRAC_UNIVERSE_DISTRIBUTION = new HashMap<>();
    public static Integer QUANTUM_FINAL_SCORE_WIN = 21;

    public static void main(String[] args) {
        Integer player1SP = 8;
        Integer player2SP = 1;
//        DIRAC_UNIVERSE_DISTRIBUTION.add(new Tuple(3,1));
//        DIRAC_UNIVERSE_DISTRIBUTION.add(new Tuple(4,3));
//        DIRAC_UNIVERSE_DISTRIBUTION.add(new Tuple(5,6));
//        DIRAC_UNIVERSE_DISTRIBUTION.add(new Tuple(6,7));
//        DIRAC_UNIVERSE_DISTRIBUTION.add(new Tuple(7,6));
//        DIRAC_UNIVERSE_DISTRIBUTION.add(new Tuple(8,3));
//        DIRAC_UNIVERSE_DISTRIBUTION.add(new Tuple(9,1));
        createDiracUniverseDistribution();
        Long part1 = playGame(player1SP, player2SP);
        Long part2 = playQuantumGame(player1SP, player2SP);
        System.out.println(String.format("Solution Part1: %d",part1));
        System.out.println(String.format("Solution Part1: %d",part2));

    }

    public static void createDiracUniverseDistribution() {
        List<Integer> arraySum = new ArrayList<>();
        for (int roll1 = 1; roll1 <= 3; roll1++) {
            for (int roll2 = 1; roll2 <= 3; roll2++) {
                for (int roll3 = 1; roll3 <= 3; roll3++) {
                    arraySum.add(roll1 + roll2 + roll3);
                }
            }
        }
        DIRAC_UNIVERSE_DISTRIBUTION = arraySum.stream().collect(Collectors.groupingBy(val -> val,counting()));
    }

    public static Long playQuantumGame(Integer player1SP, Integer player2SP) {
        Long player1Wins = 0L;
        Long player2Wins = 0L;
        Map<Tuple<Multiverse, Boolean>, Long> multiverse = new HashMap();
        multiverse.put(new Tuple(new Multiverse(player1SP,player2SP), false),1L);
        while (!multiverse.isEmpty()) {
            Map<Tuple<Multiverse, Boolean>, Long> newMultiverse = new HashMap<>();
            for (Integer jump : DIRAC_UNIVERSE_DISTRIBUTION.keySet()) {
                Long possibleUniverse = DIRAC_UNIVERSE_DISTRIBUTION.get(jump);
                for(Map.Entry<Tuple<Multiverse, Boolean>, Long> entry : multiverse.entrySet()) {
                    Multiverse tmp = entry.getKey().x.clone();
                    if (!entry.getKey().y) {  //Player 1
                        tmp.p1pos += jump;
                        tmp.p1pos = ((tmp.p1pos - 1) % 10) + 1;
                        tmp.p1score += tmp.p1pos;
                        if (tmp.p1score >= QUANTUM_FINAL_SCORE_WIN) {
                            player1Wins += entry.getValue() * possibleUniverse;
                            continue;
                        }
                    } else {  // Player 2
                        tmp.p2pos += jump;
                        tmp.p2pos = ((tmp.p2pos - 1) % 10) + 1;
                        tmp.p2score += tmp.p2pos;
                        if (tmp.p2score >= QUANTUM_FINAL_SCORE_WIN) {
                            player2Wins += entry.getValue() * possibleUniverse;
                            continue;
                        }
                    }
                    newMultiverse.merge(
                            new Tuple(tmp, entry.getKey().y ^ true),
                            entry.getValue() * possibleUniverse,
                            Long::sum);
                }
            }
            multiverse = newMultiverse;
        }
        System.out.println(String.format("Player 1 wins: %d",player1Wins));
        System.out.println(String.format("Player 2 wins: %d",player2Wins));
        return Math.max(player1Wins, player2Wins);
    }


    public static Long playGame(Integer player1SP, Integer player2SP) {
        Long p1Score = 0L;
        Long p2Score = 0L;
        Integer currentP1Position = player1SP;
        Integer currentP2Position = player2SP;
        Long dieRoll = 1L;
        boolean playerRoll = false; // false -> Player 1 roll; true -> Player 2 roll
        while ((p1Score < 1000L) && (p2Score < 1000L)) {
            if (!playerRoll) {
                currentP1Position = oneRoll(currentP1Position, Math.toIntExact(dieRoll));
                p1Score += currentP1Position;
            }  else {
                currentP2Position = oneRoll(currentP2Position, Math.toIntExact(dieRoll));
                p2Score += currentP2Position;
            }
            playerRoll ^= true;
            dieRoll +=3 ;
        }
        System.out.println(String.format("Player 1 Score: %d", p1Score));
        System.out.println(String.format("Player 2 Score: %d", p2Score));
        System.out.println(String.format("Die Roll: %d", dieRoll));
        Long loserScore = (p1Score < 1000L) ? p1Score : p2Score;
        return (loserScore * (dieRoll-1));
    }

    public static Integer oneRoll(Integer currentPosition, Integer dieRoll) {
        Integer unit = dieRoll % 10;
        Integer spaceJumps = ((3*unit) + 3) % 10;
        Integer tmpScore = ((currentPosition + spaceJumps) % 10);
        return (tmpScore == 0) ? 10 : tmpScore;
    }

}

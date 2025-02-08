package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2021.helper.Tuple;
import com.dwelguisz.year2021.helper.day21.Multiverse;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;

public class DiracDice extends AoCDay {
    //Dirac Die universe
    // 111 - 3 : 112 - 4 : 113 - 5 : 121 - 4 : 122 - 5 : 123 - 6 : 131 - 5 : 132 - 6 : 133 : 7
    // 211 - 4 : 212 - 5 : 213 - 6 : 221 - 5 : 222 - 6 : 223 - 7 : 231 - 6 : 232 - 7 : 233 : 8
    // 311 - 5 : 312 - 6 : 313 - 7 : 321 - 6 : 322 - 7 : 323 - 8 : 331 - 7 : 332 - 8 : 333 : 9
    private Map<Integer, Long> DIRAC_UNIVERSE_DISTRIBUTION = new HashMap<>();
    private static Integer QUANTUM_FINAL_SCORE_WIN = 21;

    public DiracDice() {
        super();
        createDiracUniverseDistribution();
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2021, 21, false, 0);
        Pattern pattern = Pattern.compile("starting position: (?<position>\\d+)");
        Matcher matcher = pattern.matcher(lines.get(0));
        Integer player1SP = 0;
        if (matcher.find()) {
            player1SP = Integer.parseInt(matcher.group("position"));
        }
        matcher = pattern.matcher(lines.get(1));
        Integer player2SP = 0;
        if (matcher.find()) {
            player2SP = Integer.parseInt(matcher.group("position"));
        }
        createDiracUniverseDistribution();
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = playGame(player1SP, player2SP);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = playQuantumGame(player1SP, player2SP);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private void createDiracUniverseDistribution() {
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

    private Long playQuantumGame(Integer player1SP, Integer player2SP) {
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
        return Math.max(player1Wins, player2Wins);
    }


    private Long playGame(Integer player1SP, Integer player2SP) {
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
        Long loserScore = (p1Score < 1000L) ? p1Score : p2Score;
        return (loserScore * (dieRoll-1));
    }

    private Integer oneRoll(Integer currentPosition, Integer dieRoll) {
        Integer unit = dieRoll % 10;
        Integer spaceJumps = ((3*unit) + 3) % 10;
        Integer tmpScore = ((currentPosition + spaceJumps) % 10);
        return (tmpScore == 0) ? 10 : tmpScore;
    }

}

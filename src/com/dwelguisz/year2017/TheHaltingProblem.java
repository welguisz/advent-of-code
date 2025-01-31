package com.dwelguisz.year2017;

import com.dwelguisz.base.AoCDay;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheHaltingProblem extends AoCDay {

    public static class StateMachine {
        String id;
        Integer values[];
        Integer positionChange[];
        String nextStates[];
        public StateMachine(String id, Integer values[], Integer positionChange[], String nextStates[]) {
            this.id = id;
            this.values = values;
            this.positionChange = positionChange;
            this.nextStates = nextStates;
        }
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2017,25,false,0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = "Click the link to finish";
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Long solutionPart1() {
        Map<Integer, Integer> values = new HashMap<>();
        Map<String, StateMachine> stateMachine = new HashMap<>();
        stateMachine.put("A",new StateMachine("A", new Integer[]{1,0}, new Integer[]{1,-1},new String[]{"B","B"}));
        stateMachine.put("B",new StateMachine("B", new Integer[]{1,0}, new Integer[]{-1,1},new String[]{"C","E"}));
        stateMachine.put("C",new StateMachine("C", new Integer[]{1,0}, new Integer[]{1,-1},new String[]{"E","D"}));
        stateMachine.put("D",new StateMachine("D", new Integer[]{1,1}, new Integer[]{-1,-1},new String[]{"A","A"}));
        stateMachine.put("E",new StateMachine("E", new Integer[]{0,0}, new Integer[]{1,1},new String[]{"A","F"}));
        stateMachine.put("F",new StateMachine("F", new Integer[]{1,0}, new Integer[]{1,1},new String[]{"E","A"}));
        String state = "A";
        Integer position = 0;
        for (int i = 0; i < 12861455;i++) {
            Integer current = values.getOrDefault(position, 0);
            StateMachine tmp = stateMachine.get(state);
            values.put(position, tmp.values[current]);
            position += tmp.positionChange[current];
            state = tmp.nextStates[current];
        }
        Integer sum = values.entrySet().stream().mapToInt(e -> e.getValue()).sum();
        return sum.longValue();
    }
}

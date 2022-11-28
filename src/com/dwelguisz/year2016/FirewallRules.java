package com.dwelguisz.year2016;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class FirewallRules extends AoCDay {

    List<Pair<Long,Long>> blackListedIPs;

    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2016/day20/input.txt");
        createBlacklistIPs(lines);
        Long part1 = findSmallestIP();
        Integer part2 = findNumberOfIPs();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
    }

    public Long findSmallestIP() {
        boolean found = false;
        Long currentIP = 0L;
        while (!found) {
            Pair<Boolean, Long> resultValue = checkAgainstBlacklist(currentIP);
            found = resultValue.getLeft();
            currentIP = resultValue.getRight();
            currentIP += found ? 0 : 1;
        }
        return currentIP;
    }

    public Integer findNumberOfIPs() {
        Long highestIPAllowed = 4294967295L;
        Integer count = 0;
        Long currentIP = 0L;
        while (currentIP <= highestIPAllowed) {
            Pair<Boolean, Long> resultValue = checkAgainstBlacklist(currentIP);
            if (resultValue.getLeft()) {
                count++;
            }
            currentIP = resultValue.getRight() + 1;
        }
        return count;
    }
    public Pair<Boolean, Long> checkAgainstBlacklist(Long IPAddress) {
        for (Pair<Long, Long> blacklistIPRange : blackListedIPs) {
            Long smallIP = blacklistIPRange.getLeft();
            Long bigIP = blacklistIPRange.getRight();
            if ((smallIP <= IPAddress) && (IPAddress <= bigIP)) {
                return Pair.of(false, bigIP);
            }
        }
        return Pair.of(true, IPAddress);
    }

    public void createBlacklistIPs(List<String> lines) {
        blackListedIPs = new ArrayList<>();
        for (String line : lines) {
            String split[] = line.split("-");
            Long start = Long.parseLong(split[0]);
            Long end = Long.parseLong(split[1]);
            blackListedIPs.add(Pair.of(start, end));
        }
    }


}

package com.dwelguisz.year2019;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.utilities.Coord3D;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TheNBodyProblem extends AoCDay {

    public static class Moon {
        Integer[] location;
        Integer[] velocity;
        public Moon(Integer x, Integer y, Integer z) {
            this.location = new Integer[]{x,y,z};
            this.velocity = new Integer[]{0,0,0};
        }

        public Moon(Moon prevMoon) {
            this.location = new Integer[]{prevMoon.location[0], prevMoon.location[1], prevMoon.location[2]};
            this.velocity = new Integer[]{prevMoon.velocity[0], prevMoon.velocity[1], prevMoon.velocity[2]};
        }
        @Override
        public String toString() {
            return String.format("pos=<x=%d, y=%d, z=%d>, vel=<x=%d, y=%d, z=%d>",this.location[0], this.location[1], this.location[2], this.velocity[0], this.velocity[1], this.velocity[2]);
        }

        public List<String> getUpdatedInformation() {
            List<String> tmp = new ArrayList<>();
            for (int i = 0; i < location.length; i++) {
                tmp.add(String.format("%d_%d", this.location[i], this.velocity[i]));
            }
            return tmp;
        }

        Integer[] gravity(Moon other) {
            Integer[] grav = new Integer[]{0,0,0};
            for (int i = 0; i < location.length; i++) {
                grav[i] = calculateGravity(this.location[i], other.location[i]);
            }
            return grav;
        }

        public void updateVelocity(Integer[] gravity) {
            for (int i = 0; i < velocity.length; i++) {
                this.velocity[i] += gravity[i];
            }
        }

        public void updatePosition() {
            for (int i = 0; i < location.length; i++) {
                this.location[i] += this.velocity[i];
            }
        }

        Integer calculateGravity(Integer a, Integer b) {
            if (a > b) {
                return -1;
            } else if (a < b) {
                return 1;
            } else {
                return 0;
            }
        }

        public Integer potentialEnergy() {
            return Arrays.stream(location).mapToInt(l -> Math.abs(l)).sum();
        }

        public Integer kineticEnergy() {
            return Arrays.stream(velocity).mapToInt(l -> Math.abs(l)).sum();
        }

        public Long totalEnergy() {
            return 1L *potentialEnergy() * kineticEnergy();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Moon other = (Moon) o;
            return (this.location.equals(other.location)) && (this.velocity.equals(other.location));
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.location, this.velocity);
        }

    }



    public void solve() {
        List<String> lines = readFile("/Users/dwelguisz/personal/advent-of-code/src/resources/year2019/day12/input.txt");
        Long parseTime = Instant.now().toEpochMilli();
        List<Moon> moons = parseFile(lines);
        Long startTime = Instant.now().toEpochMilli();
        Long part1 = solutionPart1(copyMoons(moons));
        Long part1Time = Instant.now().toEpochMilli();
        Long part2 = solutionPart2(copyMoons(moons));
        Long part2Time = Instant.now().toEpochMilli();
        System.out.println(String.format("Part 1 Answer: %d",part1));
        System.out.println(String.format("Part 2 Answer: %d",part2));
        System.out.println(String.format("Parsing Time: %d ms.", startTime - parseTime));
        System.out.println(String.format("Time to do Part 1: %d ms.", part1Time - startTime));
        System.out.println(String.format("Time to do Part 2: %d ms.", part2Time - part1Time));
    }

    public List<Moon> parseFile(List<String> lines) {
        List<Moon> moons = new ArrayList<>();
        for (String l : lines) {
            String s[] = l.substring(0,l.length()-1).split(", ");
            Integer x = Integer.parseInt(s[0].substring(s[0].indexOf('=')+1));
            Integer y = Integer.parseInt(s[1].substring(s[1].indexOf('=')+1));
            Integer z = Integer.parseInt(s[2].substring(s[2].indexOf('=')+1));
            moons.add(new Moon(x,y,z));
        }
        return moons;
    }

    public List<Moon> copyMoons(List<Moon> prevMoons) {
        List<Moon> newMoons = new ArrayList<>();
        for (Moon prevMoon : prevMoons) {
            newMoons.add(new Moon(prevMoon));
        }
        return newMoons;
    }
    public Long solutionPart1(List<Moon> moons) {
        for (int step = 0; step < 1000; step++) {
            moons = updateMoons(moons);
        }
        return moons.stream().mapToLong(m -> m.totalEnergy()).sum();
    }

    List<Moon> updateMoons(List<Moon> moons) {
        for (int i = 0; i < moons.size(); i++) {
            for (int j = i+1; j < moons.size(); j++) {
                Integer[] gravity = moons.get(i).gravity(moons.get(j));
                moons.get(i).updateVelocity(gravity);
                gravity = moons.get(j).gravity(moons.get(i));
                moons.get(j).updateVelocity(gravity);
            }
            moons.get(i).updatePosition();
        }
        return moons;
    }

    public boolean reachedBeginningState(List<Moon> initialMoons, List<Moon> currentMoons, Integer i) {
        Boolean allVelocitiesIsZero = currentMoons.stream().map(m -> m.velocity[i]).allMatch(v -> v == 0);
        if (!allVelocitiesIsZero) {
            return false;
        }
        for (int idx = 0; idx < 4; idx++) {
            if (!initialMoons.get(idx).location[i].equals(currentMoons.get(idx).location[i])) {
                return false;
            }
        }
        return true;
    }
    public Long solutionPart2(List<Moon> moons) {
        Map<Integer, Long> period = new HashMap<>();
        final List<Moon> initialMoons = copyMoons(moons);
        Long steps = 0L;
        while (period.size() < 3) {
            moons = updateMoons(moons);
            steps++;
            for(int i = 0; i < 3; i++) {
                if (!period.containsKey(i)) {
                    if (reachedBeginningState(initialMoons, moons, i)) {
                        System.out.println(String.format("Found for axis %d at %d", i, steps));
                        period.put(i, steps);
                    }
                }
            }
        }
        Long tmp1 = lcm(period.get(0), period.get(1));
        return lcm(period.get(2), tmp1);
    }

    public String getAxisInfo(List<Moon> moons, int i) {
        List<String> vals = new ArrayList<>();
        for (Moon moon : moons) {
            vals.add(moon.getUpdatedInformation().get(i));
        }
        return vals.stream().collect(Collectors.joining("_"));
    }
    public Integer axisOscillationPeriod(List<Moon> moons, int i) {
        Set<String> prevInfo = new HashSet<>();
        Boolean repeating = false;
        while (!repeating) {
            String info = getAxisInfo(moons, i);
            repeating = !prevInfo.add(info);
            moons = updateMoons(moons);
        }
        return prevInfo.size();
    }

    public Long lcm(Integer a, Integer b) {
        Integer gcd = Coord2D.gcd(a,b);
        return (1L*a*b)/gcd;
    }

    public Long lcm(Long a, Long b) {
        Long gcd = gcd(a,b);
        return (a*b)/gcd;
    }
    public Long gcd(Long n1, Long n2) {
        if (n1 == 0) {
            return n2;
        } else if (n2 == 0) {
            return n1;
        }
        Long i1 = n1;
        Long i2 = n2;
        while (i2 != 0L) {
            Long tmp = i1;
            i1 = i2;
            i2 = tmp % i2;
        }
        return i1;
    }

}

package com.dwelguisz.year2023;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.base.Matrix;
import com.dwelguisz.base.SpecialMath;
import com.dwelguisz.utilities.Coord2D;
import com.dwelguisz.utilities.Coord3DLong;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class AoC2023Day24 extends AoCDay {

    public class HailStone {
        Coord3DLong position;
        Coord3DLong velocity;
        int hashCode;


        public HailStone(Long posX, Long posY, Long posZ, Long vX, Long vY, Long vZ) {
            this.position = new Coord3DLong(posX, posY, posZ);
            this.velocity = new Coord3DLong(vX, vY, vZ);
            this.hashCode = Objects.hash(this.position, this.velocity);
        }

        Double minNumber = 200000000000000.0;
        Double maxNumber = 400000000000000.0;
        public boolean intersects(HailStone other) {
            Double ax = (double) this.position.x;
            Double ay = (double) this.position.y;
            Double bx = (double) other.position.x;
            Double by = (double) other.position.y;
            Double avx = (double) this.velocity.x;
            Double avy = (double) this.velocity.y;
            Double bvx = (double) other.velocity.x;
            Double bvy = (double) other.velocity.y;

            Double bt = ((bx-ax) / avx - (by-ay) / avy) / (bvy/avy - bvx/avx);
            Double at = (bx + bt*bvx - ax) / avx;

            Double ix = ax+avx*at;
            Double iy = ay+avy*at;

            return bt >= 0 && at >= 0 && minNumber <= ix && ix <= maxNumber && minNumber <= iy && iy <= maxNumber;
        }

        List<Double> toDoubles() {
            List<Double> values = new ArrayList<>();
            values.add((double) position.x);
            values.add(-1.0*(double) position.x);
            values.add((double) position.y);
            values.add(-1.0*(double) position.y);
            values.add((double) position.z);
            values.add(-1.0*(double) position.z);
            values.add((double) velocity.x);
            values.add(-1.0*(double) velocity.x);
            values.add((double) velocity.y);
            values.add(-1.0*(double) velocity.y);
            values.add((double) velocity.z);
            values.add(-1.0*(double) velocity.z);
            values.add((1.0 * position.y * velocity.x) -
                    (1.0 * position.x * velocity.y));
            values.add((1.0 * position.y * velocity.z) -
                    (1.0 * position.z * velocity.y));
            values.add((1.0 * position.z * velocity.x) -
                    (1.0 * position.x * velocity.z));

            return values;
        }
        public String printEquation(String t) {
            StringBuilder tmp = new StringBuilder("" + this.velocity.x + " * " + t + " + " + this.position.x + " = vx * " + t + " + px\n");
            tmp.append("" + this.velocity.y + " * " + t + " + " + this.position.y + " = vy * " + t + " + py\n");
            tmp.append("" + this.velocity.z + " * " + t + " + " + this.position.z + " = vz * " + t + " + pz\n");
            return tmp.toString();
        }

    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2023, 24, false, 0);
        List<HailStone> hailStones = createHailStones(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(hailStones);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solutionPart2(hailStones);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    List<HailStone> createHailStones(List<String> lines) {
        List<HailStone> hailStones = new ArrayList<>();
        for (String line : lines) {
            String[] split = line.split(" @ ");
            List<Long> positions = Arrays.stream(split[0].split(", ")).map(Long::parseLong).collect(Collectors.toList());
            List<Long> velocities = Arrays.stream(split[1].split(", ")).map(s->s.strip()).map(Long::parseLong).collect(Collectors.toList());
            hailStones.add(new HailStone(positions.get(0), positions.get(1), positions.get(2), velocities.get(0), velocities.get(1), velocities.get(2)));
        }
        return hailStones;
    }

    Long solutionPart1(List<HailStone> hailStones) {
        Long count = 0L;
        for (int i = 0; i < hailStones.size(); i++) {
            for (int j = i + 1; j < hailStones.size(); j++) {
                count +=  (hailStones.get(i).intersects(hailStones.get(j))) ? 1 : 0;
            }
        }
        return count;
    }

    Long solutionPart2(List<HailStone> hailStones) {
        List<Double> values = findValues(hailStones, List.of(9,6,2,1,12));
        values.addAll(findValues(hailStones, List.of(9,10,2,5,13)));
        values.addAll(findValues(hailStones, List.of(11,6,4,1,14)));
        return (long) (values.get(0) + values.get(4) + values.get(5));
    }


    List<Double> findValues(List<HailStone> hailStones, List<Integer> rows) {
        List<List<Double>> doubles = hailStones.stream().map(h -> h.toDoubles()).collect(Collectors.toList());
        Double[][] matrix = new Double[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = doubles.get(i+1).get(rows.get((j))) - doubles.get(i).get(rows.get(j));
            }
        }
        Matrix firstMatrix = new Matrix(matrix);
        firstMatrix.forwardElimination();
        firstMatrix.backSub();
        List<Double> values = new ArrayList<>();
        values.add(firstMatrix.getValueAtRowColumn(0,4));
        values.add(firstMatrix.getValueAtRowColumn(1,4));
        values.add(firstMatrix.getValueAtRowColumn(2,4));
        values.add(firstMatrix.getValueAtRowColumn(3,4));
        return values;
    }
}

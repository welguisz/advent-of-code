package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MineCartMadness extends AoCDay {

    public static class Cart{
        Integer x;
        Integer y;
        Integer intersectionSeen;
        Integer direction;
        Integer id;
        Boolean alive;

        public static List<Pair<Integer,Integer>> DELTAS = List.of(
                Pair.of(-1,0), // ^
                Pair.of(0,1), // >
                Pair.of(1,0), // v
                Pair.of(0,-1)); // <
        public Cart(Integer id, Integer x, Integer y, Character direction) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.intersectionSeen = 0;
            this.direction = findDirection(direction);
            alive = true;
        }

        public String toString() {
            return "Cart #" + id + " is at (" + x + "," + y + ")";
        }

        public boolean crashed(Cart other) {
            if (!other.alive) {
                return false;
            }
            if ((this.x == other.x) && (this.y == other.y)) {
                alive = false;
                other.alive = false;
                return true;
            }
            return false;
        }

        public Integer createScore(Character[][] map) {
            int lengthX = map[0].length;
            int lengthY = 99999;
            return y*lengthY + lengthX;
        }
        public boolean tick(Character[][] map, List<Cart> carts) {
            if (!alive) {
                return false;
            }
            Pair<Integer, Integer> deltas = DELTAS.get(direction);
            this.x += deltas.getRight();
            this.y += deltas.getLeft();
            if (map[y][x] == '/') {
                if (direction % 2 == 0) {
                    turnRight();
                } else {
                    turnLeft();
                }
            } else if (map[y][x] == '\\') {
                if (direction %2 == 0) {
                    turnLeft();
                } else {
                    turnRight();
                }
            } else if (map[y][x] == '+') {
                if (intersectionSeen == 0) {  // Turn left
                    turnLeft();
                } else if (intersectionSeen == 2) { // Turn right
                    turnRight();
                }
                intersectionSeen = (intersectionSeen + 1) % 3;
            }
            for (Cart oC : carts) {
                if (this == oC) {
                    continue;
                }
                if (crashed(oC)) {
                    return true;
                }
            }
            return false;
        }

        private void turnLeft() {
            direction--;
            if (direction < 0) {
                direction += 4;
            }
        }

        private void turnRight() {
            direction++;
            direction %= 4;
        }

        public Integer findDirection(Character direction) {
            switch (direction) {
                case '^':
                    return 0;
                case '>':
                    return 1;
                case 'v':
                    return 2;
                case '<':
                    return 3;
            }
            return 10;
        }
    }


    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,13,false,0);
        Character[][] mineMap = createMineMap(lines);
        List<Cart> carts = findCarts(mineMap);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(mineMap, carts);
        timeMarkers[2] = Instant.now().toEpochMilli();
        mineMap = createMineMap(lines);
        carts = findCarts(mineMap);
        part2Answer = solutionPart2(mineMap, carts);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public Pair<Integer, Integer> solutionPart1(Character[][] map, List<Cart> carts) {
        while (true) {
            Collections.sort(carts, new Comparator<Cart>() {
                @Override
                public int compare(Cart o1, Cart o2) {
                    return o1.createScore(map) - o2.createScore(map);
                }
            });
            for (Cart c : carts) {
                if (c.tick(map, carts)) {
                    return Pair.of(c.x,c.y);
                }
            }
        }
    }

    public Pair<Integer, Integer> solutionPart2(Character[][] map, List<Cart> carts) {
        boolean done = false;
        while (!done) {
            Collections.sort(carts, new Comparator<Cart>() {
                @Override
                public int compare(Cart o1, Cart o2) {
                    return o1.createScore(map) - o2.createScore(map);
                }
            });
            for (Cart c : carts) {
                if (c.alive) {
                    c.tick(map, carts);
                }
            }
            done = carts.stream().filter(c1 -> c1.alive).collect(Collectors.toList()).size() == 1;
        }
        List<Cart> remainCarts = carts.stream().filter(c -> c.alive).collect(Collectors.toList());
        Cart lastCart = remainCarts.get(0);
        return Pair.of(lastCart.x, lastCart.y);
    }

    public List<Cart> findCarts(Character[][] map) {
        List<Character> cartChar = List.of('^','v','<','>');
        List<Cart> carts = new ArrayList<>();
        Integer idNum = 0;
        for (int y = 0; y < map.length; y++) {
            for(int x = 0; x < map[y].length; x++) {
                if (cartChar.contains(map[y][x])) {
                    carts.add(new Cart(idNum, x,y,map[y][x]));
                    idNum++;
                    if (List.of('^','v').contains(map[y][x])) {
                        map[y][x] = '|';
                    } else {
                        map[y][x] = '-';
                    }
                }
            }
        }
        return carts;
    }

    public Character[][] createMineMap(List<String> lines) {
        int maxX = lines.stream().mapToInt(l -> l.length()).max().getAsInt();
        int maxY = lines.size();
        Character map[][] = new Character[maxY][maxX];
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                map[y][x] = ' ';
            }
        }
        IntStream.range(0,maxY).forEach(y -> IntStream.range(0,lines.get(y).length()).forEach(x -> map[y][x] = lines.get(y).charAt(x)));
        return map;
    }
}

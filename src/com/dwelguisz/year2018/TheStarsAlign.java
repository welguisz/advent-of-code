package com.dwelguisz.year2018;

import com.dwelguisz.base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TheStarsAlign extends AoCDay {

    public static class Star {
        Integer x;
        Integer y;
        Integer vx;
        Integer vy;

        public Star(Integer x, Integer y, Integer vx, Integer vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        public Star(String x, String y, String vx, String vy) {
            this.x = Integer.parseInt(x);
            this.y = Integer.parseInt(y);
            this.vx = Integer.parseInt(vx);
            this.vy = Integer.parseInt(vy);
        }

        public void increaseOneSecond() {
            x += vx;
            y += vy;
        }

        public void goBackOneSecond() {
            x -= vx;
            y -= vy;
        }
    }

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2018,10,false,0);
        List<Star> stars = createStars(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        solutionPart1(stars);
        timeMarkers[2] = Instant.now().toEpochMilli();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    public void solutionPart1(List<Star> stars) {
        boolean done = false;
        Integer iteration = 0;
        Integer minX = 0;
        Integer maxX = 0;
        Integer minY = 0;
        Integer maxY = 0;
        while (!done) {
            iteration++;
            for (Star s : stars) {
                s.increaseOneSecond();
            }
            minY = stars.stream().mapToInt(s -> s.y).min().getAsInt();
            maxY = stars.stream().mapToInt(s -> s.y).max().getAsInt();
            minX = stars.stream().mapToInt(s -> s.x).min().getAsInt();
            maxX = stars.stream().mapToInt(s -> s.x).max().getAsInt();
            done = (maxY - minY < 10) || (maxX - minX < 60);
        }
        printStars(stars, minX, maxX, minY, maxY);
        part2Answer = iteration;
    }

    void printStars(List<Star> stars, Integer minX, Integer maxX, Integer minY, Integer maxY) {
        String grid[][] = new String[(maxX-minX)+1][(maxY-minY)+1];
        Integer diffY = (maxY - minY)+1;
        Integer diffX = (maxX - minX)+1;
        for (int y = 0; y < diffY; y++) {
            for (int x = 0; x < diffX; x++) {
                grid[x][y] = " ";
            }
        }
        for(Star s : stars) {
            Integer x = (s.x - minX);
            Integer y = (s.y - minY);
            grid[x][y] = "#";
        }
        StringBuilder sb = new StringBuilder("\n");
        for (int y = 0; y < diffY; y++) {
            for (int x = 0; x < diffX; x++) {
                sb.append(grid[x][y]);
            }
            sb.append("\n");
        }
        part1Answer = sb.toString();
    }

    public List<Star> createStars(List<String> lines) {
        List<Star> stars = new ArrayList<>();
        //Pattern pattern = Pattern.compile("position=<\\s*(-?\\d+),\\s*(-?\\d+)> velocity=<]\\s*(-?\\d+),\\s*(\\?\\d+)>");
        for (String l : lines) {
            String position = l.substring(l.indexOf("n=<")+3,l.indexOf("> "));
            String velocity = l.substring(l.indexOf("y=<")+3,l.indexOf(">",l.indexOf("> ") + 3));
            Pair<Integer, Integer> pInter = parseCoordinates(position);
            Pair<Integer, Integer> vInter = parseCoordinates(velocity);
            stars.add(new Star(pInter.getLeft(), pInter.getRight(),vInter.getLeft(),vInter.getRight()));
        }
        return stars;
    }

    Pair<Integer, Integer> parseCoordinates(String str) {
        Integer startPx = str.charAt(0) == ' ' ? 1 : 0;
        Integer px = Integer.parseInt(str.substring(startPx,str.indexOf(",")));
        String justPy = str.substring(str.indexOf(", ")+2);
        Integer startPy = justPy.charAt(0) == ' ' ? 1 : 0;
        Integer py = Integer.parseInt(justPy.substring(startPy));
        return Pair.of(px, py);
    }
}

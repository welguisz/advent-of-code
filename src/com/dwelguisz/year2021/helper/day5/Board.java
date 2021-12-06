package com.dwelguisz.year2021.helper.day5;

import java.util.ArrayList;
import java.util.List;

public class Board {
    List<Line> lines;
    public static int MAX_SIZE = 1000;
    List<List<Integer>> map;

    public Board(List<String> values, boolean diagonal) {
        lines = new ArrayList<>();
        for (String value: values) {
            lines.add(new Line(value));
        }
        createBoard(diagonal);
    }

    public int avoidSpaces() {
        int value = 0;
        for(List<Integer> row : map) {
            for(Integer point: row) {
                if (point > 1) {
                    value++;
                }
            }
        }
        return value;
    }

    public void drawBoard() {
        for (List<Integer> row : map) {
            StringBuffer buffer = new StringBuffer();
            for(Integer value : row) {
                if (value == 0) {
                    buffer.append('.');
                } else {
                    buffer.append(value);
                }
            }
            System.out.println(buffer.toString());
        }
    }

    private void createBoard(boolean diagonal) {
        map = new ArrayList<>();
        for (int i = 0; i < MAX_SIZE; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < MAX_SIZE; j++) {
                row.add(0);
            }
            map.add(row);
        }
        for(Line line: lines) {
            List<Point> points = line.drawLine(diagonal);
            for(Point point : points) {
                int value = map.get(point.getY()).get(point.getX());
                value++;
                map.get(point.getY()).set(point.getX(), value);
            }
        }
    }

}

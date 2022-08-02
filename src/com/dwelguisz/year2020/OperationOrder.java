package com.dwelguisz.year2020;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2020.helper.operation.order.ArithmeticExpression;
import com.dwelguisz.year2020.helper.operation.order.Expression;
import com.dwelguisz.year2020.helper.operation.order.IntegerExpression;
import com.dwelguisz.year2020.helper.operation.order.OperationExpression;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class OperationOrder extends AoCDay {
    public void solve() {
        List<String> lines = readFile("/home/dwelguisz/personal/advent-of-code/src/resources/year2020/day18/input.txt");
        List<Expression> expressions = parseLines(lines);
        Long part1 = solutionPart1(expressions);
        System.out.println(String.format("Part 1 Answer: %d",part1));
        Long part2 = solutionPart2(expressions);
        System.out.println(String.format("Part 2 Answer: %d",part2));

    }

    public List<Expression> parseLines(List<String> lines) {
        List<Expression> expressions = new ArrayList<>();
        for (String line : lines) {
            String[] items = line.replace(" ", "").split("");
            Pair<Integer, Expression> values = parseSmallPart(items, 0);
            assert(values.getLeft() == items.length);
            expressions.add(values.getRight());
        }
        return expressions;
    }

    public Pair<Integer, Expression> parseSmallPart(String [] items, Integer position) {
        List<Expression> parts = new ArrayList<>();
        while (position < items.length) {
            if (")".equals(items[position])) {
                return  Pair.of(position, new ArithmeticExpression(parts));
            } else if ("(".equals(items[position])) {
                Pair<Integer, Expression> value = parseSmallPart(items, position + 1);
                assert(")".equals(items[position]));
                position = value.getLeft();
                parts.add(value.getRight());
            } else if ("+".equals(items[position])) {
                parts.add(new OperationExpression(OperationExpression.allowedOps.add));
            } else if ("*".equals(items[position])) {
                parts.add(new OperationExpression(OperationExpression.allowedOps.multiply));
            } else {
                parts.add(new IntegerExpression(Integer.parseInt(items[position])));
            }
            position++;
        }
        return Pair.of(position, new ArithmeticExpression(parts));
    }

    public Long solutionPart1(List<Expression> expressions) {
        Long result = 0L;
        for(Expression expression : expressions) {
            result += expression.evaluate();
        }
        return result;
    }

    public Long solutionPart2(List<Expression> expressions) {
        Long result = 0L;
        for(Expression expression : expressions) {
            result += expression.advanced_evaluate();
        }
        return result;
    }


}

package com.dwelguisz.year2020.helper.operation.order;

import java.util.List;

public class ArithmeticExpression implements Expression{
    List<Expression> parts;

    public ArithmeticExpression(List<Expression> parts) {
        this.parts = parts;
    }

    public Long evaluate() {
        Long result = parts.get(0).evaluate();
        for (int i = 1; i < parts.size(); i+=2) {
            Expression op = parts.get(i);
            Expression next = parts.get(i+1);
            if (op instanceof  OperationExpression) {
                result = ((OperationExpression) op).operation(result, next.evaluate());
            }
        }
        return result;
    }

    public Long advanced_evaluate() {
        Long result = 1L;
        Long factor = parts.get(0).advanced_evaluate();
        for(int i = 1; i < parts.size(); i+=2) {
            Expression op = parts.get(i);
            Expression next = parts.get(i+1);
            if (op instanceof  OperationExpression) {
                OperationExpression ope = (OperationExpression) op;
                if (ope.value == OperationExpression.allowedOps.add) {
                    factor += next.advanced_evaluate();
                } else if (ope.value == OperationExpression.allowedOps.multiply) {
                    result *= factor;
                    factor = next.advanced_evaluate();
                }
            }
        }
        return result * factor;
    }

}

package com.dwelguisz.year2020.helper.operation.order;

public class IntegerExpression implements Expression{
    Integer value;
    public IntegerExpression(Integer value) {
        this.value = value;
    }

    public Long evaluate() {
        return 0L + this.value;
    }

    public Long advanced_evaluate() {
        return evaluate();
    }

}

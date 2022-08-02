package com.dwelguisz.year2020.helper.operation.order;

public class OperationExpression implements Expression{
    public enum allowedOps {add, multiply};
    public allowedOps value;
    public OperationExpression(allowedOps value) {
        this.value = value;
    }
    public Long evaluate() {
        return 0L;
    }

    @Override
    public Long advanced_evaluate() {
        return 0L;
    }

    public Long operation(Long op1, Long op2) {
        if (value == allowedOps.add) {
            return op1 + op2;
        } else if (value == allowedOps.multiply){
            return  op1 * op2;
        }
        return 0L;
    }

}

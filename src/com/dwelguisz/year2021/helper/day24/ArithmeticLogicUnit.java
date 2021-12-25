package com.dwelguisz.year2021.helper.day24;

import static java.lang.Integer.parseInt;

public class ArithmeticLogicUnit {
    public Integer w;
    public Integer x;
    public Integer y;
    public Integer z;


    public ArithmeticLogicUnit(Integer w, Integer x, Integer y, Integer z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = y;
    }

    @Override
    public String toString() {
        return String.format("{w: %d, x: %d, y: %d, z: %d}",w,x,y,z);
    }

    public void runInstruction(String oper, String op1, String op2, boolean reverse) {
        Integer value = parseOperand(op1);
        if ("inp".equals(oper)) {
            value = parseOperand(op2);
        } else if ("add".equals(oper)) {
            value = add(op1, op2, reverse);
        } else if ("mul".equals(oper)) {
            value = mul(op1, op2, reverse);
        } else if ("div".equals(oper)) {
            value = div(op1, op2, reverse);
        } else if ("mod".equals(oper)) {
            value = mod(op1, op2, reverse);
        } else if ("eql".equals(oper)) {
            value = equal(op1, op2, reverse);
        }
        storeValue(op1, value);
    }

    public void storeValue(String op, Integer value) {
        if ("w".equals(op)) {
            this.w = value;
            return;
        } else if ("x".equals(op)) {
            this.x = value;
            return;
        } else if ("y".equals(op)) {
            this.y = value;
            return;
        } else if ("z".equals(op)) {
            this.z = value;
            return;
        }

    }

    public Integer add(String op1, String op2, boolean reverse) {
        return reverse ? (parseOperand(op1) - parseOperand(op2)) : parseOperand(op1) + parseOperand(op2);
    }
    
    public Integer mul(String op1, String op2, boolean reverse) {
        return reverse ? div(op1, op2, !reverse) : parseOperand(op1) * parseOperand(op2);
    }
    
    public Integer div(String op1, String op2, boolean reverse) {
        return reverse ? mul(op1, op2, !reverse) : parseOperand(op1) / parseOperand(op2);
    }
    
    public Integer mod(String op1, String op2, boolean reverse) {
        return reverse ? parseOperand(op1) : parseOperand(op1) % parseOperand(op2);
    }
    
    public Integer equal(String op1, String op2, boolean reverse) {
        if (reverse) {
            if (parseOperand(op1) == 1) {
                return parseOperand(op2);
            } else {
                return parseOperand(op2) + 15;
            }
        } else {
            return (parseOperand(op1) == parseOperand(op2)) ? 1 : 0;
        }
    }
    
    public Integer parseOperand(String op) {
        if ("w".equals(op)) {
            return this.w;
        } else if ("x".equals(op)) {
            return this.x;
        } else if ("y".equals(op)) {
            return this.y;
        } else if ("z".equals(op)) {
            return this.z;
        }
        return parseInt(op);
    }
}

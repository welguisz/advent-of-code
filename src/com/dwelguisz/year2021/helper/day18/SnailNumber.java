package com.dwelguisz.year2021.helper.day18;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SnailNumber {
    private SnailNumber left;
    private SnailNumber right;
    private Integer value;
    private SnailNumber parent;

    public SnailNumber(SnailNumber left, SnailNumber right) {
        this.left = left;
        this.right = right;
        left.parent = this;
        right.parent = this;
    }

    public SnailNumber(Integer value) {
        this.value = value;
    }

    public SnailNumber add(SnailNumber toAdd) {
        SnailNumber sn = new SnailNumber(this, toAdd);
        sn.reduce();
        return sn;
    }

    public SnailNumber clone() {
        if (isDigit()) return new SnailNumber(value);
        return new SnailNumber(left.clone(), right.clone());
    }

    public int magnitude() {
        if (isDigit()) return value;
        return 3 * left.magnitude() + 2 * right.magnitude();
    }

    private void reduce() {
        while (explode() || split());
    }

    private boolean isDigit() {
        return value != null;
    }

    private int level() {
        if (parent == null) return 0;
        return parent.level() + 1;
    }

    private SnailNumber origin() {
        SnailNumber current = this;
        while (current.parent != null) current = current.parent;
        return current;
    }

    private List<SnailNumber> allDigits() {
        if (isDigit()) return List.of(this);
        List<SnailNumber> all = new ArrayList<>();
        all.addAll(left.allDigits());
        all.addAll(right.allDigits());
        return all;
    }

    private Optional<SnailNumber> protect(List<SnailNumber> list, int i) {
        return (i < 0 || i >= list.size()) ? Optional.empty() : Optional.of(list.get(i));
    }

    private Optional<SnailNumber> nearestDigit(SnailNumber number, int delta) {
        List<SnailNumber> digits = origin().allDigits();
        return protect(digits, digits.indexOf(number) + delta);
    }

    private boolean explode() {
        if (!isDigit()) {
            if (level() == 4) {
                nearestDigit(left, -1).ifPresent(sn -> sn.value += left.value);
                nearestDigit(right, 1).ifPresent(sn -> sn.value += right.value);
                left = null; right = null;
                value = 0;
                return true;
            } else {
                return left.explode() || right.explode();
            }
        }
        return false;
    }

    private boolean split() {
        if (isDigit()) {
            if (value >= 10) {
                left = new SnailNumber(value / 2);
                right = new SnailNumber(value / 2 + value % 2);
                left.parent = this; right.parent = this;
                value = null;
                return true;
            }
        } else {
            return left.split() || right.split();
        }
        return false;
    }

    @Override
    public String toString() {
        if (isDigit()) return value.toString();
        return "[" + left + "," + right + "]";
    }
}

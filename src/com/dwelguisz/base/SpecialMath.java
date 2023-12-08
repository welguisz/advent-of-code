package com.dwelguisz.base;

public class SpecialMath {
    public static Long lcm(Long a, Long b) {
        Long gcd = gcd(a,b);
        return (a*b)/gcd;
    }
    public static Long gcd(Long n1, Long n2) {
        if (n1 == 0) {
            return n2;
        } else if (n2 == 0) {
            return n1;
        }
        Long i1 = n1;
        Long i2 = n2;
        while (i2 != 0L) {
            Long tmp = i1;
            i1 = i2;
            i2 = tmp % i2;
        }
        return i1;
    }
}

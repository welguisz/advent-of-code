package com.dwelguisz.base;

import static java.util.Arrays.stream;

public class ChineseRemainderTheorem {
    public static Long chineseRemainder(Long[] n, Long[] a) {

        Long prod = stream(n).reduce(1L, (i, j) -> i * j);

        Long p, sm = 0L;
        for (int i = 0; i < n.length; i++) {
            p = prod / n[i];
            sm += a[i] * mulInv(p, n[i]) * p;
        }
        return sm % prod;
    }

    private static Long mulInv(Long a, Long b) {
        Long b0 = b;
        Long x0 = 0L;
        Long x1 = 1L;

        if (b == 1)
            return 1L;

        while (a > 1) {
            Long q = a / b;
            Long amb = a % b;
            a = b;
            b = amb;
            Long xqx = x1 - q * x0;
            x1 = x0;
            x0 = xqx;
        }

        if (x1 < 0)
            x1 += b0;

        return x1;
    }
}

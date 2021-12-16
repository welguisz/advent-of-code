package com.dwelguisz.year2021;

import java.util.HashMap;
import java.util.Map;

public class CommonConversions {

    static Map<String, String> hexMap;
    static {
        hexMap = new HashMap<>();
        hexMap.put("0","0000");
        hexMap.put("1","0001");
        hexMap.put("2","0010");
        hexMap.put("3","0011");
        hexMap.put("4","0100");
        hexMap.put("5","0101");
        hexMap.put("6","0110");
        hexMap.put("7","0111");
        hexMap.put("8","1000");
        hexMap.put("9","1001");
        hexMap.put("A","1010");
        hexMap.put("B","1011");
        hexMap.put("C","1100");
        hexMap.put("D","1101");
        hexMap.put("E","1110");
        hexMap.put("F","1111");
    }

    public static String HexChar2Nibble(String hex) {
        return hexMap.get(hex);
    }

    public static String convertHexToBin(String line) {
        StringBuffer sb = new StringBuffer();
        String[] lineChar = line.split("");
        for (int i = 0; i < lineChar.length; i++) {
            sb.append(HexChar2Nibble((lineChar[i])));
        }
        return sb.toString();
    }

}

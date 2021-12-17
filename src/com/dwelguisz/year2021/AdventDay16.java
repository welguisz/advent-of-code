package com.dwelguisz.year2021;

import com.dwelguisz.year2021.helper.day16.NumberPacket;
import com.dwelguisz.year2021.helper.day16.OperatorPacket;
import com.dwelguisz.year2021.helper.day16.Packet;

import java.util.ArrayList;
import java.util.List;

import static com.dwelguisz.year2021.helper.CommonConversions.convertHexToBin;
import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Long.parseLong;

public class AdventDay16 {

    public static void main(String[] args) {
        List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2021/day16/testcase.txt");
        String hex2bin = convertHexToBin(lines.get(0));
        List<Object> objects = readPacket(hex2bin, 0);
        Packet finalPacket = (Packet) objects.get(1);
        Integer finalPosition = (Integer) objects.get(0);
        System.out.println(String.format("Total length of Hex String: %d", hex2bin.length()));
        System.out.println(String.format("Final position: %d", finalPosition));
        System.out.println(String.format("Final Packet Version Sum: %d", finalPacket.versionSum()));
        System.out.println(String.format("Final Packet Value: %d", finalPacket.getValue()));
        System.out.println("\n\nDecoded string:\n");
        System.out.println(finalPacket);
    }

    public static List<Long> readNumber(String hex2bin, Integer pos) {
        Long nibble = convertBits(hex2bin, pos,5);
        pos += 5;
        Long result = 0L;
        while ( nibble > 15L) {
            result *= 16;
            result += nibble - 16;
            nibble = convertBits(hex2bin, pos, 5);
            pos += 5;
        }
        result *= 16;
        result += 16;
        return List.of(result, Long.valueOf(pos));
    }

    public static Long convertBits(String hex2bin, Integer position, Integer length) {
        return parseLong(hex2bin.substring(position, position+length),2);
    }


    public static List<Object> readPacket(String hex2bin, Integer position) {
        List<Object> objects = new ArrayList<>();
        Integer version = Math.toIntExact(convertBits(hex2bin, position, 3));
        position += 3;
        Integer typeId = Math.toIntExact(convertBits(hex2bin, position, 3));
        position += 3;
        if (typeId == 4) {
            List<Long> parseValues = readNumber(hex2bin, position);
            position = Math.toIntExact(parseValues.get(1));
            objects = List.of(position, new NumberPacket(version, parseValues.get(0)));
        } else {
            Integer lengthType = Math.toIntExact(convertBits(hex2bin, position, 1));
            position += 1;
            if (lengthType == 0) {
                Long length =convertBits(hex2bin, position, 15);
                position += 15;
                Integer endPosition = Math.toIntExact(position + length);
                List<Packet> packets = new ArrayList<>();
                while (position < endPosition) {
                    objects = readPacket(hex2bin, position);
                    position = (Integer) objects.get(0);
                    packets.add((Packet) objects.get(1));
                }
                objects = List.of(position, new OperatorPacket(version, typeId, packets));
            } else {
                Integer numberOfPackets = Math.toIntExact(convertBits(hex2bin, position, 11));
                position += 11;
                List<Packet> packets = new ArrayList<>();
                for (int i = 0; i < numberOfPackets; i++) {
                    objects = readPacket(hex2bin, position);
                    position = (Integer) objects.get(0);
                    packets.add((Packet) objects.get(1));
                }
                objects = List.of(position, new OperatorPacket(version, typeId, packets));
            }
        }
        return objects;
    }


 }

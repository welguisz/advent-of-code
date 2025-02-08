package com.dwelguisz.year2021;

import com.dwelguisz.base.AoCDay;
import com.dwelguisz.year2021.helper.day24.ArithmeticLogicUnit;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.dwelguisz.year2021.helper.ReadFile.readFile;
import static java.lang.Integer.parseInt;

public class AdventDay24 extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResoruceFile(2021,24,false,0);
        List<List<String>> differentInputs = parseInputs(lines);
        Integer[][]differentValues = differentInstructions(differentInputs);
        printDifferentValues(differentValues);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solutionPart1(differentValues);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = "To work on";
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

//    input[2] + 13 + (-13) = input[3]
//    input[4] + 15 + (-13) = input[5]
//    input[1] + 13 + (-7) = input[6]
//    input[9] + 1 + (-4) = input[10]
//    input[8] + 16 + (-9) = input[11]
//    input[7] + 5 + (-13) = input[12]
//    input[0] + 2 + (-9) = input[13]


//    input[0] - 7 = input[13]
//    input[1] = input[6] - 6
//    input[2] = input[3]
//    input[4] = input[5] - 2
//    input[7] - 8 = input[12]
//    input[8] = input[11] - 7
//    input[9] - 3 = input[10]

//    01234567890123
//    81111379141811
//    81111379841811


    public static void printDifferentValues(Integer[][]differentValues) {
        for (int j = 0; j < differentValues[0].length; j++) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < differentValues.length; i++) {
                sb.append(String.format("%1$3s:::", differentValues[i][j]));
            }
            //System.out.println(sb.toString());
        }
    }

    public static List<List<String>> parseInputs(List<String> lines) {
        List<List<String>> inputs = new ArrayList<>();
        List<String> currentInput = new ArrayList<>();
        for (String line : lines) {
            if (line.contains("inp")) {
                if (!currentInput.isEmpty()) {
                    inputs.add(currentInput);
                    currentInput = new ArrayList<>();
                }
            }
            currentInput.add(line);
        }
        if (!currentInput.isEmpty()) {
            inputs.add(currentInput);
        }
        return inputs;
    }

    public Integer[][] differentInstructions(List<List<String>> inputs) {
        List<Integer> differentLines = new ArrayList<>();
        for (int i = 0; i < inputs.get(0).size(); i++) {
            if (!inputs.get(0).get(i).equals(inputs.get(1).get(i))) {
                //System.out.println(String.format("Diff Line from ALU pass 0: %s", inputs.get(0).get(i)));
                differentLines.add(i);
            }
        }
        Integer[][] diffValues = new Integer[inputs.size()][differentLines.size()];
        Integer i = 0;
        for (List<String> input : inputs) {
            for (Integer j = 0; j < differentLines.size(); j++) {
                String[] parsedLined = input.get(differentLines.get(j)).split(" ");
                diffValues[i][j] = parseInt(parsedLined[2]);
            }
            i++;
        }
        return diffValues;
    }

    public  void checkSolution(List<String> lines) {
        ArithmeticLogicUnit alu = new ArithmeticLogicUnit(0,0,0,0);
        //List<String> lines = readFile("/home/dwelguisz/advent-of-code/src/resources/year2021/day24/input_work.txt");
        for (String line : lines) {
            String[] parsedLine = line.split(" ");
            alu.runInstruction(parsedLine[0], parsedLine[1], parsedLine[2], false);
        }
        //System.out.println(String.format("z is %d", alu.z));

    }

    public static String solutionPart1(Integer[][] inputs){
        StringBuffer sb = new StringBuffer();
        Stack<String> stack = new Stack<>();


        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i][0] >= 0) {
                stack.push(String.format("input[%d] + %d",i,inputs[i][1]));
            } else {
                StringBuffer equation = new StringBuffer(stack.pop());
                equation.append(String.format(" + (%d) = input[%d]\n",inputs[i][0],i));
                sb.append(equation);
            }
        }
        return sb.toString();
    }
}

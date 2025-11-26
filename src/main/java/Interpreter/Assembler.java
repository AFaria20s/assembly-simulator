package Interpreter;

import java.util.Arrays;

public class Assembler {
    public int parseInstruction(String instruction) {
        String[] parts = instruction.toLowerCase().trim().split("\\s+");
        System.out.println(Arrays.toString(parts) + "\n" + parts.length);

        Instructions instr = Instructions.fromString(parts[0]);
        int machineCode = instr.getOpcode() << 24;

        switch (instr.getArgCount()) {
            case 3 -> {
                Registers r1 = Registers.fromString(parts[1]);
                Registers r2 = Registers.fromString(parts[2]);
                int r3;

                if (isRegister(parts[3])) {
                    r3 = Registers.fromString(parts[3]).getCode();
                } else {
                    r3 = parseImmediate(parts[3]);
                }

                machineCode |= (r1.getCode() << 16);
                machineCode |= (r2.getCode() << 8);
                machineCode |= r3;
            }
            case 2 -> {
                Registers r1 = Registers.fromString(parts[1]);
                int value = parseImmediate(parts[2]);
                machineCode |= (r1.getCode() << 16);
                machineCode |= value;
            }
            case 1 -> {
                if (instr == Instructions.JMP) {
                    int addr = parseImmediate(parts[1]);
                    machineCode |= addr;
                } else if (instr == Instructions.JMPR) {
                    Registers r = Registers.fromString(parts[1]);
                    machineCode |= (r.getCode() << 16);
                }
            }
        }

        return machineCode;
    }

    private boolean isRegister(String s) {
        try {
            Registers.fromString(s);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private int parseImmediate(String value) {
        if (value.startsWith("0x")) {
            return Integer.parseInt(value.substring(2), 16);
        } else if (value.startsWith("#")) {
            return Integer.parseInt(value.substring(1));
        } else {
            return Integer.parseInt(value);
        }
    }
}

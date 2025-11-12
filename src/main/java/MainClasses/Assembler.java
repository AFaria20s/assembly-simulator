package MainClasses;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The Assembler class simulates a simple assembler that converts assembly instructions
 * into machine code. It supports several instructions (e.g., mov, add, sub) and registers
 * (e.g., r0, r1, r2). The resulting machine code is represented as an integer.
 */
public class Assembler {

    private static final Map<String, Integer> OPCODES = new HashMap<>();
    private static final Map<String, Integer> REGISTERS = new HashMap<>();
    private static final Map<String, Integer> ARG_COUNTS = new HashMap<>();

    static {
        OPCODES.put("mov",  0x01);
        OPCODES.put("add",  0x02);
        OPCODES.put("sub",  0x03);
        OPCODES.put("mul",  0x04);
        OPCODES.put("div",  0x05);
        OPCODES.put("and",  0x06);
        OPCODES.put("or",   0x07);
        OPCODES.put("str",  0x08);
        OPCODES.put("ld",   0x09);
        OPCODES.put("jmp",  0x0A);
        OPCODES.put("jmpe", 0x0B);
        OPCODES.put("jmpn", 0x0C);
        OPCODES.put("jmpg", 0x0D);
        OPCODES.put("jmpl", 0x0E);
        OPCODES.put("jmpr", 0x0F);
        OPCODES.put("nop",  0xFF);

        REGISTERS.put("r0", 0);
        REGISTERS.put("r1", 1);
        REGISTERS.put("r2", 2);
        REGISTERS.put("r3", 3);
        REGISTERS.put("r4", 4);
        REGISTERS.put("r5", 5);
        REGISTERS.put("r6", 6);
        REGISTERS.put("r7", 7);

        ARG_COUNTS.put("mov",  2);
        ARG_COUNTS.put("add",  3);
        ARG_COUNTS.put("sub",  3);
        ARG_COUNTS.put("mul",  3);
        ARG_COUNTS.put("div",  3);
        ARG_COUNTS.put("and",  3);
        ARG_COUNTS.put("or",   3);
        ARG_COUNTS.put("str",  2);
        ARG_COUNTS.put("ld",   2);
        ARG_COUNTS.put("jmp",  1);
        ARG_COUNTS.put("jmpe", 3);
        ARG_COUNTS.put("jmpn", 3);
        ARG_COUNTS.put("jmpg", 3);
        ARG_COUNTS.put("jmpl", 3);
        ARG_COUNTS.put("jmpr", 1);
        ARG_COUNTS.put("nop",  0);
    }

    /**
     * Parses an assembly instruction into machine code.
     */
    public int parseInstruction(String instruction) {
        String[] parts = instruction.toLowerCase().trim().split("\\s+");
        System.out.println(Arrays.toString(parts)+"\n"+parts.length);
        String mnemonic = parts[0];
        int opcode = OPCODES.get(mnemonic);
        int argCount = ARG_COUNTS.get(mnemonic);
        int machineCode = opcode << 24;

        if (argCount == 3) {
            int r1 = REGISTERS.getOrDefault(parts[1], -1);
            int r2 = REGISTERS.getOrDefault(parts[2], -1);
            int r3;

            machineCode |= (r1 << 16);
            machineCode |= (r2 << 8);

            if (REGISTERS.containsKey(parts[3])) {
                r3 = REGISTERS.get(parts[3]);
            } else {
                r3 = parseImmediate(parts[3]);
            }

            machineCode |= r3;

        } else if (argCount == 2) {
            int r1 = REGISTERS.get(parts[1].toLowerCase());
            int value = parseImmediate(parts[2]);
            machineCode |= (r1 << 16);
            machineCode |= value;

        } else if (argCount == 1) {
            if (mnemonic.equals("jmp")) {
                int addr = parseImmediate(parts[1]);
                machineCode |= addr;
            } else if (mnemonic.equals("jmpr")) {
                int reg = REGISTERS.get(parts[1]);
                machineCode |= (reg << 16);
            }
        }

        return machineCode;
    }

    /**
     * Parses an immediate value from a string, supporting hex (0x), decimal, and # prefix.
     */
    private int parseImmediate(String value) {
        if (value.startsWith("0x")) {
            return Integer.parseInt(value.substring(2), 16);
        } else if (value.startsWith("#")) {
            return Integer.parseInt(value.substring(1)); // decimal
        } else {
            return Integer.parseInt(value); // plain decimal or label-resolved int
        }
    }
}

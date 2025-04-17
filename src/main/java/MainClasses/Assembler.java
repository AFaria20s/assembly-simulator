package MainClasses;

import java.util.HashMap;
import java.util.Map;

/**
 * The Assembler class simulates a simple assembler that converts assembly instructions
 * into machine code. It supports several instructions (e.g., mov, add, sub) and registers
 * (e.g., r0, r1, r2). The resulting machine code is represented as an integer.
 */
public class Assembler {

    // HashMap to store opcode mappings for each instruction mnemonic
    private static final Map<String, Integer> OPCODES = new HashMap<>();

    // HashMap to store register mappings for register names
    private static final Map<String, Integer> REGISTERS = new HashMap<>();

    // HashMap to store the number of arguments each instruction requires
    private static final Map<String, Integer> ARG_COUNTS = new HashMap<>();

    // Static block to initialize the opcode, register, and argument count maps
    static {
        // Initialize the opcode mappings for each mnemonic
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

        // Initialize the register mappings for register names
        REGISTERS.put("r0", 0);
        REGISTERS.put("r1", 1);
        REGISTERS.put("r2", 2);
        REGISTERS.put("r3", 3);
        REGISTERS.put("r4", 4);
        REGISTERS.put("r5", 5);
        REGISTERS.put("r6", 6);
        REGISTERS.put("r7", 7);

        // Initialize the argument counts for each mnemonic
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
     * Assembles an instruction from its string representation into an integer representing the machine code.
     *
     * @param instruction The assembly instruction as a string.
     * @return An integer representing the machine code for the instruction.
     */
    public int parseInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        String mnemonic = parts[0];
        int opcode = OPCODES.get(mnemonic);
        int argCount = ARG_COUNTS.get(mnemonic);

        int machineCode = opcode << 24;

        // Handle different argument counts for instructions
        if (argCount == 3) {
            int r1 = REGISTERS.get(parts[1]);
            int r2 = REGISTERS.get(parts[2]);
            int r3 = REGISTERS.get(parts[3]);

            /*
                TODO: Check if r3 starts with 0x to specify PC and
                      distinguish conditional JMP instructions
            */
            //if()

            machineCode |= (r1 << 16);
            machineCode |= (r2 << 8);
            machineCode |= r3;
        } else if (argCount == 2) {
            int r1 = REGISTERS.get(parts[1]);
            if (parts[2].startsWith("#")) {
                int immediateValue = Integer.parseInt(parts[2].substring(1));
                machineCode |= (r1 << 16);
                machineCode |= immediateValue;
            } else if(parts[2].startsWith("0x")){
                int immediateValue = Integer.parseInt(parts[2].substring(2));
                machineCode |= (r1 << 16);
                machineCode |= immediateValue;
            } else {
                int addr = Integer.parseInt(parts[2]);
                machineCode |= (r1 << 16);
                machineCode |= addr;
            }
        } else if (argCount == 1) {
            if (mnemonic.equals("jmp")) {
                int addr = Integer.parseInt(parts[1]);
                machineCode |= addr;
            } else if (mnemonic.equals("jmpr")) {
                int reg = REGISTERS.get(parts[1]);
                machineCode |= (reg << 16);
            }
        }

        return machineCode;
    }
}

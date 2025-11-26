package Interpreter;

public enum Instructions {
    MOV(0x01, 2),
    ADD(0x02, 3),
    SUB(0x03, 3),
    MUL(0x04, 3),
    DIV(0x05, 3),
    AND(0x06, 3),
    OR(0x07, 3),
    STR(0x08, 2),
    LD(0x09, 2),
    JMP(0x0A, 1),
    JMPE(0x0B, 3),
    JMPN(0x0C, 3),
    JMPG(0x0D, 3),
    JMPL(0x0E, 3),
    JMPR(0x0F, 1),
    NOP(0xFF, 0);

    private int opcode;
    private int argCount;

    Instructions(int opcode, int argCount) {
        this.opcode = opcode;
        this.argCount = argCount;
    }

    public int getOpcode() { return opcode; }
    public int getArgCount() { return argCount; }

    public static Instructions fromString(String s) {
        return Instructions.valueOf(s.toUpperCase());
    }
}

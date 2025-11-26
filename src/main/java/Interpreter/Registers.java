package Interpreter;

public enum Registers {
    R0(0), R1(1), R2(2), R3(3), R4(4), R5(5), R6(6), R7(7);

    private final int code;

    Registers(int code) {
        this.code = code;
    }

    public int getCode() { return code; }

    public static Registers fromString(String s) {
        return Registers.valueOf(s.toUpperCase());
    }
}


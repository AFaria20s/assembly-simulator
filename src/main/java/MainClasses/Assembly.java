package MainClasses;

import GUI.settingsComponents.ProgramExecutionPanel;
import GUI.windowComponents.FlagsPanel;
import GUI.windowComponents.MemoryPanel;
import GUI.windowComponents.RegistersPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Assembly {
    private int[] register = new int[8];
    private int[] memory = new int[256];

    private int pc = 0;
    private int[] program;
    private int delay = 500;

    private RegistersPanel registersPanel;
    private MemoryPanel memoryPanel;
    private Timer timer;
    private FlagsPanel flagsPanel;

    public Assembly(int programSize, RegistersPanel registersPanel, MemoryPanel memoryPanel, FlagsPanel flagsPanel) {
        this.program = new int[programSize];
        this.registersPanel = registersPanel;
        this.memoryPanel = memoryPanel;
        this.flagsPanel = flagsPanel;
    }

    public void loadProgram(String[] rawProgram) {
        int length = Math.min(program.length, rawProgram.length);
        Assembler assembler = new Assembler();
        for (int i = 0; i < length; i++) {
            int instruction = assembler.parseInstruction(rawProgram[i]);
            program[i] = instruction;
        }
    }

    public void execute() {
        flagsPanel.resetFlags();
        final int maxIterations = 1000;

        ActionListener taskPerIteration = new ActionListener() {
            private int iteration = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (pc < program.length && iteration < maxIterations) {
                    int instruction = program[pc];

                    int opcode = (instruction >> 24) & 0xFF;
                    int r1 = (instruction >> 16) & 0xFF;
                    int r2 = (instruction >> 8) & 0xFF;
                    int addr = instruction & 0xFF;

                    boolean carry = false;
                    boolean overflow = false;
                    boolean negative = false;

                    switch (opcode) {
                        case 0x01: // MOV r1, imm
                            register[r1] = addr;
                            break;
                        case 0x02: // ADD r1, r2, r3
                            long result = (long) register[r2] + (long) register[addr];
                            carry = (result > Integer.MAX_VALUE);
                            overflow = (
                                    (register[r2] > 0 && register[addr] > 0 && result < 0) ||
                                    (register[r2] < 0 && register[addr] < 0 && result > 0));
                            register[r1] = (int) result;
                            negative = register[r1] < 0;
                            flagsPanel.updateFlags(register[r1], carry, overflow, negative);
                            break;
                        case 0x03: // SUB r1, r2, r3
                            result = (long) register[r2] - (long) register[addr];
                            carry = (register[r2] < register[addr]);
                            overflow = (
                                    (register[r2] > 0 && register[addr] < 0 && result < 0) ||
                                    (register[r2] < 0 && register[addr] > 0 && result > 0));
                            register[r1] = (int) result;
                            negative = register[r1] < 0;
                            flagsPanel.updateFlags(register[r1], carry, overflow, negative);
                            break;
                        case 0x04: // MUL r1, r2, r3
                            result = (long) register[r2] * (long) register[addr];
                            carry = (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE);
                            overflow = (register[r2] != 0 && result / register[r2] != register[addr]);
                            register[r1] = (int) result;
                            negative = register[r1] < 0;
                            flagsPanel.updateFlags(register[r1], carry, overflow, negative);
                            break;
                        case 0x05: // DIV r1, r2, r3
                            if (register[addr] != 0) {
                                result = register[r2] / register[addr];
                                overflow = (register[r2] == Integer.MIN_VALUE && register[addr] == -1);
                                register[r1] = (int) result;
                                negative = register[r1] < 0;
                                flagsPanel.updateFlags(register[r1], false, overflow, negative);
                            }
                            break;
                        case 0x06: // AND r1, r2, r3
                            register[r1] = register[r2] & register[addr];
                            flagsPanel.updateFlags(register[r1], false, false, false);
                            break;
                        case 0x07: // OR r1, r2, r3
                            register[r1] = register[r2] | register[addr];
                            flagsPanel.updateFlags(register[r1], false, false, false);
                            break;
                        case 0x08: // STORE r1, addr
                            memory[addr] = register[r1];
                            flagsPanel.updateFlags(register[r1], false, false, false);
                            break;
                        case 0x09: // LOAD r1, addr
                            register[r1] = memory[addr];
                            break;
                        case 0x0A: // JMP addr
                            pc = addr - 1;
                            break;
                        case 0x0B: // JMPE r1, r2, addr
                            if (register[r1] == register[r2]) {
                                pc = addr - 1;
                            }
                            break;
                        case 0x0C: // JMPN r1, r2, addr
                            if (register[r1] != register[r2]) {
                                pc = addr - 1;
                            }
                            break;
                        case 0x0D: // JMPG r1, r2, addr
                            if (register[r1] > register[r2]) {
                                pc = addr - 1;
                            }
                            break;
                        case 0x0E: // JMPL r1, r2, addr
                            if (register[r1] < register[r2]) {
                                pc = addr - 1;
                            }
                            break;
                        case 0x0F: // JMPR r1
                            int offset = register[r1];
                            pc += offset;
                            if (pc < 0 || pc >= program.length) {
                                System.out.println("PC out of bounds!");
                                pc = program.length; // Stop execution
                            }
                            break;
                        case 0xFF: // NO OP
                            break;
                        default:
                            System.out.println("Invalid instruction set!");
                            break;
                    }

                    SwingUtilities.invokeLater(() -> {
                        registersPanel.getRegistersTable().updateRegisterValues(register);
                        memoryPanel.updateMemoryValues(memory);
                    });

                    pc++;
                    iteration++;
                } else {
                    timer.stop();
                    flagsPanel.resetFlags();
                    if (iteration >= maxIterations) {
                        System.out.println("Execution halted: Maximum iterations reached.");
                    }
                }
            }
        };

        timer = new Timer(delay, taskPerIteration);
        timer.start();
    }

    public void reset() {
        Arrays.fill(memory, 0);
        Arrays.fill(register, 0);
        pc = 0;
        registersPanel.getRegistersTable().updateRegisterValues(register);
        memoryPanel.updateMemoryValues(memory);
        flagsPanel.resetFlags();
    }

    public Timer getTimer() {
        return this.timer;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public void updateDelay(int newDelay) {
        this.delay = newDelay;
        if (timer != null) {
            timer.setDelay(newDelay);
        }
    }
}

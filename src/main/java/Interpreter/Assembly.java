package Interpreter;

import GUI.windowComponents.FlagsPanel;
import GUI.windowComponents.MemoryPanel;
import GUI.windowComponents.RegistersPanel;

import javax.swing.*;
import java.awt.*;
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

    /**
     * Carrega o programa em assembly e converte para código máquina usando Assembler.
     */
    public void loadProgram(String[] rawProgram) {
        int length = Math.min(program.length, rawProgram.length);
        Assembler assembler = new Assembler();
        for (int i = 0; i < length; i++) {
            program[i] = assembler.parseInstruction(rawProgram[i]);
        }
    }

    /**
     * Executa o programa com delay controlado por Timer, atualizando GUI.
     */
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
                    int r3OrImm = instruction & 0xFFFF; // 16 bits para imediato ou r3

                    boolean carry = false;
                    boolean overflow = false;
                    boolean negative = false;

                    switch (opcode) {
                        case 0x01: // MOV r1, imm16
                            register[r1] = r3OrImm;
                            break;

                        case 0x02: // ADD r1, r2, r3/imm
                            long resultAdd = (long) register[r2] + (long) r3OrImmValue(r3OrImm);
                            carry = (resultAdd > Integer.MAX_VALUE);
                            overflow = ((register[r2] > 0 && r3OrImmValue(r3OrImm) > 0 && resultAdd < 0)
                                    || (register[r2] < 0 && r3OrImmValue(r3OrImm) < 0 && resultAdd > 0));
                            register[r1] = (int) resultAdd;
                            negative = register[r1] < 0;
                            flagsPanel.updateFlags(register[r1], carry, overflow, negative);
                            break;

                        case 0x03: // SUB r1, r2, r3/imm
                            long resultSub = (long) register[r2] - (long) r3OrImmValue(r3OrImm);
                            carry = (register[r2] < r3OrImmValue(r3OrImm));
                            overflow = ((register[r2] > 0 && r3OrImmValue(r3OrImm) < 0 && resultSub < 0)
                                    || (register[r2] < 0 && r3OrImmValue(r3OrImm) > 0 && resultSub > 0));
                            register[r1] = (int) resultSub;
                            negative = register[r1] < 0;
                            flagsPanel.updateFlags(register[r1], carry, overflow, negative);
                            break;

                        case 0x04: // MUL r1, r2, r3/imm
                            long resultMul = (long) register[r2] * (long) r3OrImmValue(r3OrImm);
                            carry = (resultMul > Integer.MAX_VALUE || resultMul < Integer.MIN_VALUE);
                            overflow = (register[r2] != 0 && resultMul / register[r2] != r3OrImmValue(r3OrImm));
                            register[r1] = (int) resultMul;
                            negative = register[r1] < 0;
                            flagsPanel.updateFlags(register[r1], carry, overflow, negative);
                            break;

                        case 0x05: // DIV r1, r2, r3/imm
                            int divisor = r3OrImmValue(r3OrImm);
                            if (divisor != 0) {
                                overflow = (register[r2] == Integer.MIN_VALUE && divisor == -1);
                                register[r1] = register[r2] / divisor;
                                negative = register[r1] < 0;
                                flagsPanel.updateFlags(register[r1], false, overflow, negative);
                            }
                            break;

                        case 0x06: // AND r1, r2, r3/imm
                            register[r1] = register[r2] & r3OrImmValue(r3OrImm);
                            flagsPanel.updateFlags(register[r1], false, false, false);
                            break;

                        case 0x07: // OR r1, r2, r3/imm
                            register[r1] = register[r2] | r3OrImmValue(r3OrImm);
                            flagsPanel.updateFlags(register[r1], false, false, false);
                            break;

                        case 0x08: // STORE r1, addr
                            memory[r3OrImm] = register[r1];
                            flagsPanel.updateFlags(register[r1], false, false, false);
                            break;

                        case 0x09: // LOAD r1, addr
                            register[r1] = memory[r3OrImm];
                            break;

                        case 0x0A: // JMP addr
                            pc = r3OrImm - 1;
                            break;

                        case 0x0B: // JMPE r1, r2, addr
                            if (register[r1] == register[r2]) pc = r3OrImm - 1;
                            break;

                        case 0x0C: // JMPN r1, r2, addr
                            if (register[r1] != register[r2]) pc = r3OrImm - 1;
                            break;

                        case 0x0D: // JMPG r1, r2, addr
                            if (register[r1] > register[r2]) pc = r3OrImm - 1;
                            break;

                        case 0x0E: // JMPL r1, r2, addr
                            if (register[r1] < register[r2]) pc = r3OrImm - 1;
                            break;

                        case 0x0F: // JMPR r1
                            pc += register[r1];
                            if (pc < 0 || pc >= program.length) pc = program.length; // stop
                            break;

                        case 0xFF: // NOP
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
                    Toolkit.getDefaultToolkit().beep();
                    flagsPanel.resetFlags();
                    if (iteration >= maxIterations) {
                        System.out.println("Execution halted: Maximum iterations reached.");
                    }
                }
            }

            /**
             * Retorna valor do 3º argumento: se for registo, devolve valor do registo; se for imediato, devolve valor direto
             */
            private int r3OrImmValue(int r3OrImm) {
                if (r3OrImm < 8) return register[r3OrImm]; // é registo
                return r3OrImm; // imediato
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
        if (timer != null) timer.setDelay(newDelay);
    }
}

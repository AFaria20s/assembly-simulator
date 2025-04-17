# SALS

## Simple Assembly Language Simulator

SALS is a basic assembly language simulator written in Java, totally custom-made. It simulates the behavior of a simple processor with features including arithmetic, logical, and memory operations.

## Features

- **Arithmetic Operations**: Addition, subtraction, multiplication, and division.
- **Logical Operations**: Bitwise AND and OR.
- **Memory Operations**: Loading from and storing to memory.
- **Control Flow**: Jump instructions to alter the sequence of execution.
- **Graphical User Interface (GUI)**: Provides a user-friendly interface for interacting with the simulator.
- **Conditional Jumps**: Conditional jumps based on equality, inequality, and relational operators.

## Instructions

- **MOV r1, imm** (0x01): Move an immediate value to register `r1`.
- **ADD r1, r2, addr** (0x02): Add the value of register `r2` to the value from memory at address `addr` and store the result in `r1`.
- **SUB r1, r2, addr** (0x03): Subtract the value from memory at address `addr` from the value of register `r2` and store the result in `r1`.
- **MUL r1, r2, addr** (0x04): Multiply the value of register `r2` by the value from memory at address `addr` and store the result in `r1`.
- **DIV r1, r2, addr** (0x05): Divide the value of register `r2` by the value from memory at address `addr` and store the result in `r1`.
- **AND r1, r2, addr** (0x06): Perform a bitwise AND on the values of registers `r2` and `addr` and store the result in `r1`.
- **OR r1, r2, addr** (0x07): Perform a bitwise OR on the values of registers `r2` and `addr` and store the result in `r1`.
- **STR r1, addr** (0x08): Store the value of register `r1` into memory at the specified address `addr`.
- **LD r1, addr** (0x09): Load the value from memory at the specified address `addr` into register `r1`.
- **JMP addr** (0x0A): Jump to the specified address `addr` in the program.
- **JMPE r1, r2, addr** (0x0B): Conditional jump to address `addr` if the value of register `r1` equals the value of register `r2`.
- **JMPN r1, r2, addr** (0x0C): Conditional jump to address `addr` if the value of register `r1` does not equal the value of register `r2`.
- **JMPG r1, r2, addr** (0x0D): Conditional jump to address `addr` if the value of register `r1` is greater than the value of register `r2`.
- **JMPL r1, r2, addr** (0x0E): Conditional jump to address `addr` if the value of register `r1` is less than the value of register `r2`.
- **JMPR r1** (0x0F): Relative jump to address `current + r1`, basically add a number to jump as many relative to the current address.

## Usage

1. **Download the Executable:**
   - Download the `.exe` file from the [releases page](https://github.com/AfonsoFaria20s/assembly-simulator/releases).
   - Make sure that you have latest version of JRE and JDK-22 installed.

2. **Create a Program File:**
   - Create a file (for example) `myProgram.txt` with your instructions, each on a new line. For example:
     ```
     MOV R1, 10
     MOV R2, 5
     ADD R1, R2, 0
     STORE R1, 0
     LOAD R1, 0
     JMP 0
     ```

3. **Run the Simulator:**
   - Double-click the `.exe` file to start the simulator.

## Notes

- The simulator uses a busy-wait loop to simulate delays; you can adjust or remove this as needed.
- Feel free to modify and extend the simulator according to your needs. Happy coding!


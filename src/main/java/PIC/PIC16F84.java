package PIC;

import UI.MyFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PIC16F84 {
    private static int[] Programstore = new int[1024];
    private static int[] RAM = new int[256];
    private static int[] Stack = new int[8];
    private static int StackIndex = 0;
    private static int Programcounter = 0;
    private static int WReg = 0;
    private static volatile boolean is_paused = true;

    private static final Logger log = LogManager.getLogger(PIC16F84.class);

    public PIC16F84() {};

    public static int getWReg() {
        return WReg;
    }

    public static void toggleIsPaused() {
        is_paused = !is_paused;
        log.info("pausiert? " + is_paused);
    }

    public static void writeProgramstore(int address, int value) {
        Programstore[address] = value;
    }

    public static int getProgramstore(int address) {
        return Programstore[address];
    }

    public static void writeRAM(int address, int value) {
        RAM[address] = value & 255;
    }

    public static int getRAM(int address) {
        return RAM[address];
    }

    public static void decode(int code) {
        if ((code & 16128) == 1792) {
            int value = code & 127;
            int destination = ((code & 128) >> 7);
            ADDWF(value, destination);

        } else if ((code & 16128) == 1280) {
            int value = code & 127;
            int destination = ((code & 128) >> 7);
            ANDWF(value, destination);

        } else if ((code & 16256) == 384) {
            int value = code & 127;
            CLRF(value);

        } else if ((code & 16256) == 256) {
            int value_x = code & 127;
            CLRW(value_x);

        } else if ((code & 16128) == 2304) {
            int value = code & 127;
            int destination = ((code & 128) >> 7);
            COMF(value, destination);

        } else if ((code & 16128) == 768) {
            int value = code & 127;
            int destination = ((code & 128) >> 7);
            DECF(value, destination);

        } else if ((code & 16128) == 2816) {
            int value = code & 127;
            int destination = ((code & 128) >> 7);
            DECFSZ(value, destination);

        } else if ((code & 16128) == 2560) {
            int value = code & 127;
            int destination = ((code & 128) >> 7);
            INCF(value, destination);

        } else if ((code & 16128) == 3840) {
            int value = code & 127;
            int destination = ((code & 128) >> 7);
            INCFSZ(value, destination);

        } else if ((code & 16128) == 1024) {
            int value = code & 127;
            int destination = ((code & 128) >> 7);
            IORWF(value, destination);

        } else if ((code & 16128) == 2048) {
            int value = code & 127;
            int destination = ((code & 128) >> 7);
            MOVF(value, destination);

        } else if ((code & 16256) == 128) {
            int value = code & 127;
            MOVWF(value);

        } else if ((code & 16287) == 0) {
            NOP();

        } else if ((code & 16128) == 3328) {
            int value = code & 127;
            int destination = ((code & 128) >> 7);
            RLF(value, destination);

        } else if ((code & 16128) == 3072) {
            int value = code & 127;
            int destination = ((code & 128) >> 7);
            RRF(value, destination);

        } else if ((code & 16128) == 512) {
            int value = code & 127;
            int destination = ((code & 128) >> 7);
            SUBWF(value, destination);

        } else if ((code & 16128) == 3584) {
            int value = code & 127;
            int destination = ((code & 128) >> 7);
            SWAPF(value, destination);

        } else if ((code & 16128) == 1536) {
            int value = code & 127;
            int destination = ((code & 128) >> 7);
            XORWF(value, destination);

        } else if ((code & 15360) == 4096) {
            int value = code & 127;
            int destination = ((code & 896) >> 7);
            BCF(value, destination);

        } else if ((code & 15360) == 5120) {
            int value = code & 127;
            int destination = ((code & 896) >> 7);
            BSF(value, destination);

        } else if ((code & 15360) == 6144) {
            int value = code & 127;
            int destination = ((code & 896) >> 7);
            BTFSC(value, destination);

        } else if ((code & 15360) == 7168) {
            int value = code & 127;
            int destination = ((code & 896) >> 7);
            BTFSS(value, destination);

        } else if ((code & 15872) == 15872) {
            int value = code & 255;
            ADDLW(value);

        } else if ((code & 16128) == 14592) {
            int literal = code & 255;
            ANDLW(literal);

        } else if ((code & 14336) == 8192) {
            int literal = code & 2047;
            CALL(literal);

        } else if ((code & 16383) == 100) {
            CLRWDT();

        } else if ((code & 14336) == 10240) {
            int literal = code & 2047;
            GOTO(literal);

        } else if ((code & 16128) == 14336) {
            int literal = code & 255;
            IORLW(literal);

        } else if ((code & 15360) == 12288) {
            int literal = code & 255;
            MOVLW(literal);

        } else if ((code & 16383) == 9) {
            RETFIE();

        } else if ((code & 15360) == 13312) {
            int literal = code & 255;
            RETLW(literal);

        } else if ((code & 16383) == 8) {
            RETURN();

        } else if ((code & 16383) == 99) {
            SLEEP();

        } else if ((code & 15872) == 15360) {
            int literal = code & 255;
            SUBLW(literal);

        } else if ((code & 16128) == 14848) {
            int literal = code & 255;
            XORLW(literal);
        }
    }

    public static void readProgramstore() {
        for (int i = 0; i < 1024; i++) {
            log.info(getProgramstore(i));
        }
    }

    public static void reset() {
        // set TRIS to input to avoid damage
        writeRAM(133, 255);
        writeRAM(134, 255);

        // set INCONT
        writeRAM(11, 0);
    }

    public static void pushStack(int addresse) {
        Stack[StackIndex % 8] = addresse;
        StackIndex += 1;
    }

    public static int popStack() {
        StackIndex -= 1;
        return Stack[StackIndex];
    }

    public static int[] getStack() {
        return Stack;
    }

    public static void setZeroFlag() {
        RAM[3] = RAM[3] | 4;
        RAM[131] = RAM[131] | 4;
    }

    public static void clearZeroFlag() {
        RAM[3] = RAM[3] & 251;
        RAM[131] = RAM[131] & 251;
    }

    public static int getZeroFlag() {
        return (RAM[3] & 4) >> 2;
    }

    public static int getCarryFlag() {
        return (RAM[3] & 1);
    }

    public static void setCarryFlag() {
        RAM[3] = RAM[3] | 1;
        RAM[131] = RAM[131] | 1;
    }

    public static void clearCarryFlag() {
        RAM[3] = RAM[3] & 254;
        RAM[131] = RAM[131] & 254;
    }

    public static int getDigitcarryFlag() {
        return (RAM[3] & 2) >> 1;
    }

    public static void setDigitcarryFlag() {
        RAM[3] = RAM[3] | 2;
        RAM[131] = RAM[131] | 2;
    }

    public static void clearDigitcarryFlag() {
        RAM[3] = RAM[3] & 253;
        RAM[131] = RAM[131] & 253;
    }

    public static int getActualProgramCounter() {
        return Programcounter;
    }

    public static int getProgramCounter() {
        return RAM[2];
    }

    public static void incrementProgramCounter() {
        if (Programcounter == 1023) {
            Programcounter = 0;
        } else {
            Programcounter += 1;

        }
        RAM[2] = Programcounter & 255;
        RAM[130] = Programcounter & 255;
    }

    public static void setProgramCounter(int address) {
        Programcounter = address & 1023;
        RAM[2] = Programcounter & 255;
        RAM[130] = Programcounter & 255;
    }

    public static void resetProgramCounter() {
        RAM[2] = 0;
        RAM[130] = 0;
        Programcounter = 0;
    }

    public static void ADDWF(int value, int destination) {
        log.info("ADDWF");
    }

    public static void ANDWF(int value, int destination) {
        log.info("ANDWF");
    }

    public static void CLRF(int value) {
        log.info("CLRF");
    }

    public static void CLRW(int value) {
        log.info("CLRW");
    }

    public static void COMF(int value, int destination) {
        log.info("COMF");
    }

    public static void DECF(int value, int destination) {
        log.info("DECF");
    }

    public static void DECFSZ(int value, int destination) {
        log.info("DECFSZ");
    }

    public static void INCF(int value, int destination) {
        log.info("INCF");
    }

    public static void INCFSZ(int value, int destination) {
        log.info("INCFSZ");
    }

    public static void IORWF(int value, int destination) {
        log.info("IORWF");
    }

    public static void MOVF(int value, int destination) {
        log.info("MOVF");
    }

    public static void MOVWF(int value) {

        log.info("MOVWF, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void NOP() {
        log.info("NOP, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void RLF(int value, int destination) {
        log.info("RLF");
    }

    public static void RRF(int value, int destination) {
        log.info("RRF");
    }

    public static void SUBWF(int value, int destination) {
        log.info("SUBWF");
    }

    public static void SWAPF(int value, int destination) {
        log.info("SWAPF");
    }

    public static void XORWF(int value, int destination) {
        log.info("XORWF");
    }

    public static void BCF(int value, int destination) {
        log.info("BCF");
    }

    public static void BSF(int value, int destination) {
        log.info("BSF");
    }

    public static void BTFSC(int value, int destination) {
        log.info("BTFSC");
    }

    public static void BTFSS(int value, int destination) {
        log.info("BTFSS");
    }

    public static void ADDLW(int value) {
        int new_value = value + WReg;
        if (value == 0) setZeroFlag();
        else clearZeroFlag();
        if (new_value > 255) setCarryFlag();
        else clearCarryFlag();
        if ((value & 15) + (WReg & 15) > 15) setDigitcarryFlag();
        else clearDigitcarryFlag();
        WReg = new_value;
        log.info("ADDLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void ANDLW(int literal) {
        int value = WReg & literal;
        if (value == 0) setZeroFlag();
        else clearZeroFlag();
        WReg = value;
        log.info("ANDLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void CALL(int literal) {
        pushStack(Programcounter);
        PIC16F84.setProgramCounter(literal & 1023);
        log.info("CALL, return-address=" + (Programcounter) + ", destination-address=" + (literal & 1023));
    }

    public static void CLRWDT() {
        log.info("CLRWDT");
    }

    public static void GOTO(int literal) {
        PIC16F84.setProgramCounter(literal);
        log.info("GOTO, destination-address=" + (literal & 1023));
    }

    public static void IORLW(int literal) {
        int value = WReg | literal;
        if (value == 0) setZeroFlag();
        else clearZeroFlag();
        WReg = value;
        log.info("IORLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void MOVLW(int literal) {
        WReg = literal;
        log.info("MOVLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void RETFIE() {
        log.info("RETFIE");
    }

    public static void RETLW(int literal) {
        WReg = literal;
        Programcounter = popStack();
        log.info("RETLW, return-address=" + Programcounter + ", W=" + Integer.toHexString(WReg) + "h");
    }

    public static void RETURN() {
        Programcounter = popStack();
        log.info("RETURN");
    }

    public static void SLEEP() {
        log.info("SLEEP");
    }

    public static void SUBLW(int literal) {
        int value = literal - WReg;
        if (value == 0) setZeroFlag();
        else clearZeroFlag();
        if (literal >= WReg) setCarryFlag();
        else clearCarryFlag();
        if ((literal & 15) >= (WReg & 15)) setDigitcarryFlag();
        else clearDigitcarryFlag();
        WReg = value;
        log.info("SUBLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void XORLW(int literal) {
        int value = literal ^ WReg;
        if (value == 0) setZeroFlag();
        else clearZeroFlag();
        WReg = value;
        log.info("XORLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void runProgram() {
        while (!is_paused) {
            int command = getProgramstore(Programcounter);
            incrementProgramCounter();
            decode(command);
            MyFrame.updateFieldWEST();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void stepProgram() {
        int command = getProgramstore(Programcounter);
        incrementProgramCounter();
        decode(command);
        MyFrame.updateFieldWEST();
    }

}
package PIC;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PIC16F84 {
    private static int[] Programstore = new int[1024];
    private int[] RAM = new int[256];
    private int[] Stack = new int[8];
    private int StackIndex = 0;
    private int Programcounter = 0;

    private static final Logger log = LogManager.getLogger(PIC16F84.class);

    public PIC16F84() {};

    public static void writeProgramstore(int address, int value) {
        Programstore[address] = value;
    }

    public int getProgramstore(int address) {
        return this.Programstore[address];
    }

    public void writeRAM(int address, int value) {
        this.RAM[address] = value;
    }

    public int getRAM(int address) {
        return this.RAM[address];
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
               RETILW(literal);

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

    public void readProgramstore() {
        for (int i = 0; i < 1024; i++) {
            log.info(this.getProgramstore(i));
        }
    }

    public void reset() {
        // set TRIS to input to avoid damage
        writeRAM(133, 255);
        writeRAM(134, 255);

        // set INCONT
        writeRAM(11, 0);
    }

    public void setStack(int addresse) {
        this.Stack[this.StackIndex % 8] = addresse;
        this.StackIndex += 1;
    }

    public int getStack() {
        this.StackIndex -= 1;
        return this.Stack[this.StackIndex];
    }

    public void setZeroFlag() {
        this.RAM[3] = this.RAM[3] | 4;
        this.RAM[131] = this.RAM[131] | 4;
    }

    public void clearZeroFlag() {
        this.RAM[3] = this.RAM[3] & 251;
        this.RAM[131] = this.RAM[131] & 251;
    }

    public int getZeroFlag() {
        return (this.RAM[3] & 4) >> 2;
    }


    public int getCarryFlag() {
        return (this.RAM[3] & 1);
    }

    public void setCarryFlag() {
        this.RAM[3] = this.RAM[3] | 1;
        this.RAM[131] = this.RAM[131] | 1;
    }

    public void clearCarryFlag() {
        this.RAM[3] = this.RAM[3] & 254;
        this.RAM[131] = this.RAM[131] & 254;
    }

    public int getDigitcarryFlag() {
        return (this.RAM[3] & 2) >> 1;
    }

    public void setDigitcarryFlag() {
        this.RAM[3] = this.RAM[3] | 2;
        this.RAM[131] = this.RAM[131] | 2;
    }

    public void clearDigitcarryFlag() {
        this.RAM[3] = this.RAM[3] & 253;
        this.RAM[131] = this.RAM[131] & 253;
    }

    public int getProgramCounter() {
        return this.RAM[2];
    }

    public void incrementProgramCounter() {
        if (this.Programcounter == 1023) {
            this.Programcounter = 0;
        } else {
            this.Programcounter += 1;
        }
        if (getProgramCounter() < 255) {
            this.RAM[2] += 1;
            this.RAM[130] += 1;
        }
    }

    public void resetProgramCounter() {
        this.RAM[2] = 0;
        this.RAM[130] = 0;
        this.Programcounter = 0;
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
        log.info("MOVWF");
    }

    public static void NOP() {
        log.info("NOP");
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
        log.info("ADDLW");
    }

    public static void ANDLW(int literal) {
        log.info("ANDLW");
    }

    public static void CALL(int literal) {
        log.info("CALL");
    }

    public static void CLRWDT() {
        log.info("CLRWDT");
    }

    public static void GOTO(int literal) {
        log.info("GOTO");
    }

    public static void IORLW(int literal) {
        log.info("IORLW");
    }

    public static void MOVLW(int literal) {
        log.info("MOVLW");
    }

    public static void RETFIE() {
        log.info("RETFIE");
    }

    public static void RETILW(int literal) {
        log.info("RETILW");
    }

    public static void RETURN() {
        log.info("RETURN");
    }

    public static void SLEEP() {
        log.info("SLEEP");
    }

    public static void SUBLW(int literal) {
        log.info("SUBLW");
    }

    public static void XORLW(int literal) {
        log.info("XORLW");
    }

}

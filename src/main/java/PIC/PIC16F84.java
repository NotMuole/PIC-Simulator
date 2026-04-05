package PIC;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PIC16F84 {
    private static int[] Programmstore = new int[1024];
    private int[] RAM = new int[256];
    private int[] Stack = new int[8];
    private int StackIndex = 0;

    private static final Logger log = LogManager.getLogger(PIC16F84.class);

    public PIC16F84() {};

    public static void writeProgrammstore(int address, int value) {
        Programmstore[address] = value;
    }

    public int getProgrammstore(int address) {
        return this.Programmstore[address];
    }

    public void writeRAM(int address, int value) {
        this.RAM[address] = value;
    }

    public int getRAM(int address) {
        return this.RAM[address];
    }

    public static void decode(int code) {
           if ((code & 16128) == 1792) {
               log.info("ADDWF");
               int value = code & 127;
               int destination = ((code & 128) >> 7);

           } else if ((code & 16128) == 1280) {
               log.info("ANDWF");
               int value = code & 127;
               int destination = ((code & 128) >> 7);

           } else if ((code & 16256) == 384) {
               log.info("CLRF");
               int value = code & 127;

           } else if ((code & 16256) == 256) {
               log.info("CLRW");
               int value_x = code & 127;

           } else if ((code & 16128) == 2304) {
               log.info("COMF");
               int value = code & 127;
               int destination = ((code & 128) >> 7);

           } else if ((code & 16128) == 768) {
               log.info("DECF");
               int value = code & 127;
               int destination = ((code & 128) >> 7);

           } else if ((code & 16128) == 2816) {
               log.info("DECFSZ");
               int value = code & 127;
               int destination = ((code & 128) >> 7);

           } else if ((code & 16128) == 2560) {
               log.info("INCF");
               int value = code & 127;
               int destination = ((code & 128) >> 7);

           } else if ((code & 16128) == 3840) {
               log.info("INCFSZ");
               int value = code & 127;
               int destination = ((code & 128) >> 7);

           } else if ((code & 16128) == 1024) {
               log.info("IORWF");
               int value = code & 127;
               int destination = ((code & 128) >> 7);

           } else if ((code & 16128) == 2048) {
               log.info("MOVF");
               int value = code & 127;
               int destination = ((code & 128) >> 7);

           } else if ((code & 16256) == 128) {
               log.info("MOVWF");
               int value = code & 127;

           } else if ((code & 16287) == 0) {
                log.info("NOP");

           } else if ((code & 16128) == 3328) {
               log.info("RLF");
               int value = code & 127;
               int destination = ((code & 128) >> 7);

           } else if ((code & 16128) == 3072) {
               log.info("RRF");
               int value = code & 127;
               int destination = ((code & 128) >> 7);

           } else if ((code & 16128) == 512) {
               log.info("SUBWF");
               int value = code & 127;
               int destination = ((code & 128) >> 7);

           } else if ((code & 16128) == 3584) {
               log.info("SWAPF");
               int value = code & 127;
               int destination = ((code & 128) >> 7);

           } else if ((code & 16128) == 1536) {
               log.info("XORWF");
               int value = code & 127;
               int destination = ((code & 128) >> 7);

           } else if ((code & 15360) == 4096) {
               log.info("BCF");
               int value = code & 127;
               int destination = ((code & 896) >> 7);

           } else if ((code & 15360) == 5120) {
               log.info("BSF");
               int value = code & 127;
               int destination = ((code & 896) >> 7);

           } else if ((code & 15360) == 6144) {
               log.info("BTFSC");
               int value = code & 127;
               int destination = ((code & 896) >> 7);

           } else if ((code & 15360) == 7168) {
               log.info("BTFSS");
               int value = code & 127;
               int destination = ((code & 896) >> 7);

           } else if ((code & 15872) == 15872) {
               log.info("ADDLW");
               int value = code & 255;

           } else if ((code & 16128) == 14592) {
               log.info("ANDLW");
               int literal = code & 255;

           } else if ((code & 14336) == 8192) {
               log.info("CALL");
               int literal = code & 2047;

           } else if ((code & 16383) == 100) {
               log.info("CLRWDT");

           } else if ((code & 14336) == 10240) {
               log.info("GOTO");
               int literal = code & 2047;

           } else if ((code & 16128) == 14336) {
               log.info("IORLW");
               int literal = code & 255;

           } else if ((code & 15360) == 12288) {
               log.info("MOVLW");
               int literal = code & 255;

           } else if ((code & 16383) == 9) {
               log.info("RETFIE");

           } else if ((code & 15360) == 13312) {
               log.info("RETILW");
               int literal = code & 255;

           } else if ((code & 16383) == 8) {
               log.info("RETURN");

           } else if ((code & 16383) == 99) {
               log.info("SLEEP");

           } else if ((code & 15872) == 15360) {
               log.info("SUBLW");
               int literal = code & 255;

           } else if ((code & 16128) == 14848) {
               log.info("XORLW");
               int literal = code & 255;
           }
    }

    public void readProgrammstore() {
        for (int i = 0; i < 1024; i++) {
            log.info(this.getProgrammstore(i));
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
}

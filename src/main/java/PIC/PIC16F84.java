package PIC;

import UI.Checkbox;
import UI.MyFrame;
import file.MyFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;

public class PIC16F84 {
    static Map<Integer, Integer> mirrowAddress = Map.of(
        0, 4
    );

    private static int[] Programstore = new int[1024];
    private static int[] RAM = new int[256];
    private static int[] Stack = new int[8];
    private static int StackIndex = 0;
    private static int Programcounter = 0;
    private static int WReg = 0;
    private static volatile boolean is_paused = true;
    private static double clockRate = 4.0;
    private static double timePerCycleUs = 4 / clockRate;
    private static double timePassed = 0;
    private static double delay = 300 / clockRate;
    private static final Logger log = LogManager.getLogger(PIC16F84.class);

    public PIC16F84() {}

    public static void writeWReg(int value) {
        WReg = value & 255;
    }

    public static int getWReg() {
        return WReg;
    }

    public static void toggleIsPaused() {
        is_paused = !is_paused;
    }

    public static void setPaused(boolean bool) {
        is_paused = bool;
    }

    public static boolean getIsPaused() {
        return is_paused;
    }

    public static double getTimePassed() {
        return timePassed;
    }

    public static void setClockRate(double value) {
        clockRate = value;
        timePerCycleUs = 4 / clockRate;
        delay = 300 / clockRate;
    }
    public static double getClockRate() {
        return clockRate;
    }

    public static double getTimePerCycleUs() {
        return timePerCycleUs;
    }

    public static void writeProgramstore(int address, int value) {
        Programstore[address] = value;
    }

    public static int getProgramstore(int address) {
        return Programstore[address];
    }

    public static void writeRAM(int address, int value) {
        int actual_address = mirrowAddress.containsKey(address) ? mirrowAddress.get(address) : address;
        RAM[actual_address] = value & 255;
    }

    public static int getRAM(int address) {
        int actual_address = mirrowAddress.containsKey(address) ? mirrowAddress.get(address) : address;
        return RAM[actual_address];
    }

    public static void decode(int code) {
           if ((code & 16128) == 1792) {
               int file_address = code & 127;
               int destination = ((code & 128) >> 7);
               ADDWF(file_address, destination);

           } else if ((code & 16128) == 1280) {
               int file_address = code & 127;
               int destination = ((code & 128) >> 7);
               ANDWF(file_address, destination);

           } else if ((code & 16256) == 384) {
               int file_address = code & 127;
               CLRF(file_address);

           } else if ((code & 16256) == 256) {
               CLRW();

           } else if ((code & 16128) == 2304) {
               int file_address = code & 127;
               int destination = ((code & 128) >> 7);
               COMF(file_address, destination);

           } else if ((code & 16128) == 768) {
               int file_address = code & 127;
               int destination = ((code & 128) >> 7);
               DECF(file_address, destination);

           } else if ((code & 16128) == 2816) {
               int file_address = code & 127;
               int destination = ((code & 128) >> 7);
               DECFSZ(file_address, destination);

           } else if ((code & 16128) == 2560) {
               int file_address = code & 127;
               int destination = ((code & 128) >> 7);
               INCF(file_address, destination);

           } else if ((code & 16128) == 3840) {
               int file_address = code & 127;
               int destination = ((code & 128) >> 7);
               INCFSZ(file_address, destination);

           } else if ((code & 16128) == 1024) {
               int file_address = code & 127;
               int destination = ((code & 128) >> 7);
               IORWF(file_address, destination);

           } else if ((code & 16128) == 2048) {
               int file_address = code & 127;
               int destination = ((code & 128) >> 7);
               MOVF(file_address, destination);

           } else if ((code & 16256) == 128) {
               int file_address = code & 127;
               MOVWF(file_address);

           } else if ((code & 16287) == 0) {
                NOP();

           } else if ((code & 16128) == 3328) {
               int file_address = code & 127;
               int destination = ((code & 128) >> 7);
               RLF(file_address, destination);

           } else if ((code & 16128) == 3072) {
               int file_address = code & 127;
               int destination = ((code & 128) >> 7);
               RRF(file_address, destination);

           } else if ((code & 16128) == 512) {
               int file_address = code & 127;
               int destination = ((code & 128) >> 7);
               SUBWF(file_address, destination);

           } else if ((code & 16128) == 3584) {
               int file_address = code & 127;
               int destination = ((code & 128) >> 7);
               SWAPF(file_address, destination);

           } else if ((code & 16128) == 1536) {
               int file_address = code & 127;
               int destination = ((code & 128) >> 7);
               XORWF(file_address, destination);

           } else if ((code & 15360) == 4096) {
               int file_address = code & 127;
               int bit = ((code & 896) >> 7);
               BCF(file_address, bit);

           } else if ((code & 15360) == 5120) {
               int file_address = code & 127;
               int bit = ((code & 896) >> 7);
               BSF(file_address, bit);

           } else if ((code & 15360) == 6144) {
               int file_address = code & 127;
               int bit = ((code & 896) >> 7);
               BTFSC(file_address, bit);

           } else if ((code & 15360) == 7168) {
               int file_address = code & 127;
               int bit = ((code & 896) >> 7);
               BTFSS(file_address, bit);

           } else if ((code & 15872) == 15872) {
               int literal = code & 255;
               ADDLW(literal);

           } else if ((code & 16128) == 14592) {
               int literal = code & 255;
               ANDLW(literal);

           } else if ((code & 14336) == 8192) {
               int address = code & 2047;
               CALL(address);

           } else if ((code & 16383) == 100) {
               CLRWDT();

           } else if ((code & 14336) == 10240) {
               int address = code & 2047;
               GOTO(address);

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

    public static void ADDWF(int file_address, int destination) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int value = WReg + getRAM(file_address);
        if (value == 0) setZeroFlag(); else clearZeroFlag();
        if (value > 255) setCarryFlag(); else clearCarryFlag();
        if ((getRAM(file_address) & 15) + (WReg & 15) > 15) setDigitcarryFlag(); else clearDigitcarryFlag();
        if (destination == 0) {
            writeWReg(value);

        } else {
            writeRAM(file_address, value);
        }
        updateTime(1);
        //log.info("ADDWF, WReg: \" + Integer.toHexString(WReg) + \"h, C=\" + getCarryFlag() + \", DC=\" + getDigitcarryFlag() + \", Z=\" + getZeroFlag()");
    }

    public static void ANDWF(int file_address, int destination) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int value = WReg & getRAM(file_address);
        if (value == 0) setZeroFlag(); else clearZeroFlag();
        if (destination == 0) {
            writeWReg(value);

        } else {
            writeRAM(file_address, value);
        }
        updateTime(1);
        //log.info("ANDWF, WReg: \" + Integer.toHexString(WReg) + \"h, C=\" + getCarryFlag() + \", DC=\" + getDigitcarryFlag() + \", Z=\" + getZeroFlag()");
    }

    public static void CLRF(int file_address) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        writeRAM(file_address, 0);
        setZeroFlag();
        updateTime(1);
        //log.info("CLRF");
    }

    public static void CLRW() {
        writeWReg(0);
        setZeroFlag();
        updateTime(1);
        //log.info("CLRW");
    }

    public static void COMF(int file_address, int destination) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int value = getRAM(file_address) ^ 255;
        if (value == 0) setZeroFlag(); else clearZeroFlag();
        if (destination == 0) {
            writeWReg(value);
        } else {
            writeRAM(file_address, value);
        }
        updateTime(1);
        //log.info("COMF");
    }

    public static void DECF(int file_address, int destination) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int value = getRAM(file_address) - 1;
        if (value == 0) setZeroFlag(); else clearZeroFlag();
        if (destination == 0) {
            writeWReg(value);
        } else {
            writeRAM(file_address, value);
        }
        updateTime(1);
        //log.info("DECF");
    }

    public static void DECFSZ(int file_address, int destination) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int value = getRAM(file_address) - 1;
        if (destination == 0) {
            writeWReg(value);
        } else {
            writeRAM(file_address, value);
        }

        if (value == 0) { 
            NOP();
            incrementProgramCounter();
        }
        updateTime(1);
        //log.info("DECFSZ");
    }

    public static void INCF(int file_address, int destination) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int value = getRAM(file_address) + 1;
        if (value > 255) setZeroFlag(); else clearZeroFlag();
        if (destination == 0) {
            writeWReg(value);
        } else {
            writeRAM(file_address, value);
        }
        updateTime(1);
        //log.info("INCF");
    }

    public static void INCFSZ(int file_address, int destination) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int value = getRAM(file_address) + 1;
        if (destination == 0) {
            writeWReg(value);
        } else {
            writeRAM(file_address, value);
        }

        if (value > 255) {
            NOP();
            incrementProgramCounter();
        }
        updateTime(1);
        //log.info("INCFSZ");
    }

    public static void IORWF(int file_address, int destination) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int value = WReg | getRAM(file_address);
        if (destination == 0) {
            writeWReg(value);
        } else {
            writeRAM(file_address, value);
        }

        if (value == 0) setZeroFlag(); else clearZeroFlag();
        updateTime(1);
        //log.info("IORWF");
    }

    public static void MOVF(int file_address, int destination) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int value = getRAM(file_address);
        if (value == 0) setZeroFlag(); else clearZeroFlag();

        if (destination == 0) {
            writeWReg(value);
        } else {
            writeRAM(file_address, value);
        }
        updateTime(1);
        //log.info("MOVF");
    }

    public static void MOVWF(int file_address) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int value = getWReg();
        writeRAM(file_address, value);
        updateTime(1);
        //log.info("MOVWF, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void NOP() {
        updateTime(1);
        //log.info("NOP, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void RLF(int file_address, int destination) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        log.info("Wert: " + getRAM(file_address));
        int carry = (getRAM(file_address) & 128) >> 7;
        log.info("Carry: " + carry);
        int value = (getRAM(file_address) << 1) + getCarryFlag();
        log.info("Wert nach shift: " + value);
        if (carry == 1) setCarryFlag(); else clearCarryFlag();

        if (destination == 0) {
            writeWReg(value);
        } else {
            writeRAM(file_address, value);
        }
        updateTime(1);
        //log.info("RLF");
    }

    public static void RRF(int file_address, int destination) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int carry = (getRAM(file_address) & 1);
        int value = (getRAM(file_address) >> 1) + (getCarryFlag() << 7);
        if (carry == 1) setCarryFlag(); else clearCarryFlag();

        if (destination == 0) {
            writeWReg(value);
        } else {
            writeRAM(file_address, value);
        }
        updateTime(1);
        //log.info("RRF");
    }

    public static void SUBWF(int file_address, int destination) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int value = getRAM(file_address) - WReg;
        if (value == 0) setZeroFlag(); else clearZeroFlag();
        if (getRAM(file_address) >= WReg) setCarryFlag(); else clearCarryFlag();
        if ((getRAM(file_address) & 15) >= (WReg & 15)) setDigitcarryFlag(); else clearDigitcarryFlag();

        if (destination == 0) {
            writeWReg(value);
        } else {
            writeRAM(file_address, value);
        }
        updateTime(1);
        //log.info("SUBWF");
    }

    public static void SWAPF(int file_address, int destination) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int topNibbles = (getRAM(file_address) & 240) >> 4;
        int value = (getRAM(file_address) << 4) + topNibbles;

        if (destination == 0) {
            writeWReg(value);
        } else {
            writeRAM(file_address, value);
        }
        updateTime(1);
        //log.info("SWAPF");
    }

    public static void XORWF(int file_address, int destination) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int value = WReg ^ getRAM(file_address);
        if (value == 0) setZeroFlag(); else clearZeroFlag();

        if (destination == 0) {
            writeWReg(value);
        } else {
            writeRAM(file_address, value);
        }
        updateTime(1);
        //log.info("XORWF");
    }

    public static void BCF(int file_address, int bit) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int value = getRAM(file_address)&~(1 << bit);
        writeRAM(file_address, value);
        updateTime(1);
        //log.info("BCF");
    }

    public static void BSF(int file_address, int bit) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int value = getRAM(file_address)|(1 << bit);
        writeRAM(file_address, value);
        //log.info("BSF");
        updateTime(1);
    }

    public static void BTFSC(int file_address, int bit) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int value = (getRAM(file_address)&(1 << bit)) >> bit;
        if (value == 0) {
            NOP();
            incrementProgramCounter();
        }
        updateTime(1);
        //log.info("BTFSC");
    }

    public static void BTFSS(int file_address, int bit) {
        if (file_address == 0) {
            file_address = getRAM(0);
        }
        int value = (getRAM(file_address)&(1 << bit)) >> bit;
        if (value == 1) {
            NOP();
            incrementProgramCounter();
        }
        updateTime(1);
        //log.info("BTFSS");
    }

    public static void ADDLW(int literal) {
        int value = literal + WReg;
        if (value == 0) setZeroFlag(); else clearZeroFlag();
        if (value > 255) setCarryFlag(); else clearCarryFlag();
        if ((literal & 15) + (WReg & 15) > 15) setDigitcarryFlag(); else clearDigitcarryFlag();
        writeWReg(value);
        updateTime(1);
        //log.info("ADDLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void ANDLW(int literal) {
        int value = WReg & literal;
        if (value == 0) setZeroFlag(); else clearZeroFlag();
        writeWReg(value);
        updateTime(1);
        //log.info("ANDLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void CALL(int address) {
        pushStack(Programcounter);
        PIC16F84.setProgramCounter(address & 1023);
        updateTime(2);
        //log.info("CALL, return-address=" + (Programcounter) + ", destination-address=" + (address & 1023));
    }

    public static void CLRWDT() {
        updateTime(1);
        //log.info("TODO: CLRWDT");
    }

    public static void GOTO(int address) {
        PIC16F84.setProgramCounter(address);
        updateTime(2);
        //log.info("GOTO, destination-address=" + (address & 1023));
    }

    public static void IORLW(int literal) {
        int value = WReg | literal;
        if (value == 0 ) setZeroFlag(); else clearZeroFlag();
        writeWReg(value);
        updateTime(1);
        //log.info("IORLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void MOVLW(int literal) {
        writeWReg(literal);
        updateTime(1);
        //log.info("MOVLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void RETFIE() {
        updateTime(2);
        //log.info("TODO: RETFIE");
    }

    public static void RETLW(int literal) {
        writeWReg(literal);
        Programcounter = popStack();
        updateTime(2);
        //log.info("RETLW, return-address=" + Programcounter + ", W=" + Integer.toHexString(WReg) + "h");
    }

    public static void RETURN() {
        Programcounter = popStack();
        updateTime(2);
        //log.info("RETURN");
    }

    public static void SLEEP() {
        updateTime(1);
        //log.info("TODO: SLEEP");
    }

    public static void SUBLW(int literal) {
        int value = literal - WReg;
        if (value == 0) setZeroFlag(); else clearZeroFlag();
        if (literal >= WReg) setCarryFlag(); else clearCarryFlag();
        if ((literal & 15) >= (WReg & 15)) setDigitcarryFlag(); else clearDigitcarryFlag();
        writeWReg(value);
        updateTime(1);
        //log.info("SUBLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void XORLW(int literal) {
        int value = literal ^ WReg;
        if (value == 0) setZeroFlag(); else clearZeroFlag();
        writeWReg(value);
        updateTime(1);
        //log.info("XORLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void updateTime(int anzahl) {
        for (int i=0; i<anzahl; i++) {
            timePassed += timePerCycleUs;
        }
    }

    public static void runProgram() {
        while (!is_paused) {
            executeProgram();
            if (Checkbox.getBreakpoint(Programcounter)) {
                toggleIsPaused();
            }
            try {
                Thread.sleep((long) delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void stepProgram() {
        executeProgram();
    }

    private static void executeProgram() {
        int command = getProgramstore(Programcounter);
        incrementProgramCounter();
        decode(command);
        MyFrame.updateFieldWEST();
        MyFrame.updateListing();
        MyFrame.updateFieldEAST();
    }

    public static void resetProgram() {
        reset();
        is_paused = true;
        Programstore = new int[1024];
        RAM = new int[256];
        Stack = new int[8];
        StackIndex = 0;
        Programcounter = 0;
        timePassed = 0;
        writeWReg(0);
        MyFileReader.resetProgram();
        MyFrame.updateFieldWEST();
        MyFrame.updateFieldEAST();
        MyFrame.createListing(MyFrame.uploaded_file_path);
    }

}
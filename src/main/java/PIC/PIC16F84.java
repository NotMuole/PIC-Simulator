package PIC;

import UI.CenterPanel.Checkbox;
import UI.MainFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.ArrayList;

public class PIC16F84 {

    private static int[] Programstore = new int[1024];
    private static int[] RAM = new int[256];
    private static int[] Stack = new int[8];
    private static int StackIndex = 0;
    private static int Programcounter = 0;
    private static int WReg = 0;
    private static int rp0 = 0;
    public static int ind = 0;
    private static int dataLatch = 0;
    private static volatile boolean is_paused = true;
    public static boolean watchdogEnabled = true;
    public static boolean ui = false;
    private static double clockRate = 4.0;
    private static double timePerClockUs = 1 / clockRate;
    private static double timePerCycleUs = 4 / clockRate;
    private static double timePassed = 0;
    private static double delay = 5 / clockRate;
    private static int prevRA4;
    private static int T0SE = 1;
    private static int T0CS = 1;
    private static int PSA = 1;
    public static int PSA0_2 = 7;
    public static float watchdogTimer = 0;
    private static boolean watchdogOverflow = false;
    private static int helperTimer = 0;
    public static int GIE = 0;
    public static int EEIE = 0;
    public static int T0IE = 0;
    public static int INTE = 0;
    public static int RBIE = 0;
    public static int T0IF = 0;
    public static int INTF = 0;
    public static int RBIF = 0;
    public static int INTEDG = 1;
    public static int TO = 1;
    public static int PD = 1;
    public static boolean isSleep = false;
    private static final Logger log = LogManager.getLogger(PIC16F84.class);

    public PIC16F84() {}

    public static List<Integer> decodeAddress(int address) {
        if (rp0 == 1 && address < 128) address += 128;

        List<Integer> addresses = new ArrayList<>();

        // indirect
        if (address == 0 || address == 128) {
            addresses.add(ind);

        // pcl
        } else if (address == 2 || address == 130) {
            addresses.add(2);
            addresses.add(130);
        
        // status
        } else if (address == 3 || address == 131) {
            addresses.add(3);
            addresses.add(131);

        // fsr
        } else if (address == 4 || address == 132) {
            addresses.add(4);
            addresses.add(132);

        // pclath
        } else if (address == 10 || address == 138) {
            addresses.add(10);
            addresses.add(138);

        // intcon
        } else if (address == 11 || address == 139) {
            addresses.add(11);
            addresses.add(139);

        // general purpose register
        } else if (address > 11 && address < 80) {
            addresses.add(address);
        
        // mapped genereal purpose register
        } else if (address > 139 && address < 208) {
            addresses.add(address - 128);

        } else {
            addresses.add(address);
        }
        return addresses;
    }

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

    public static void setClockRate(float value) {
        clockRate = value;
        timePerCycleUs = 4 / clockRate;
        timePerClockUs = 1 / clockRate;
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

    public static void resetProgramtore() {
        Programstore = new int[1024];
    }

    public static void writeRAM(int address, int value) {
        if (address == 3 || address == 131) {
            rp0 = (value & 32) >> 5;
        } else if (address == 7 || (address < 128 && address > 79) || address == 135) {
            value = 0;
        } else if (address == 4 || address == 132) {
            ind = value;
        } else if (address == 5 && ui) {
            int TRISA = RAM[133];
            int PORTA = RAM[5];
            dataLatch = value;
            value = ((~TRISA & PORTA) | (TRISA & value));
            ui = false;
        } else if (address == 6 && ui) {
            int TRISB = RAM[134];
            int PORTB = RAM[6];
            dataLatch = value;
            value = ((~TRISB & PORTB) | (TRISB & value));
            ui = false;
            setINTF(value & 1);
            setRBIF(value & 240);
        } else if (address == 133) {
            int TRISA = RAM[133];
            int PORTA = RAM[5];
            PORTA = ((dataLatch & TRISA) | PORTA);
            RAM[5] = PORTA;
        } else if (address == 134) {
            int TRISB = RAM[134];
            int PORTB = RAM[6];
            PORTB = ((dataLatch & TRISB) | PORTB);
            RAM[6] = PORTB;
        } else if (address == 129) {
            INTEDG = (value & 64) >> 6;
            T0CS = (value & 32) >> 5;
            T0SE = (value & 16) >> 4;
            PSA = (value & 8) >> 3;
            PSA0_2 = value & 7;
            helperTimer = 0;
        } else if (address == 1) {
            helperTimer = 0;
            if (value > 255) {
                T0IF = 1;
                int newValue = getRAM(11) | 4;
                writeRAM(11, newValue);
                writeRAM(139, newValue);
                if (GIE == 1 && T0IE == 1) {
                    interrupt();
                }
            }
        } else if (address == 11 || address == 139) {
            GIE = (value & 128) >> 7;
            EEIE = (value & 64) >> 6;
            T0IE = (value & 32) >> 5;
            INTE = (value & 16) >> 4;
            RBIE = (value & 8) >> 3;
            T0IF = (value & 4) >> 2;
            INTF = (value & 2) >> 1;
            RBIF = (value & 1);
        }
        RAM[address] = value & 255;
    }

    public static int getVisualizedRAM(int address) {
        return RAM[address];
    }

    public static int getRAM(int address) {
        if ((rp0 == 1) && (address < 127)) {
            address += 128;
        }
        return RAM[address];
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

    public static void reset() {
        RAM = new int[256];
        // set TRIS to input to avoid damage
        writeRAM(133, 255);
        writeRAM(134, 255);

        // set INCONT
        writeRAM(11, 0);

        // set OPTION
        writeRAM(129, 255);

        // set STATUS
        writeRAM(3, 24);

    }

    public static void pushStack(int addresse) {
        Stack[StackIndex] = addresse;
        StackIndex += 1;
        StackIndex = StackIndex % 8;
    }

    public static int popStack() {
        if (StackIndex != 0) {
            StackIndex -= 1;
        } else {
            StackIndex = 7;
        }
        return Stack[StackIndex];
    }

    public static int[] getStack() {
        return Stack;
    }

    public static int getStackIndex() {
        return StackIndex;
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
        List<Integer> addresses = decodeAddress(file_address);
        int value = WReg + getRAM(addresses.get(0));

        if (value == 0) setZeroFlag(); else clearZeroFlag();
        if (value > 255) setCarryFlag(); else clearCarryFlag();
        if ((getRAM(addresses.get(0)) & 15) + (WReg & 15) > 15) setDigitcarryFlag(); else clearDigitcarryFlag();
        
        if (destination == 0) {
            writeWReg(value);
        } else {
            for (int i = 0; i < addresses.size(); i ++) {
                writeRAM(addresses.get(i), value);
            }
        }
        incrementProgramCounter();
        updateTime(4);
        //log.info("ADDWF, WReg: \" + Integer.toHexString(WReg) + \"h, C=\" + getCarryFlag() + \", DC=\" + getDigitcarryFlag() + \", Z=\" + getZeroFlag()");
    }

    public static void ANDWF(int file_address, int destination) {
        List<Integer> addresses = decodeAddress(file_address);
        int value = WReg & getRAM(addresses.get(0));

        if (value == 0) setZeroFlag(); else clearZeroFlag();

        if (destination == 0) {
            writeWReg(value);

        } else {
            for (int i = 0; i < addresses.size(); i ++) {
                writeRAM(addresses.get(i), value);
            }
        }
        incrementProgramCounter();
        updateTime(4);
        //log.info("ANDWF, WReg: \" + Integer.toHexString(WReg) + \"h, C=\" + getCarryFlag() + \", DC=\" + getDigitcarryFlag() + \", Z=\" + getZeroFlag()");
    }

    public static void CLRF(int file_address) {
        List<Integer> addresses = decodeAddress(file_address);

        setZeroFlag();

        for (int i = 0; i < addresses.size(); i ++) {
            writeRAM(addresses.get(i), 0);
        }
        incrementProgramCounter();
        updateTime(4);
        //log.info("CLRF");
    }

    public static void CLRW() {
        setZeroFlag();

        writeWReg(0);
        incrementProgramCounter();
        updateTime(4);
        //log.info("CLRW");
    }

    public static void COMF(int file_address, int destination) {
        List<Integer> addresses = decodeAddress(file_address);
        int value = getRAM(addresses.get(0)) ^ 255;

        if (value == 0) setZeroFlag(); else clearZeroFlag();

        if (destination == 0) {
            writeWReg(value);
        } else {
            for (int i = 0; i < addresses.size(); i ++) {
                writeRAM(addresses.get(i), value);
            }
        }
        incrementProgramCounter();
        updateTime(4);
        //log.info("COMF");
    }

    public static void DECF(int file_address, int destination) {
        List<Integer> addresses = decodeAddress(file_address);
        int value = getRAM(addresses.get(0)) - 1;

        if (value == 0) setZeroFlag(); else clearZeroFlag();

        if (destination == 0) {
            writeWReg(value);
        } else {
            for (int i = 0; i < addresses.size(); i ++) {
                writeRAM(addresses.get(i), value);
            }
        }
        incrementProgramCounter();
        updateTime(4);
        //log.info("DECF");
    }

    public static void DECFSZ(int file_address, int destination) {
        List<Integer> addresses = decodeAddress(file_address);
        int value = getRAM(addresses.get(0)) - 1;

        if (destination == 0) {
            writeWReg(value);
        } else {
            for (int i = 0; i < addresses.size(); i ++) {
                writeRAM(addresses.get(i), value);
            }
        }

        if (value == 0) { 
            NOP();
            incrementProgramCounter();
            incrementTMR0();
        }
        incrementProgramCounter();
        updateTime(4);
        //log.info("DECFSZ");
    }

    public static void INCF(int file_address, int destination) {
        List<Integer> addresses = decodeAddress(file_address);
        int value = getRAM(addresses.get(0)) + 1;

        if (value > 255) setZeroFlag(); else clearZeroFlag();

        if (destination == 0) {
            writeWReg(value);
        } else {
            for (int i = 0; i < addresses.size(); i ++) {
                writeRAM(addresses.get(i), value);
            };
        }
        incrementProgramCounter();
        updateTime(4);
        //log.info("INCF");
    }

    public static void INCFSZ(int file_address, int destination) {
        List<Integer> addresses = decodeAddress(file_address);
        int value = getRAM(addresses.get(0)) + 1;

        if (destination == 0) {
            writeWReg(value);
        } else {
            for (int i = 0; i < addresses.size(); i ++) {
                writeRAM(addresses.get(i), value);
            };
        }

        if (value > 255) {
            NOP();
            incrementProgramCounter();
            incrementTMR0();
        }
        incrementProgramCounter();
        updateTime(4);
        //log.info("INCFSZ");
    }

    public static void IORWF(int file_address, int destination) {
        List<Integer> addresses = decodeAddress(file_address);
        int value = WReg | getRAM(addresses.get(0));

        if (value == 0) setZeroFlag(); else clearZeroFlag();

        if (destination == 0) {
            writeWReg(value);
        } else {
             for (int i = 0; i < addresses.size(); i ++) {
                writeRAM(addresses.get(i), value);
            };
        }

        incrementProgramCounter();
        updateTime(4);
        //log.info("IORWF");
    }

    public static void MOVF(int file_address, int destination) {
        List<Integer> addresses = decodeAddress(file_address);
        int value = getRAM(addresses.get(0));

        if (value == 0) setZeroFlag(); else clearZeroFlag();

        if (destination == 0) {
            writeWReg(value);
        } else {
            for (int i = 0; i < addresses.size(); i ++) {
                writeRAM(addresses.get(i), value);
            };
        }
        incrementProgramCounter();
        updateTime(4);
        //log.info("MOVF");
    }

    public static void MOVWF(int file_address) {
        List<Integer> addresses = decodeAddress(file_address);
        int value = getWReg();

        for (int i = 0; i < addresses.size(); i ++) {
            writeRAM(addresses.get(i), value);
        };
        incrementProgramCounter();
        updateTime(4);
        //log.info("MOVWF, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void NOP() {
        incrementProgramCounter();
        updateTime(4);
        //log.info("NOP, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void RLF(int file_address, int destination) {
        List<Integer> addresses = decodeAddress(file_address);
        int carry = (getRAM(addresses.get(0)) & 128) >> 7;
        int value = (getRAM(addresses.get(0)) << 1) + getCarryFlag();

        if (carry == 1) setCarryFlag(); else clearCarryFlag();

        if (destination == 0) {
            writeWReg(value);
        } else {
            for (int i = 0; i < addresses.size(); i ++) {
                writeRAM(addresses.get(i), value);
            };
        }
        incrementProgramCounter();
        updateTime(4);
        //log.info("RLF");
    }

    public static void RRF(int file_address, int destination) {
        List<Integer> addresses = decodeAddress(file_address);
        int carry = (getRAM(addresses.get(0)) & 1);
        int value = (getRAM(addresses.get(0)) >> 1) + (getCarryFlag() << 7);

        if (carry == 1) setCarryFlag(); else clearCarryFlag();

        if (destination == 0) {
            writeWReg(value);
        } else {
            for (int i = 0; i < addresses.size(); i ++) {
                writeRAM(addresses.get(i), value);
            };
        }
        incrementProgramCounter();
        updateTime(4);
        //log.info("RRF");
    }

    public static void SUBWF(int file_address, int destination) {
        List<Integer> addresses = decodeAddress(file_address);
        int value = getRAM(addresses.get(0)) - WReg;

        if (value == 0) setZeroFlag(); else clearZeroFlag();
        if (getRAM(addresses.get(0)) >= WReg) setCarryFlag(); else clearCarryFlag();
        if ((getRAM(addresses.get(0)) & 15) >= (WReg & 15)) setDigitcarryFlag(); else clearDigitcarryFlag();

        if (destination == 0) {
            writeWReg(value);
        } else {
            for (int i = 0; i < addresses.size(); i ++) {
                writeRAM(addresses.get(i), value);
            };
        }
        incrementProgramCounter();
        updateTime(4);
        //log.info("SUBWF");
    }

    public static void SWAPF(int file_address, int destination) {
        List<Integer> addresses = decodeAddress(file_address);
        int topNibbles = (getRAM(addresses.get(0)) & 240) >> 4;
        int value = (getRAM(addresses.get(0)) << 4) + topNibbles;

        if (destination == 0) {
            writeWReg(value);
        } else {
            for (int i = 0; i < addresses.size(); i ++) {
                writeRAM(addresses.get(i), value);
            };
        }
        incrementProgramCounter();
        updateTime(4);
        //log.info("SWAPF");
    }

    public static void XORWF(int file_address, int destination) {
        List<Integer> addresses = decodeAddress(file_address);
        int value = WReg ^ getRAM(addresses.get(0));

        if (value == 0) setZeroFlag(); else clearZeroFlag();

        if (destination == 0) {
            writeWReg(value);
        } else {
            for (int i = 0; i < addresses.size(); i ++) {
                writeRAM(addresses.get(i), value);
            };
        }
        incrementProgramCounter();
        updateTime(4);
        //log.info("XORWF");
    }

    public static void BCF(int file_address, int bit) {
        List<Integer> addresses = decodeAddress(file_address);
        int value = getRAM(addresses.get(0))&~(1 << bit);

        for (int i = 0; i < addresses.size(); i ++) {
            writeRAM(addresses.get(i), value);
        };
        incrementProgramCounter();
        updateTime(4);
        //log.info("BCF");
    }

    public static void BSF(int file_address, int bit) {
        List<Integer> addresses = decodeAddress(file_address);
        int value = getRAM(addresses.get(0))|(1 << bit);

        for (int i = 0; i < addresses.size(); i ++) {
            writeRAM(addresses.get(i), value);
        };
        incrementProgramCounter();
        updateTime(4);
        //log.info("BSF");
    }

    public static void BTFSC(int file_address, int bit) {
        List<Integer> addresses = decodeAddress(file_address);
        int value = (getRAM(addresses.get(0))&(1 << bit)) >> bit;

        if (value == 0) {
            NOP();
            incrementProgramCounter();
            incrementTMR0();
        }
        incrementProgramCounter();
        updateTime(4);
        //log.info("BTFSC");
    }

    public static void BTFSS(int file_address, int bit) {
        List<Integer> addresses = decodeAddress(file_address);
        int value = (getRAM(addresses.get(0))&(1 << bit)) >> bit;

        if (value == 1) {
            NOP();
            incrementProgramCounter();
            incrementTMR0();
        }
        incrementProgramCounter();
        updateTime(4);
        //log.info("BTFSS");
    }

    public static void ADDLW(int literal) {
        int value = literal + WReg;

        if (value == 0) setZeroFlag(); else clearZeroFlag();
        if (value > 255) setCarryFlag(); else clearCarryFlag();
        if ((literal & 15) + (WReg & 15) > 15) setDigitcarryFlag(); else clearDigitcarryFlag();

        writeWReg(value);
        incrementProgramCounter();
        updateTime(4);
        //log.info("ADDLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void ANDLW(int literal) {
        int value = WReg & literal;

        if (value == 0) setZeroFlag(); else clearZeroFlag();

        writeWReg(value);
        incrementProgramCounter();
        updateTime(4);
        //log.info("ANDLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void CALL(int address) {
        incrementProgramCounter();
        pushStack(Programcounter);
        setProgramCounter(address & 1023);
        updateTime(8);
        incrementTMR0();
        //log.info("CALL, return-address=" + (Programcounter) + ", destination-address=" + (address & 1023));
    }

    public static void CLRWDT() {
        //set Bits & Variables
        watchdogTimer = 0;
        PSA0_2 = 0;
        int optionReg = getRAM(129);
        int newOptionReg = (optionReg & 248);
        writeRAM(129, newOptionReg);
        TO = 1;
        PD = 1;
        int statusReg = getRAM(3);
        int newStatusReg = (statusReg | 24);
        writeRAM(3, newStatusReg);
        writeRAM(131, newStatusReg);

        incrementProgramCounter();
        updateTime(4);
        //log.info("CLRWDT");
    }

    public static void GOTO(int address) {
        incrementProgramCounter();
        PIC16F84.setProgramCounter(address);
        updateTime(8);
        incrementTMR0();
        //log.info("GOTO, destination-address=" + (address & 1023));
    }

    public static void IORLW(int literal) {
        int value = WReg | literal;

        if (value == 0 ) setZeroFlag(); else clearZeroFlag();

        writeWReg(value);
        incrementProgramCounter();
        updateTime(4);
        //log.info("IORLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void MOVLW(int literal) {
        writeWReg(literal);
        incrementProgramCounter();
        updateTime(4);
        //log.info("MOVLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void RETFIE() {
        incrementProgramCounter();
        Programcounter = popStack();
        updateTime(8);
        //log.info("TODO: RETFIE");
    }

    public static void RETLW(int literal) {
        writeWReg(literal);
        Programcounter = popStack();
        incrementProgramCounter();
        updateTime(8);
        //log.info("RETLW, return-address=" + Programcounter + ", W=" + Integer.toHexString(WReg) + "h");
    }

    public static void RETURN() {
        incrementProgramCounter();
        Programcounter = popStack();
        updateTime(8);
        //log.info("RETURN");
    }

    public static void SLEEP() {
        watchdogTimer = 0;
        PSA0_2 = 0;
        writeRAM(129,getRAM(129) & 248);

        TO = 1;
        PD = 0;
        int value = (getRAM(3) | 16) & 247; 
        writeRAM(3, value);
        writeRAM(131, value);
        
        isSleep = true;
        updateTime(4);
        //log.info("SLEEP");
    }

    public static void SUBLW(int literal) {
        int value = literal - WReg;

        if (value == 0) setZeroFlag(); else clearZeroFlag();
        if (literal >= WReg) setCarryFlag(); else clearCarryFlag();
        if ((literal & 15) >= (WReg & 15)) setDigitcarryFlag(); else clearDigitcarryFlag();

        writeWReg(value);
        incrementProgramCounter();
        updateTime(4);
        //log.info("SUBLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void XORLW(int literal) {
        int value = literal ^ WReg;

        if (value == 0) setZeroFlag(); else clearZeroFlag();
        
        writeWReg(value);
        incrementProgramCounter();
        updateTime(4);
        //log.info("XORLW, WReg: " + Integer.toHexString(WReg) + "h, C=" + getCarryFlag() + ", DC=" + getDigitcarryFlag() + ", Z=" + getZeroFlag());
    }

    public static void updateTime(int anzahl) {
        for (int i=0; i<anzahl; i++) {
            if (watchdogOverflow) {
                return;
            }
            timePassed += timePerClockUs;
            if (watchdogEnabled) {
                watchdogTimer += timePerClockUs;
                if(PSA == 0 && watchdogTimer >= 18000) {
                    watchdogOverflow();
                } else if (PSA == 1 && watchdogTimer >= Math.pow(2, PSA0_2)*18000) {
                    watchdogOverflow();
                }
            }
        }
    }

    public static void watchdogOverflow() {
        watchdogOverflow = true;
        if (!isSleep) {
            is_paused = true;
            MainFrame.createPopUp();
        } else {
            isSleep = false;
            watchdogOverflow = false;
            watchdogTimer = 0;
            incrementProgramCounter();
        }
    }

    public static void setRBIF(int currentValue) {
        int previousValue = (getRAM(6) & 240);
        if (currentValue == previousValue) {
            return;
        } else {
            RBIF = 1;
            int value = getRAM(11) | 1;
            writeRAM(11, value);
            writeRAM(139, value);
            if (GIE == 1 && RBIE == 1) {
                interrupt();
            }
        }
    }

    public static void setINTF(int currentValue) {
        int previousValue = (getRAM(6) & 1);
        if (currentValue == previousValue) {
            return;
        } if (currentValue == INTEDG) {
            INTF = 1;
            int value = getRAM(11) | 2;
            writeRAM(11, value);
            writeRAM(139, value);
            if (GIE == 1 && INTE == 1) {
                interrupt();
            }
        } else {
            INTF = 0;
            int value = (getRAM(11) & 253);
            writeRAM(11, value);
            writeRAM(139, value);
        }
    }

    public static void interrupt() {
        isSleep = false;
        CALL(4);
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
        if (isSleep) {
            updateTime(4);   
            updateUI();
            return;
        }

        int command = getProgramstore(Programcounter);
        if (!isSleep) {
            decode(command);
        }
        //incrementProgramCounter();
        incrementTMR0();
        updateUI();

    }

    public static void resetProgram() {
        watchdogOverflow = false;
        isSleep = false;
        is_paused = true;
        reset();
        ui = false;
        Stack = new int[8];
        StackIndex = 0;
        Programcounter = 0;
        PSA = 1;
        PSA0_2 = 7;
        T0CS = 1;
        T0SE = 1;
        prevRA4 = 0;
        helperTimer = 0;
        timePassed = 0;
        watchdogTimer = 2302000;
        dataLatch = 0;
        GIE = 0;
        EEIE = 0;
        T0IE = 0;
        INTE = 0;
        RBIE = 0;
        T0IF = 0;
        INTF = 0;
        RBIF = 0;
        INTEDG = 1;
        TO = 1;
        PD = 1;
        writeWReg(0);
        updateUI();
    }

    public static void incrementTMR0() {
        int source;
        int timer = getRAM(1);
        boolean event = false;

        // erster Multiplexer, entscheidet anhand des T0CS-Bit über die Source
        if (T0CS == 0) {
            //source = CLKOUT TODO: verwende CLK als source
            event = true;
        } else if (T0CS == 1) {
            int RA4 = ((getRAM(5) & 16) >> 4);
            source = (T0SE ^ RA4);
            if (source == 1 && (prevRA4 != RA4)) {
                event = true;
            }
        } else {
            log.error("TOCS ist " + T0CS + " statt 0 oder 1");
        }

        prevRA4 = (getRAM(5) & 16) >> 4;
        if (!event) return;

        // zweiter Multiplexer, entscheidet anhand des PSA-Bit, ob Signal direkt zum Timer oder zunächst zum Prescaler geht
        if (PSA == 1) {
            updateTime(2);
            writeRAM(1, timer+1);
            helperTimer = 0;
        } else if (PSA == 0) {
            helperTimer++;
            if (helperTimer == Math.pow(2, PSA0_2+1)) {
                writeRAM(1,timer+1);
                helperTimer = 0;
            }
        } else {
            log.error("PSA ist " + PSA + " statt 0 oder 1");
        }
    }

    public static void updateUI() {
        if (!isSleep) {
            MainFrame.paintListing();
        }
        MainFrame.paintWestPanel();
        MainFrame.paintEastPanel();
    }

}
package PIC;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PIC16F84 {
    private static int[] Programmstore = new int[1024];
    private static final Logger log = LogManager.getLogger(PIC16F84.class);

    public PIC16F84() {};

    public static void writeProgrammstore(int address, int value) {
        Programmstore[address] = value;
    }

    public static int getProgrammstore(int address) {
        return Programmstore[address];
    }

    public static void decode(int code) {
            if ((code & 15360) == 12288) {
                int literal = code & 1023;
                log.info("MOVLW" + " mit Literal " + literal);
            }
    }

    public static void readProgrammstore() {
        for (int i = 0; i < 1024; i++) {
            log.info(getProgrammstore(i));
        }
    }
}

package Hardwareansteuerung;
import java.io.Console;

import com.fazecast.jSerialComm.*;

public class Hardwareansteuerung {
    static SerialPort port;

    public static void initialize() {
        port = SerialPort.getCommPort("COM3");

        port.setComPortParameters(
            4800,
            8,
            SerialPort.ONE_STOP_BIT,
            SerialPort.NO_PARITY
        );  

        port.setComPortTimeouts(
            SerialPort.TIMEOUT_READ_BLOCKING,
            0,
            0
        );

        if (!port.openPort()) {
            System.out.println("Port konnte nicht geöffnet werden!");
            return;
        }
    }


    public static void print(int trisA, int trisB, int portA, int portB) {
        String stringTrisA = String.format("%02X", trisA);
        String stringTrisB = String.format("%02X", trisB);
        String stringPortA = String.format("%02X", portA);
        String stringPortB = String.format("%02X", portB);

        String translatedTrisA = "3" + stringTrisA.substring(0,1) + "3" + stringTrisA.substring(1,2);
        String translatedTrisB = "3" + stringTrisB.substring(0,1) + "3" + stringTrisB.substring(1,2);
        String translatedPortA = "3" + stringPortA.substring(0,1) + "3" + stringPortA.substring(1,2);
        String translatedPortB = "3" + stringPortB.substring(0,1) + "3" + stringPortB.substring(1,2);
        String output = translatedTrisA + translatedPortA + translatedTrisB + translatedPortB + (char)13;
        System.out.println(output.getBytes());
        port.writeBytes(output.getBytes(), output.length());
    }

    public static void poll() {

    }
}

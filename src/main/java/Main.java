import java.io.File;

import UI.Window;
import file.MyFileReader;

public class Main {
    public static void main(String[] args) {
        Window.init();
        MyFileReader reader = new MyFileReader();
        reader.readFile(new File("C:\\_work\\studium\\DHBW\\PIC-Simulator\\Tests\\TPicSim1.LST"));
    }
}
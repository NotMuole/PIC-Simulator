import PIC.PIC16F84;
import Service.Dataservice;
import UI.MainFrame;

public class Main {
    public static void main(String[] args) {
        Dataservice data = new Dataservice();
        new PIC16F84(data);
        new MainFrame();
    }
}
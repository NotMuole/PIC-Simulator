package PIC;

public class RamRegister extends Register{
    protected int[] ramAddresses;
    protected int value;

    public RamRegister(int[] ramAddresses, int valueOnReset) {
        this.ramAddresses = ramAddresses;
        this.value = valueOnReset;
        writeInRAM();
    }

    @Override
    public void setValue(int value) {
        this.value = value;
        writeInRAM();
    }

    private void writeInRAM() {
        for (int address : ramAddresses) {
            PIC16F84.writeRAM(address, this.value);
        }
    }

}
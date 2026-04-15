package PIC;

public class SpecialBitRegister extends Register {
    private int[] ramAddresses;
    private SpecialBit[] specialBits;

    public SpecialBitRegister(SpecialBit[] specialBits, int[] ramAddresses) {
        this.ramAddresses = ramAddresses;
        this.specialBits = specialBits;
        for (int i = 0; i < 8; i++) {
            this.value += specialBits[i].get() << i;
        }
        writeInRAM();
    }

    public SpecialBit getBit(int index) {
        return this.specialBits[index];
    }

    @Override
    public void setValue(int value) {
        this.value = value;

        for (int i=0; i<8; i++) {
            if ((value & (1 << i)) != 0) {
                this.specialBits[i].set();
            } else {
                this.specialBits[i].clear();
            }
        }

        writeInRAM();
    }

    public void update() {
        for (int i = 0; i < 8; i++) {
            this.value += specialBits[i].get() << i;
        }
        writeInRAM();
    }

    private void writeInRAM() {
        for (int address : ramAddresses) {
            PIC16F84.writeRAM(address, this.value);
        }
    }

}
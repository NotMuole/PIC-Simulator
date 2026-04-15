package PIC;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpecialBit {
    private static final Logger log = LogManager.getLogger(SpecialBit.class);
    public int valueOnReset;
    public int value;

    public void SpecialBit(int valueOnReset) {
        this.valueOnReset = valueOnReset;
        this.value = this.valueOnReset;
    }

    public void set() {
        this.value = 1;
    }

    public void clear() {
        this.value = 0;
    }

    public int get() {
        return this.value;
    }
}
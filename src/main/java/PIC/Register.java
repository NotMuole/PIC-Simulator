package PIC;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Register {
    protected static final Logger log = LogManager.getLogger(Register.class);
    protected int value;

    public Register() {}

    public Register(int valueOnReset) {
        this.value = valueOnReset;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value & 255;
    }

}
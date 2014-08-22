package co.uk.vonaq.cableguardian.core.development;

import co.uk.vonaq.cableguardian.gpio.GPIOControl;
import java.io.IOException;

//this class is usefull for test on system where is no gpio(like laptop.)
public class FakeGpioControl implements GPIOControl {

    public FakeGpioControl() throws IOException {
    }

    @Override
    public void sendData(int gpioChannel, boolean value) throws IOException {
    }
}

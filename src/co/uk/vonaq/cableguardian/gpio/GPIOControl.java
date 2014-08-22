package co.uk.vonaq.cableguardian.gpio;

import com.google.inject.ImplementedBy;
import java.io.IOException;

@ImplementedBy(DefaultGPIOControl.class)
public interface GPIOControl {

    public void sendData(int gpioChannel, boolean value) throws IOException;
}

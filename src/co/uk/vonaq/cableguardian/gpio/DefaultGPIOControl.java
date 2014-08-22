package co.uk.vonaq.cableguardian.gpio;

import javax.inject.Singleton;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Singleton
public class DefaultGPIOControl implements GPIOControl {

    //private static GPIOControl gpioControlObject;
    private FileWriter unexportFile;
    private FileWriter exportFile;
    private FileWriter[] commandChannels;
    private static final String GPIO_OUT = "out";
    private static final String GPIO_ON = "1";
    private static final String GPIO_OFF = "0";
    private static String LED0_PATH = "/sys/class/leds/beaglebone:green:usr0";

    public DefaultGPIOControl() throws IOException {
        // Open file handles to GPIO port unexport and export controls
        unexportFile = new FileWriter("/sys/class/gpio/unexport");
        //exportFile = new FileWriter("/sys/class/gpio/export");
        commandChannels = new FileWriter[100];
    }

    private void export(int gpioChannel) throws IOException {
        // Reset the port, if needed
        File exportFileCheck = new File("/sys/class/gpio/gpio" + gpioChannel);
        if (!exportFileCheck.exists()) {
            exportFile = new FileWriter("/sys/class/gpio/export");
            exportFile.write("" + gpioChannel);
            exportFile.close();
        }
    }

    private void setDirectionOut(int gpioChannel) throws IOException {
        // Open file handle to port input/output control
        FileWriter directionFile = new FileWriter("/sys/class/gpio/gpio" + gpioChannel + "/direction");
        // Set port for output
        directionFile.write(GPIO_OUT);
        directionFile.flush();
        directionFile.close();
    }

    @Override
    public void sendData(int gpioChannel, boolean value) throws IOException {
        if (commandChannels[gpioChannel] == null) {
            export(gpioChannel);
            setDirectionOut(gpioChannel);
            commandChannels[gpioChannel] = new FileWriter("/sys/class/gpio/gpio" + gpioChannel + "/value");
        }
        if (value) {
            commandChannels[gpioChannel].write(GPIO_ON);
        } else {
            commandChannels[gpioChannel].write(GPIO_OFF);
        }
        commandChannels[gpioChannel].flush();
    }
}

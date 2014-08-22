package co.uk.vonaq.cableguardian.multiplexer;

import co.uk.vonaq.cableguardian.Inject.Log;
import co.uk.vonaq.cableguardian.gpio.GPIOControl;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RelayControl {

    @Log
    private Logger logger;
    @Inject
    private GPIOControl control;
    private final static int A = 66;
    private final static int B = 67;
    private final static int C = 69;
    private final static int D = 68;
    private final static int E = 45;
    private final static int F = 44;
    private final static int ENABLE = 23;
    private final static int SUPY1 = 26;
    private final static int SUPY2 = 47;
    private final static int SUPY3 = 46;
    private final static int SUPY4 = 27;
    private final static int CALIBRATIONRELAY = 65;

    public void setCable(int number) {
//        System.out.println("setcable()"+number);
        try {
            int theByte = number - 1;
            boolean d = (theByte & 0x1) != 0;
            boolean c = (theByte & 0x2) != 0;
            boolean b = (theByte & 0x4) != 0;
            boolean a = (theByte & 0x8) != 0;
            boolean e = (theByte & 0x10) != 0;
            boolean f = (theByte & 0x20) != 0;
//            int d_ = d ? 1 : 0;
//            int c_ = c ? 1 : 0;
//            int b_ = b ? 1 : 0;
//            int a_ = a ? 1 : 0;
//            int e_ = e ? 1 : 0;
//            int f_ = f ? 1 : 0;
            //      boolean g = (theByte & 0x40) != 0;
            //      boolean h = (theByte & 0x80) != 0;
            //        ystem.out.println("          feabcd");
            //ystem.out.println("Bytes Are "+d_+c_+b_+a_+e_+f_);
            //        ystem.out.println("Bytes Are "+f_+e_+a_+b_+c_+d_);
            control.sendData(A, a);
            control.sendData(B, b);
            control.sendData(C, c);
            control.sendData(D, d);
            control.sendData(E, e);
            control.sendData(F, f);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
        }
    }

    public void enable() throws IOException {
        control.sendData(ENABLE, false);
    }

    public void disable() throws IOException {
        control.sendData(ENABLE, true);
    }

    public void enableCalibration() throws IOException {
        control.sendData(CALIBRATIONRELAY, true);
    }

    public void disableCalibration() throws IOException {
        control.sendData(CALIBRATIONRELAY, false);
    }
}

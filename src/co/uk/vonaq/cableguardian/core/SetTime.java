package co.uk.vonaq.cableguardian.core;

import co.uk.vonaq.cableguardian.Inject.Log;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

//on startup try to set up the time.
public class SetTime extends Thread {

    @Log private Logger logger;

    @Override
    public void run() {
        try {
            Thread.currentThread().setName("SetTime");
            logger.log(Level.INFO, "Setting the time");
            String cmd = "ntpdate -b -s -u pool.ntp.org";
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(cmd);
            pr.waitFor();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(SetTime.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

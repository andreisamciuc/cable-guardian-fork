package co.uk.vonaq.cableguardian.core;

import co.uk.vonaq.cableguardian.Inject.Log;
import co.uk.vonaq.cableguardian.db.CalibrationTable;
import co.uk.vonaq.cableguardian.db.SettingsTable;
import co.uk.vonaq.cableguardian.multiplexer.Multiplexer;
import co.uk.vonaq.cableguardian.multiplexer.RelayControl;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

public class CalibrateLoop extends Thread {
    @Log
    private Logger logger;
    @Inject
    private Multiplexer multiplexer;
    @Inject
    private CalibrationTable calibrationTable;
    @Inject
    private SettingsTable settingsTable;
    @Inject
    private RelayControl relayControl;

    public void run() {
        Thread.currentThread().setName("CalibrateLoop");
        System.out.println("CalibrateLoop");
        while (true) {
            try {
                relayControl.enableCalibration();
                int cableNumber = Integer.parseInt(settingsTable.getSetting("calibrate"));
                calibrate(cableNumber);
                if (!settingsTable.isCalibrationOn()) {
                    logger.log(Level.INFO, "leaving CalibrateLoop");
                    return;
                }
                logger.log(Level.INFO, "CalibrateLoop");
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
                return;
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    private void calibrate(int cableNumber) {
        try {
            multiplexer.setCable(cableNumber);
            int length = multiplexer.getLenth();
            calibrationTable.putMeasurement(cableNumber, length);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        } catch (IOException | SQLException | NullPointerException e) {
            logger.log(Level.SEVERE, "", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
        }
    }
}

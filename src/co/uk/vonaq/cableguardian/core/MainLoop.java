package co.uk.vonaq.cableguardian.core;

import co.uk.vonaq.cableguardian.Inject.Log;
import co.uk.vonaq.cableguardian.alarm.AlarmAnnouncer;
import co.uk.vonaq.cableguardian.db.AlarmsTable;
import co.uk.vonaq.cableguardian.db.CableTable;
import co.uk.vonaq.cableguardian.db.SettingsTable;
import co.uk.vonaq.cableguardian.db.SwitchTable;
import co.uk.vonaq.cableguardian.db.TestsTable;
import co.uk.vonaq.cableguardian.exceptions.BoardDataException;
import co.uk.vonaq.cableguardian.log.DatabaseLogger;
import co.uk.vonaq.cableguardian.multiplexer.Multiplexer;
import co.uk.vonaq.cableguardian.multiplexer.RelayControl;
import com.google.inject.Injector;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

public class MainLoop extends Thread {
    private static final int iterationTime = 5;//how long one all cable check lasts
    private int iteration = 0;
    @Log
    private Logger logger;
    @Inject
    private Multiplexer multiplexer;
    @Inject
    private CableTable cablesTable;
    @Inject
    private SwitchTable switchTable;
    @Inject
    private AlarmAnnouncer alarmAnnouncer;
    @Inject
    private TestsTable testsTable;
    @Inject
    private AlarmsTable alarmsTable;
    @Inject
    private DatabaseLogger dbLogger;
    @Inject
    private SettingsTable settingsTable;
    @Inject
    private Injector injector;
    @Inject
    private Constants constants;
    @Inject
    private RelayControl relayControl;

    @Override
    public void run() {
        Thread.currentThread().setName("MainLoop");
        logger.log(Level.INFO, "Main loop is started");
        while (true) {
            try {
                if (settingsTable.isCalibrationOn()) {
                    Thread t = injector.getInstance(CalibrateLoop.class);
                    t.start();
                    t.join();
                }
            } catch (SQLException | InterruptedException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            testCables();
        }
    }

    private void testCables() {
        Set<Integer> cableSet = null;
        try {
            relayControl.disableCalibration();
            cableSet = cablesTable.getTestableCables();
            int cablesInTheRow = cableSet.size();
            for (Integer cableNumber : cableSet) {
                //if cable is switch and has no nodes then do not test.
                boolean cableIsSwitch = cablesTable.isCableSwitch(cableNumber);
                if(cableIsSwitch){
                    if(switchTable.doesCableHaveNodes(cableNumber)){
                        testCable(cableNumber);
                    }
                }else{
                    testCable(cableNumber);
                }
            }
            logger.log(Level.INFO, "Just tested " + cablesInTheRow + " cables, iteration " + iteration++);
        } catch (IOException | SQLException | NullPointerException e) {
            logger.log(Level.SEVERE, "", e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
        } finally {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
            }
            //sleeper.sleepResponsibly(iterationTime);
        }
    }
    
    private void testCable(int cableNumber) throws SQLException, IOException {
        try {
            multiplexer.setCable(cableNumber);
            int tolerance = cablesTable.getTolerance(cableNumber);
            int cableAverage = testsTable.getAverageCableUntilGood(cableNumber);
            int length = multiplexer.getLenth();
            if(length<0)return;
            int difference = calculateDifference(cableAverage, length);
            
            if (alarmsTable.getAlarmExists(cableNumber)) {
                int lastValue = testsTable.getLastMeasurement(cableNumber);
                testsTable.putMeasurement(cableNumber, "Fail Continue", lastValue, length);
            } else if ((cableAverage != 0) && (difference > tolerance) && (length < cableAverage)) {
                //if shorter than average.
                handleAnAlarm(cableNumber, length, cableAverage);
            } else {//if cable is ok
                testsTable.putMeasurement(cableNumber, "OK", cableAverage, length);
            }
            
        } catch (BoardDataException e1) {
            logger.log(Level.SEVERE, "BoardDataException", e1);
        }
    }

    //difference in percents
    private int calculateDifference(int cableAverage, int length) {
        //on low values (under 20) if difference is 10 or less than metres return percentage 0
        if (Math.abs(cableAverage-length)<11) {
            return 0;
        }
        if (cableAverage == 0) {
            return 0;
        }
        return Math.abs(((cableAverage - length) * 100) / cableAverage);
    }

    private String getFailMessageForCable(int cableNumber, int diff, boolean cableIsSwitch, int switchNumber) throws SQLException {
        if(cableIsSwitch){
            return constants.phrases.getString("fail")+ " " + constants.phrases.getString("on_cable")
                    + " " + cableNumber + ". " +constants.phrases.getString("switch")
                    + " " + switchTable.getNodeDescription(switchNumber)
                    + " " + constants.phrases.getString("open");
        }else{
            return constants.phrases.getString("fail") + " " + constants.phrases.getString("exceeded") 
                    + " " + constants.phrases.getString("on_cable") + " " + cableNumber + ". "
                    + " " + constants.phrases.getString("difference") + (diff);  
        }
    }

    void handleAnAlarm(int cableNumber, int length, int cableAverage) throws SQLException {
        int lastValue = testsTable.getLastMeasurement(cableNumber);
        boolean cableIsSwitch = cablesTable.isCableSwitch(cableNumber);
        int switchNumber = 0;//if switch number is 0 we  will assume that cable was not switch.
        if (cableIsSwitch){
            switchNumber = switchTable.getNodeForCableByDistance(cableNumber, length);
        }
        String failMessage = getFailMessageForCable(cableNumber, cableAverage - length, cableIsSwitch, switchNumber);
        int newKey = testsTable.putMeasurement(cableNumber, failMessage, lastValue, length);
            newKey = alarmsTable.putAlarm("New", cableNumber, newKey, failMessage,switchNumber);
        boolean alarmEnabled = cablesTable.getAlarmEnabled(cableNumber);
        //dbLogger.log(constants.phrases.getString("cable") + " " + cableNumber + " " + constants.phrases.getString("exceeded"));
        dbLogger.log(failMessage);
        if (alarmEnabled) {
            alarmAnnouncer.announce(newKey);
        }
    }

}

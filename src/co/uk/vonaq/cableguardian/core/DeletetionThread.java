package co.uk.vonaq.cableguardian.core;

import co.uk.vonaq.cableguardian.Inject.Log;
import co.uk.vonaq.cableguardian.db.TestsTable;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

//this thread deletes all excess data from database
public class DeletetionThread extends Thread {
    private static final int iterationTime = 500000;//how long to sleep
    private static final int numberOfRecordsToLeave = 5000;//how long to sleep
    @Log
    private Logger logger;
    @Inject
    private TestsTable testsTable;

    @Override
    public void run() {
        Thread.currentThread().setName("Deletion Thread");
        logger.log(Level.INFO, "Deletion Thread loop is started");
        while (true) {
            try {
                try {
                    testsTable.deleteOldestTests(numberOfRecordsToLeave);
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
                logger.log(Level.INFO, "Deletion Thread iteration");
                Thread.sleep(iterationTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(DeletetionThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}

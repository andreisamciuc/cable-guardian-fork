package co.uk.vonaq.cableguardian.db;

import co.uk.vonaq.cableguardian.Inject.Log;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LogTable {
    @Log private Logger logger;
    @Inject private DBConnector dBConnector;

    public void putLogString(String message) throws SQLException {
        //todo translate
        logger.log(Level.INFO, "The data is being logged into database: " + message);
        dBConnector.executeUpdate("INSERT INTO cable_guardian.log (value) VALUES (?)",message);
        //try {Thread.sleep(1);}catch(Exception e){}
    }
}

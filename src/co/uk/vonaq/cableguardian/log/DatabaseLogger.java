package co.uk.vonaq.cableguardian.log;

import co.uk.vonaq.cableguardian.Inject.Log;
import co.uk.vonaq.cableguardian.core.Constants;
import co.uk.vonaq.cableguardian.db.LogTable;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DatabaseLogger {

    @Log
    private Logger logger;
    @Inject
    private LogTable logTable;
    @Inject
    private Constants constants;

    public void log(String log) throws SQLException {
        System.out.println("-------------------- log");
        logTable.putLogString(log);
        logger.log(Level.INFO, "The data is being logged into database: " + log);
    }
    
    public void logSMS(String number, String smsDate, String insertDate, String text) throws SQLException {
        System.out.println("-------------------- logSMS");
        String log
                = constants.phrases.getString("sms_received") + ": "
                + constants.phrases.getString("number") + ": " + number + " "
                + constants.phrases.getString("sms_date") + ": " + smsDate + " "
                + constants.phrases.getString("received_date") + ": " + insertDate + " "
                + constants.phrases.getString("sms_text") + ": " + text + " ";
        logTable.putLogString(log);
    }

    public void logEmail(String number, String insertDate, String text) throws SQLException {
        System.out.println("-------------------- logEmail");
        String log = constants.phrases.getString("email_received") + ": "
                + constants.phrases.getString("from") + ": " + number + " "
                + constants.phrases.getString("received_date") + ": " + insertDate + " "
                + constants.phrases.getString("sms_text") + ": " + text + " ";
        logTable.putLogString(log);
    }

    public void logFailedSMS(String number, int errCode, String insertDate, String text) throws SQLException {
        System.out.println("-------------------- logFailedSMS");
        String log
                = constants.phrases.getString("sms_failed") + ". "
                + constants.phrases.getString("error_code") + ": " + errCode + " "
                + constants.phrases.getString("number") + ": " + number + " "
                + constants.phrases.getString("sms_date") + ": " + insertDate + " "
                + constants.phrases.getString("sms_text") + ": " + text + " ";
        logTable.putLogString(log);
    }

    public void logSMSOut(String number, String text) throws SQLException {
        System.out.println("-------------------- logSMSOut");
        String log
                = constants.phrases.getString("sms_sent") + ": "
                + constants.phrases.getString("number") + ": " + number + " "
                //              +Constants.phrases.getString("sms_date")+": "+smsDate+" "
                //              +Constants.phrases.getString("received_date")+": "+insertDate+" "
                + constants.phrases.getString("sms_text") + ": " + text + " ";
        logTable.putLogString(log);
    }
        
    public void logEmailOut(String number, String subject, String text) throws SQLException {
        System.out.println("-------------------- logEmailOut");
        String log
                = constants.phrases.getString("email_sent") + ": "
                + constants.phrases.getString("number") + ": " + number + " "
                + constants.phrases.getString("subject")+ ": " + subject+ " "
                //              +Constants.phrases.getString("sms_date")+": "+smsDate+" "
                //              +Constants.phrases.getString("received_date")+": "+insertDate+" "
                + constants.phrases.getString("sms_text") + ": " + text + " ";
        logTable.putLogString(log);
    }
    
}
